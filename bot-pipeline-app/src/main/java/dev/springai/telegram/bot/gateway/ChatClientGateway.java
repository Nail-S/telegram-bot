package dev.springai.telegram.bot.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import static dev.springai.telegram.bot.conf.ChannelsConfiguration.CHSA_TG_BOT_TO_CHAT_CLIENT;
import static dev.springai.telegram.bot.conf.ChannelsConfiguration.CHS_CHAT_CLIENT_TO_TG_BOT;
import static dev.springai.telegram.bot.consts.HeaderNames.HEADER_CHAT_ID;

@MessagingGateway
public interface ChatClientGateway {
    @Gateway(
            requestChannel = CHSA_TG_BOT_TO_CHAT_CLIENT,
            replyChannel = CHS_CHAT_CLIENT_TO_TG_BOT
    )
    String processUserMessage(@Payload String userMessage, @Header(HEADER_CHAT_ID) Long chatId);
}
