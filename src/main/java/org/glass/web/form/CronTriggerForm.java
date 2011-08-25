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

package org.glass.web.form;

import java.text.ParseException;

import org.glass.job.util.JobDataMapUtils;
import org.glass.util.Dates;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author damien bourdette
 */
public class CronTriggerForm extends TriggerFormSupport implements TriggerForm {
    @NotEmpty
    protected String cronExpression;

    public CronTriggerForm() {
    }

    public CronTriggerForm(Trigger trigger) {
        this.startTime = Dates.copy(trigger.getStartTime());
        this.endTime = Dates.copy(trigger.getEndTime());
        this.dataMap = JobDataMapUtils.toProperties(trigger.getJobDataMap(), "\n");
        this.cronExpression = ((CronTrigger) trigger).getCronExpression();

    }

    public Trigger getTrigger(Trigger trigger) throws ParseException {
        fixParameters();

        return TriggerBuilder.newTrigger().forJob(trigger.getJobKey().getName(), trigger.getJobKey().getGroup())
                .withIdentity(trigger.getKey().getName(), trigger.getKey().getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionIgnoreMisfires())
                .startAt(startTime).endAt(endTime)
                .usingJobData(JobDataMapUtils.fromProperties(dataMap))
                .build();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    protected void fixParameters() {
        if (startTime == null) {
            startTime = new DateTime().plusSeconds(1).toDate();
        }
    }
}
