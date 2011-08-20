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

import org.glass.annotation.JobArgument;
import org.glass.job.JobUtils;
import org.glass.log.Log;
import org.glass.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@org.glass.annotation.Job(description = "Test job for value conversions")
public class TypesJob implements Job {
    @JobArgument(description = "test for long value")
    private Long longValue;

    @JobArgument(description = "test for date value")
    private Date dateValue;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logs logs = JobUtils.getSpringBean(context, Logs.class);

        logs.add(Log.info("longValue = " + longValue));
        logs.add(Log.info("dateValue = " + dateValue));
    }
}
