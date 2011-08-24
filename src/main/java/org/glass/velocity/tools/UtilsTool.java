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

package org.glass.velocity.tools;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.glass.job.util.TriggerUtils;
import org.joda.time.Period;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

/**
 * @author damien bourdette
 */
public class UtilsTool {
    public boolean isInterruptible(JobDetail job) {
        return InterruptableJob.class.isAssignableFrom(job.getJobClass());
    }

    public String duration(JobExecutionContext context) {
        return duration(context.getFireTime(), new Date());
    }

    public String duration(Date start, Date end) {
        Period period = new Period(start.getTime(), end.getTime());

        StringBuilder builder = new StringBuilder();

        appendDuration(builder, period.getDays(), "d");
        appendDuration(builder, period.getHours(), "h");
        appendDuration(builder, period.getMinutes(), "m");
        appendDuration(builder, period.getSeconds(), "s");

        return builder.toString().trim();
    }

    public String planification(Trigger trigger) {
        return TriggerUtils.getPlanification(trigger);
    }

    public boolean isEmpty(String string) {
        return StringUtils.isEmpty(string);
    }

    public boolean isNotEmpty(String string) {
        return StringUtils.isNotEmpty(string);
    }

    public void appendDuration(StringBuilder builder, int value, String unit) {
        if (value != 0) {
            builder.append(value + unit + " ");
        }
    }
}
