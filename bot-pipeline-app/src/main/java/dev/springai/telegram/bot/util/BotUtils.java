package dev.springai.telegram.bot.util;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
public class BotUtils {
    public static SendMessage createMessage(long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .disableWebPagePreview(true)
                .build();
    }

    public static String eliminateNonAlphaNumericSymbols(String source) {
        if(source == null) {
            log.info("The source string is null");
            return "";
        }
        if (source.isEmpty()) {
            log.info("The source string is empty");
            return "";
        }
        if (source.isEmpty() || source.isBlank()) {
            log.info("The source string is empty");
            return "";
        }
        String cleanToken = source.trim()
                .replaceAll("^\"|\"$", "")     // Remove possible leading/trailing double quotes
                .replaceAll("^'|'$", "");      // Remove possible leading/trailing single quotes
        log.info("Source string length: {}", cleanToken.length());
        log.info("Source string last char code: {}", (int) cleanToken.charAt(cleanToken.length() - 1));
        return cleanToken;
    }
}
