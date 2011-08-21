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

import org.apache.commons.lang.StringUtils;
import org.glass.job.TriggerUtils;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

import java.util.Date;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class UtilsTool {
    public boolean isInterruptible(JobDetail job) {
        return InterruptableJob.class.isAssignableFrom(job.getJobClass());
    }

    public String duration(JobExecutionContext context) {
        return ((new Date().getTime() -  context.getFireTime().getTime()) / 1000) + " secondes";
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
}
