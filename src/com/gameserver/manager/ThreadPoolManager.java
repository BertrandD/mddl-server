package com.gameserver.manager;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author LEBOC Philippe
 */
public class ThreadPoolManager implements TaskScheduler {

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    protected ThreadPoolManager(){
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return threadPoolTaskScheduler.schedule(task, trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return threadPoolTaskScheduler.schedule(task, startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        return threadPoolTaskScheduler.scheduleAtFixedRate(task, startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return threadPoolTaskScheduler.scheduleAtFixedRate(task, period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        return threadPoolTaskScheduler.scheduleWithFixedDelay(task, startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return threadPoolTaskScheduler.scheduleWithFixedDelay(task, delay);
    }

    public static ThreadPoolManager getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final ThreadPoolManager _instance = new ThreadPoolManager();
    }
}
