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

package org.glass.history;

import javax.inject.Inject;

import org.glass.log.Logs;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author damien bourdette
 */
@Component
public class QuartzListenerForHistory extends JobListenerSupport {
    @Inject
    private History history;

    @Inject
    private Logs logs;

    @Override
    public String getName() {
        return QuartzListenerForHistory.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        ExecutionLog log = history.jobStarts(context);

        log.setInContext(context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        ExecutionLog log = ExecutionLog.getFromContext(context);

        history.jobEnds(log, context, exception);

        if (exception != null) {
            logs.error("Exception occurred while executing job " + context.getJobDetail().getClass().getName(), exception);
        }
    }
}
