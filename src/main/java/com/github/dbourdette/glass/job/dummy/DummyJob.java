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

package com.github.dbourdette.glass.job.dummy;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.github.dbourdette.glass.job.annotation.Job;
import com.github.dbourdette.glass.job.annotation.JobArgument;
import com.github.dbourdette.glass.log.joblog.JobLogLevel;
import com.github.dbourdette.glass.log.joblog.JobLogs;

/**
 * A dummy quartz job for testing purposes.
 */
@Job(description = "Dummy job for testing purposes", logLevel = JobLogLevel.INFO)
@DisallowConcurrentExecution
public class DummyJob implements InterruptableJob {

    @JobArgument(required = true, description = "Duration of the job, in seconds. Default is 10 seconds", sampleValues = "10, 60")
    private Long duration = 10l;

    private Thread runningThread;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        runningThread = Thread.currentThread();

        if (duration < 2) {
            JobLogs.error("Running dummy job for {} seconds", duration);
        } else if (duration < 4) {
            JobLogs.warn("Running dummy job for {} seconds", duration);
        } else {
            JobLogs.info("Running dummy job for {} seconds", duration);
        }

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
