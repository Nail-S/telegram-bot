package dev.springai.telegram.bot.conf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfiguration {

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository repository) {
        return  MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(100)
                .build();
    }

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            ChatMemory chatMemory,
            VectorStore vectorStore
    ) {
        return builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
        //                QuestionAnswerAdvisor.builder(vectorStore).build(),
                        SimpleLoggerAdvisor.builder().build()
                ).build();
    }
}
