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

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

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
        traces.setContext(context);

        Execution execution = executions.jobStarts(context);

        execution.setInContext(context);

        final Job job = context.getJobInstance();

        ReflectionUtils.doWithFields(job.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (field.getType().isAssignableFrom(Traces.class)) {
                    ReflectionUtils.makeAccessible(field);

                    ReflectionUtils.setField(field, job, traces);
                }
            }
        });
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        Execution execution = Execution.getFromContext(context);

        if (exception != null) {
            execution.setSuccess(false);

            traces.error("Exception occurred while executing job " + context.getJobDetail().getClass().getName(), exception);
        }

        executions.jobEnds(execution, context);

        traces.setContext(null);
    }
}
