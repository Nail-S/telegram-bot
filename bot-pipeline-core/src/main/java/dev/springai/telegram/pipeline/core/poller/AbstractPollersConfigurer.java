package dev.springai.telegram.pipeline.core.poller;

import org.aopalliance.aop.Advice;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transaction.TransactionSynchronizationFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class AbstractPollersConfigurer {
    protected PeriodicTrigger getFixedDelayTrigger(long period, TemporalUnit unit) {
        var trigger = new PeriodicTrigger(Duration.of(period, unit));
        trigger.setFixedRate(false);
        return trigger;
    }

    protected CronTrigger getCronTrigger(String cronExpression, String timeZone) {
        return new CronTrigger(cronExpression, ZoneId.of(timeZone));
    }

    protected PollerMetadata getFixedDelayPollerMetadata(long period, TemporalUnit unit) {
        var metadata = new PollerMetadata();
        metadata.setTrigger(getFixedDelayTrigger(period, unit));
        return metadata;
    }

    protected PollerMetadata getFixedDelayPollerMetadata(
            long period,
            TemporalUnit unit,
            int maxMessagesPerPoll
    ) {
        var metadata = getFixedDelayPollerMetadata(period, unit);
        metadata.setMaxMessagesPerPoll(maxMessagesPerPoll);
        return metadata;
    }

    protected PollerMetadata getFixedDelayPollerMetadata(
            long period,
            TemporalUnit unit,
            List<Advice> adviceChain) {
        var metadata = getFixedDelayPollerMetadata(period, unit);
        metadata.setAdviceChain(adviceChain);
        return metadata;
    }

    protected PollerMetadata getFixedDelayPollerMetadata(
            long period,
            TemporalUnit unit,
            int maxMessagesPerPoll,
            List<Advice> adviceChain) {
        var metadata = getFixedDelayPollerMetadata(period, unit, maxMessagesPerPoll);
        metadata.setAdviceChain(adviceChain);
        return metadata;
    }

    protected PollerMetadata getFixedDelayPollerMetadata(
            long period,
            TemporalUnit unit,
            int maxMessagePerPoll,
            TaskExecutor taskExecutor,
            List<Advice> adviceChain,
            TransactionSynchronizationFactory synchronizationFactory) {
        var metadata = getFixedDelayPollerMetadata(
                period,
                unit,
                adviceChain
        );
        metadata.setMaxMessagesPerPoll(maxMessagePerPoll);
        metadata.setTaskExecutor(taskExecutor);
        metadata.setTransactionSynchronizationFactory(synchronizationFactory);
        return metadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(String cronExpression, String timeZone) {
        var metadata = new PollerMetadata();
        metadata.setTrigger(getCronTrigger(cronExpression, timeZone));
        return metadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(
            String cronExpression,
            String timeZone,
            List<Advice> adviceChain
    ) {
        var metadata = getSchedulledPollerMetadata(cronExpression, timeZone);
        metadata.setAdviceChain(adviceChain);
        return metadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(
            String cronExpression,
            String timeZone,
            Long receiveTimeoutMs,
            List<Advice> adviceChain
    ) {
        var metadata = getSchedulledPollerMetadata(cronExpression, timeZone, adviceChain);
        metadata.setReceiveTimeout(receiveTimeoutMs);
        return metadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(
            String cronExpression,
            String timeZone,
            int maxMessagePerPoll
    ) {
        var metadata = getSchedulledPollerMetadata(cronExpression, timeZone);
        metadata.setMaxMessagesPerPoll(maxMessagePerPoll);
        return metadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(
            String cronExpression,
            String timeZone,
            int maxMessagePerPoll,
            List<Advice> adviceChain
    ) {
        PollerMetadata pollerMetadata = getSchedulledPollerMetadata(
                cronExpression,
                timeZone,
                maxMessagePerPoll
        );
        pollerMetadata.setAdviceChain(adviceChain);
        return pollerMetadata;
    }

    protected PollerMetadata getSchedulledPollerMetadata(
            String cronExpression,
            String timeZone,
            int maxMessagePerPoll,
            TaskExecutor taskExecutor,
            List<Advice> adviceChain,
            TransactionSynchronizationFactory synchronizationFactory
    ) {
        PollerMetadata pollerMetadata = getSchedulledPollerMetadata(
                cronExpression,
                timeZone,
                maxMessagePerPoll,
                adviceChain
        );
        pollerMetadata.setTaskExecutor(taskExecutor);
        pollerMetadata.setTransactionSynchronizationFactory(synchronizationFactory);
        return pollerMetadata;
    }
}
