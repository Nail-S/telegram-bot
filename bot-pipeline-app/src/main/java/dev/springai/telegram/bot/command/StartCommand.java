package dev.springai.telegram.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static dev.springai.telegram.bot.util.BotUtils.createMessage;

@Slf4j
public class StartCommand extends BotCommand {
    private final ChatMemory chatMemory;
    public StartCommand(String commandIdentifier, String description, ChatMemory chatMemory) {
        super(commandIdentifier, description);
        this.chatMemory = chatMemory;
    }
    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] arguments) {
        chatMemory.clear(chat.getId().toString());
        try {
            telegramClient.execute(
                    createMessage(
                            chat.getId(),
                            "Vamos a empezar nuestra conversacion de nuevo"
                    )
            );
        } catch (TelegramApiException e) {
            log.error("An exception occured whike starting a new chat conversation", e);
        }
    }
}
