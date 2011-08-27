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

package org.glass.job.types;

import java.util.Date;

import org.glass.job.annotation.JobArgument;
import org.glass.job.util.Spring;
import org.glass.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author damien bourdette
 */
@org.glass.job.annotation.Job(description = "Test job for value conversions")
public class TypesJob implements Job {
    @JobArgument(description = "test for long value")
    private Long longValue;

    @JobArgument(description = "test for date value")
    private Date dateValue;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logs logs = Spring.getBean(context, Logs.class);

        logs.info("longValue = {}", longValue);
        logs.info("dateValue = {}", dateValue);
    }
}
