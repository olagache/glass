/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.glass.job;

import javax.inject.Inject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

import com.github.dbourdette.glass.job.annotation.JobArgumentBean;
import com.github.dbourdette.glass.job.util.CurrentJobExecutionContext;
import com.github.dbourdette.glass.log.execution.CurrentJobExecution;
import com.github.dbourdette.glass.log.execution.JobExecution;
import com.github.dbourdette.glass.log.execution.JobExecutions;
import com.github.dbourdette.glass.log.joblog.JobLogs;

/**
 * @author damien bourdette
 */
@Component
public class GlassJobListener extends JobListenerSupport {
    @Inject
    private JobExecutions executions;

    @Override
    public String getName() {
        return GlassJobListener.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        CurrentJobExecutionContext.set(context);

        JobExecution execution = executions.jobStarts(context);

        CurrentJobExecution.set(execution);
        JobLogs.setLevel(getLogLevelFromContext(context));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        JobExecution execution = CurrentJobExecution.get();

        if (exception != null) {
            execution.error();

            JobLogs.error("Exception occurred while executing job " + context.getJobDetail().getClass().getName(), exception);
        }

        executions.jobEnds(execution, context);

        JobLogs.setDefaultLevel();
        CurrentJobExecution.unset();

        CurrentJobExecutionContext.unset();
    }

    private String getLogLevelFromContext(JobExecutionContext context) {
        return context.getMergedJobDataMap().getString(JobArgumentBean.LOG_LEVEL_ARGUMENT);
    }
}
