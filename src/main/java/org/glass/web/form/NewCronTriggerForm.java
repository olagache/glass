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

import org.glass.job.JobUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class NewCronTriggerForm {
    private String group;

    private String name;

    @NotEmpty
    private String triggerGroup;

    @NotEmpty
    private String triggerName;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Future
    private Date startTime;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date endTime;

    @NotEmpty
    private String cronExpression;

    private String dataMap;

    public NewCronTriggerForm() {
    }

    public NewCronTriggerForm(JobDetail job) {
        this.group = job.getKey().getGroup();
        this.name = job.getKey().getName();
        this.triggerGroup = job.getKey().getGroup();
        this.triggerName = job.getKey().getName() + " trigger";
        this.startTime = new DateTime().plusMinutes(1).toDate();
    }

    public Trigger getTrigger() throws ParseException {
        return TriggerBuilder.newTrigger().forJob(name.trim(), group.trim()).withIdentity(triggerName.trim(), triggerGroup.trim())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionIgnoreMisfires())
                .startAt(startTime).endAt(endTime)
                .usingJobData(JobUtils.fromProperties(dataMap))
                .build();
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }
}