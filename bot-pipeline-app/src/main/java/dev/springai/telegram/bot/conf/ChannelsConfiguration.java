package dev.springai.telegram.bot.conf;

import dev.springai.telegram.pipeline.core.channel.AbstractMessageChannelsConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.Executor;

@Configuration
@EnableIntegration
@IntegrationComponentScan("dev.springai.telegram.bot.gateway")
public class ChannelsConfiguration extends AbstractMessageChannelsConfigurer {
    public static final String PRFX_THREADS_CHAT_CLIENT = "cc-vi-";
    public static final String TH_EXEC_VI_CHAT_CLIENT = "th.exec.vi.chat_client";
    public static final String CHSA_TG_BOT_TO_CHAT_CLIENT = "chs.async.telegram_bot.chat_client";
    public static final String CHS_CHAT_CLIENT_TO_TG_BOT = "chs.chat_client.telegram_bot";

    @Bean(TH_EXEC_VI_CHAT_CLIENT)
    public Executor chatClientVirtualExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor(PRFX_THREADS_CHAT_CLIENT);
        executor.setVirtualThreads(true);
        return executor;
    }

    @Bean(CHSA_TG_BOT_TO_CHAT_CLIENT)
    public SubscribableChannel getAsyncTelegramBotToChatClientChannel(
         @Qualifier(TH_EXEC_VI_CHAT_CLIENT) Executor executor
    ) {
       return createSubscribableChannel(executor,String.class);
    }

    @Bean(CHS_CHAT_CLIENT_TO_TG_BOT)
    public SubscribableChannel getChatClientChannelToTelegramBot() {
        return createSubscribableChannel(String.class);
    }
}
