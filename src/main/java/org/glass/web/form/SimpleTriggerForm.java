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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.glass.job.util.JobDataMapUtils;
import org.joda.time.DateTime;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author damien bourdette
 */
public class SimpleTriggerForm extends TriggerFormSupport implements TriggerForm {
    @Min(-1)
    protected Integer repeatCount;

    @NotNull
    @Min(0)
    protected Integer intervalInMilliseconds;

    public SimpleTriggerForm() {
    }

    public SimpleTriggerForm(Trigger trigger) {
        this.startTime = trigger.getStartTime();
        this.endTime = trigger.getEndTime();
        this.dataMap = JobDataMapUtils.toProperties(trigger.getJobDataMap(), "\n");
        this.repeatCount = ((SimpleTrigger) trigger).getRepeatCount();
        this.intervalInMilliseconds = (int) ((SimpleTrigger) trigger).getRepeatInterval();
    }

    public Trigger getTrigger(Trigger trigger) throws ParseException {
        fixParameters();

        TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger().forJob(trigger.getJobKey().getName(), trigger.getJobKey().getGroup())
                .withIdentity(trigger.getKey().getName(), trigger.getKey().getGroup())
                .startAt(startTime).endAt(endTime)
                .usingJobData(JobDataMapUtils.fromProperties(dataMap));

        if (repeatCount == -1) {
            builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever()
                    .withIntervalInMilliseconds(intervalInMilliseconds));
        } else {
            builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
                    .withIntervalInMilliseconds(intervalInMilliseconds));
        }

        return builder.build();
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Integer getIntervalInMilliseconds() {
        return intervalInMilliseconds;
    }

    public void setIntervalInMilliseconds(Integer intervalInMilliseconds) {
        this.intervalInMilliseconds = intervalInMilliseconds;
    }

    protected void fixParameters() {
        if (repeatCount == null) {
            repeatCount = 0;
        }

        if (intervalInMilliseconds == null) {
            intervalInMilliseconds = 0;
        }

        if (startTime == null) {
            startTime = new DateTime().plusSeconds(1).toDate();
        }
    }
}
