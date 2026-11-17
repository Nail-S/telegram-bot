package dev.springai.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import static dev.springai.telegram.bot.conf.ChannelsConfiguration.CHSA_TG_BOT_TO_CHAT_CLIENT;
import static dev.springai.telegram.bot.conf.ChannelsConfiguration.CHS_CHAT_CLIENT_TO_TG_BOT;
import static dev.springai.telegram.bot.consts.HeaderNames.HEADER_CHAT_ID;

@Slf4j
@RequiredArgsConstructor
@MessageEndpoint
public class ChatClientService {
    private static final String SYSTEM_PROMPT = """
            Ты приятный собеседник и готов поддержать разговор на любые темы, 
            не нарушающие этические нормы. Если вопросы выходят за рамки этических норм отвечай пожалуйста:
            "Я не готов обсуждать предложенную тему" 
        """.stripIndent();
    private final ChatClient chatClient;

    @ServiceActivator(
            inputChannel = CHSA_TG_BOT_TO_CHAT_CLIENT,
            outputChannel = CHS_CHAT_CLIENT_TO_TG_BOT
    )
    public String processUserMessage(
            @Payload String userMessage,
            @Header(HEADER_CHAT_ID) Long chatId) {
        log.info("ChatId {} sent a message {}", chatId, userMessage);
        String response = chatClient
                        .prompt()
                        .system(SYSTEM_PROMPT)
                        .advisors(
                                a -> a.param(
                                        ChatMemory.CONVERSATION_ID, chatId.toString()
                                )
                        ).user(u -> u.text(userMessage))
                        .call()
                        .content();
        log.info("Received response from LLM: {}", response);
        return response;
    }
}
