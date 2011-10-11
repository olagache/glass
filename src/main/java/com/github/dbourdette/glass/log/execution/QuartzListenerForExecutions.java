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

package com.github.dbourdette.glass.log.execution;

import java.util.List;

import javax.inject.Inject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

import com.github.dbourdette.glass.log.log.Log;
import com.github.dbourdette.glass.log.log.LogLevel;
import com.github.dbourdette.glass.log.Logs;

/**
 * @author damien bourdette
 */
@Component
public class QuartzListenerForExecutions extends JobListenerSupport {
    @Inject
    private Executions executions;

    @Inject
    private Logs logs;

    @Override
    public String getName() {
        return QuartzListenerForExecutions.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        Execution log = executions.jobStarts(context);

        log.setInContext(context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        Execution log = Execution.getFromContext(context);

        if (exception != null) {
            log.setSuccess(false);
        }

        executions.jobEnds(log, context);

        if (exception != null) {
            logs.error("Exception occurred while executing job " + context.getJobDetail().getClass().getName(), exception);
        }
    }

    private boolean success(Execution executionLog) {
        List<Log> logs = this.logs.getLogs(executionLog.getId());

        for (Log log : logs) {
            if (log.getLevel().ordinal() >= LogLevel.WARN.ordinal()) {
               return false;
            }
        }

        return true;
    }
}
