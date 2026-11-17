package dev.springai.telegram.bot.service;

import dev.springai.telegram.bot.conf.properties.TelegramProps;
import dev.springai.telegram.bot.gateway.ChatClientGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandLongPollingTelegramBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.StructuredTaskScope;

import static dev.springai.telegram.bot.util.BotUtils.createMessage;

@Slf4j
@Service
public class TelegramBotService extends CommandLongPollingTelegramBot {
    private final ChatClientGateway gateway;
    public TelegramBotService(ChatClientGateway gateway, TelegramClient telegramClient, TelegramProps props, Set<BotCommand> commands) {
        super(telegramClient,
                false,
                () -> props.getBotName()
                );
        registerAll(commands.toArray(new BotCommand[commands.size()]));
        this.gateway = gateway;
    }
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                log.info("Message from chatId {} received: {}",
                        chatId,
                        text
                );
                var future = new CompletableFuture<String>();
                try (var scope = StructuredTaskScope.open(
                        StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {

                    scope.fork(() -> {
                        String assistantMessage = gateway.processUserMessage(text, chatId);
                        future.complete(assistantMessage);
                        return assistantMessage;
                    });

                    scope.fork(() -> {
                        String msg = future.join(); // wait for first step result
                        sendToTelegramClient(chatId, msg);
                        return null;
                    });

                    scope.join();
                } catch (InterruptedException e) {
                    log.error("Sending message within the chat was interrupted.");
                    if (Thread.interrupted()) {
                        Thread.currentThread().interrupt();
                    }
                }

            } else {
                sendToTelegramClient(chatId, "Solo entiendo los mensajes de texto");
            }
        }
    }

    private void sendToTelegramClient(Long chatId, String text) {
        try {
            telegramClient.execute(
                    createMessage(chatId, text)
            );
        } catch (TelegramApiException e) {
            log.error("Se produjo un error al enviar un mensaje a Telegram", e);
        }
    }
}
