package com.github.dbourdette.glass.job.util;

import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import com.github.dbourdette.glass.SpringConfig;

/**
 * Utility class to get references to spring beans.
 *
 * @author damien bourdette
 */
public class Spring {
    public static <T> T bean(Class<T> beanClass) throws JobExecutionException {
        return getApplicationContext().getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext() throws JobExecutionException {
        try {
            return getApplicationContext(CurrentJobExecutionContext.get().getScheduler().getContext());
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to get scheduler from JobExecutionContext");
        }
    }

    public static ApplicationContext getApplicationContext(SchedulerContext schedulerContext) throws JobExecutionException {
        ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(SpringConfig.APPLICATION_CONTEXT_KEY);

        if (applicationContext == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \"" + SpringConfig.APPLICATION_CONTEXT_KEY + "\"");
        }

        return applicationContext;
    }
}
