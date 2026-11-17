package dev.springai.telegram.pipeline.core.channel;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;
import java.util.concurrent.Executor;

public abstract class AbstractMessageChannelsConfigurer {
    protected DirectChannel createDirectChannel(Class<?>... clazz) {
        DirectChannel channel = new DirectChannel();
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }

    protected DirectChannel createDirectChannel(List<ChannelInterceptor> interceptors, Class<?>... clazz) {
        DirectChannel channel = createDirectChannel(clazz);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (int i = 0; i < interceptors.size(); i++) {
                channel.addInterceptor(interceptors.get(i));
            }
            channel.setInterceptors(interceptors);
        }
        return channel;
    }

    protected SubscribableChannel createSubscribableChannel(Class<?>... clazz) {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }

    protected SubscribableChannel createSubscribableChannel(Executor executor, Class<?>... clazz) {
        PublishSubscribeChannel channel = new PublishSubscribeChannel(executor);
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }

    protected SubscribableChannel createSubscribableChannel(List<ChannelInterceptor> interceptors, Class<?>... clazz) {
        PublishSubscribeChannel channel = new PublishSubscribeChannel();
        if (interceptors != null && !interceptors.isEmpty()) {
            channel.setInterceptors(interceptors);
        }
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }

    protected SubscribableChannel createSubscribableChannel(Executor executor, List<ChannelInterceptor> interceptors, Class<?>... clazz) {
        PublishSubscribeChannel channel = new PublishSubscribeChannel(executor);
        if (interceptors != null && !interceptors.isEmpty()) {
            channel.setInterceptors(interceptors);
        }
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }


    protected PollableChannel createPollableChannel(Integer capacity, Class<?>... clazz) {
        QueueChannel channel;
        channel = capacity != null ? new QueueChannel(capacity) : new QueueChannel();
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }

    protected PollableChannel createPollableChannel(Class<?>... clazz) {
        QueueChannel channel;
        channel = new QueueChannel();
        if (clazz != null) {
            channel.setDatatypes(clazz);
        }
        return channel;
    }
}
