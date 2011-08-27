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

package org.glass.history;

import java.util.Date;

import org.glass.job.util.JobDataMapUtils;
import org.glass.util.Dates;
import org.quartz.JobExecutionContext;

/**
 * Summary of a job execution stored as a log.
 *
 * @author damien bourdette
 */
public class ExcecutionLog {
    private Long id;

    private Date startDate;

    private Date endDate;

    private boolean ended;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String jobClass;

    private String dataMap;

    private boolean success;

    private ExcecutionLog() {

    }

    /**
     * Fill common attributes
     */
    public static ExcecutionLog fromContext(JobExecutionContext context) {
        ExcecutionLog log = new ExcecutionLog();

        log.startDate = context.getFireTime();
        log.jobClass = context.getJobDetail().getJobClass().getName();
        log.jobGroup = context.getJobDetail().getKey().getGroup();
        log.jobName = context.getJobDetail().getKey().getName();
        log.triggerGroup = context.getTrigger().getKey().getGroup();
        log.triggerName = context.getTrigger().getKey().getName();
        log.dataMap = JobDataMapUtils.toProperties(context.getMergedJobDataMap(), "\n");

        return log;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void setStartDate(Date startDate) {
        this.startDate = Dates.copy(startDate);
    }

    public Date getStartDate() {
        return Dates.copy(startDate);
    }

    public Date getEndDate() {
        return Dates.copy(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = Dates.copy(endDate);
    }

    public String getJobClass() {
        return jobClass;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
