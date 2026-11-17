package dev.springai.telegram.bot.conf.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor

@ConfigurationProperties(prefix = "telegram")
@Getter
public class TelegramProps {
    private final String token;
    private final String botName;
}
