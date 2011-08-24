package org.glass.job.util;

import org.glass.SpringConfig;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

/**
 * Small spring utility methods for Job.
 *
 * @author damien bourdette
 */
public class Spring {
    public static <T> T getBean(JobExecutionContext context, Class<T> beanClass) throws JobExecutionException {
        return getApplicationContext(context).getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext(JobExecutionContext context) throws JobExecutionException {
        try {
            return getApplicationContext(context.getScheduler().getContext());
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
