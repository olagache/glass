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
import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.glass.SpringConfig;
import org.glass.job.JobUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author damien bourdette
 */
public class NewSimpleTriggerForm {
    private String group;

    private String name;

    @NotEmpty
    private String triggerGroup;

    @NotEmpty
    private String triggerName;

    @DateTimeFormat(pattern = SpringConfig.DATE_FORMAT)
    @Future
    private Date startTime;

    @DateTimeFormat(pattern = SpringConfig.DATE_FORMAT)
    private Date endTime;

    @Min(-1)
    private Integer repeatCount;

    @NotNull
    @Min(0)
    private Integer intervalInMilliseconds = 1000;

    private String dataMap;

    public NewSimpleTriggerForm() {
    }

    public NewSimpleTriggerForm(JobDetail job) {
        this.group = job.getKey().getGroup();
        this.name = job.getKey().getName();
        this.triggerGroup = job.getKey().getGroup();
        this.triggerName = job.getKey().getName() + " trigger";
    }

    public Trigger getTrigger() throws ParseException {
        if (repeatCount == null) {
            repeatCount = 0;
        }

        if (intervalInMilliseconds == null) {
            intervalInMilliseconds = 1;
        }

        if (startTime == null) {
            startTime = new DateTime().plusSeconds(1).toDate();
        }

        TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger().forJob(name.trim(), group.trim()).withIdentity(triggerName.trim(), triggerGroup.trim())
                .startAt(startTime).endAt(endTime)
                .usingJobData(JobUtils.fromProperties(dataMap));

        if (repeatCount == -1) {
            builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever()
                    .withIntervalInMilliseconds(intervalInMilliseconds));
        } else {
            builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)
                    .withIntervalInMilliseconds(intervalInMilliseconds));
        }

        return builder.build();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }
}
