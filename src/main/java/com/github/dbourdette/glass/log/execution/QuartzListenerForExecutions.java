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

import javax.inject.Inject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

import com.github.dbourdette.glass.log.trace.Traces;

/**
 * @author damien bourdette
 */
@Component
public class QuartzListenerForExecutions extends JobListenerSupport {
    @Inject
    private Executions executions;

    @Inject
    private Traces traces;

    @Override
    public String getName() {
        return QuartzListenerForExecutions.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        Execution execution = executions.jobStarts(context);

        execution.setInContext(context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        Execution execution = Execution.getFromContext(context);

        if (exception != null) {
            execution.setSuccess(false);

            traces.error("Exception occurred while executing job " + context.getJobDetail().getClass().getName(), exception);
        }

        executions.jobEnds(execution, context);
    }
}
