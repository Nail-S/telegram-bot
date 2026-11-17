package dev.springai.telegram.bot.conf;

import dev.springai.telegram.bot.command.StartCommand;
import dev.springai.telegram.bot.conf.properties.TelegramProps;
import dev.springai.telegram.bot.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static dev.springai.telegram.bot.util.BotUtils.eliminateNonAlphaNumericSymbols;

@Slf4j
@Configuration
@EnableConfigurationProperties(TelegramProps.class)
public class TelegramBotConfig {

    @Bean
    public  TelegramClient telegramClient(TelegramProps properties) {
        return new OkHttpTelegramClient(
                eliminateNonAlphaNumericSymbols(properties.getToken())
        );
    }

    @Bean
    public StartCommand startCommand(ChatMemory chatMemory) {
        return new StartCommand("/start", "", chatMemory);
    }

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(
            TelegramProps props,
            TelegramBotService botService
    ) {
        TelegramBotsLongPollingApplication pollingApplication = new TelegramBotsLongPollingApplication();
        try {
            String cleanToken = eliminateNonAlphaNumericSymbols(props.getToken());
            pollingApplication.registerBot(cleanToken, botService);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return pollingApplication;
    }
}
