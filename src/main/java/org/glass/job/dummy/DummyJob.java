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

package org.glass.job.dummy;

import org.glass.annotation.Job;
import org.glass.annotation.JobArgument;
import org.glass.job.JobUtils;
import org.glass.log.Log;
import org.glass.log.Logs;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import javax.inject.Inject;

/**
 * A dummy quartz job for testing purposes.
 */
@Job(description = "Dummy job for testing purposes")
@DisallowConcurrentExecution
public class DummyJob implements InterruptableJob {

    @JobArgument(name = "duration", required = true, description = "Duration of the job, in seconds. Default is 10 seconds", sampleValues = "10, 60")
    private Long duration = 10l;

    private Thread runningThread;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        runningThread = Thread.currentThread();

        JobUtils.getSpringBean(context, Logs.class).add(Log.info("Running dummy job for " + duration + " seconds"));

        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            throw new JobExecutionException(e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        runningThread.interrupt();
    }
}
