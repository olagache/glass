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

package com.github.dbourdette.glass.log.execution;

import java.util.Date;

import org.quartz.JobExecutionContext;

import com.github.dbourdette.glass.job.util.JobDataMapUtils;
import com.github.dbourdette.glass.util.Dates;

/**
 * Summary of a job execution stored as a log.
 *
 * @author damien bourdette
 */
public class Execution {
    private static final String KEY_IN_CONTEXT = "__GLASS_JOB_EXECUTION_CONTEXT";

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

    private ExecutionResult result = ExecutionResult.SUCCESS;

    /**
     * Gets currently Execution stored in context's data map.
     */
    public static Execution getFromContext(JobExecutionContext context) {
        return (Execution) context.get(KEY_IN_CONTEXT);
    }

    public Execution() {

    }

    /**
     * Fill common attributes with properties from context.
     */
    public void fillWithContext(JobExecutionContext context) {
        startDate = context.getFireTime();
        jobClass = context.getJobDetail().getJobClass().getName();
        jobGroup = context.getJobDetail().getKey().getGroup();
        jobName = context.getJobDetail().getKey().getName();
        triggerGroup = context.getTrigger().getKey().getGroup();
        triggerName = context.getTrigger().getKey().getName();
        dataMap = JobDataMapUtils.toProperties(context.getMergedJobDataMap(), "\n");
    }

    /**
     * Sets this Execution in context as a value in its data map.
     */
    public void setInContext(JobExecutionContext context) {
        context.put(KEY_IN_CONTEXT, this);
    }

    public void warn() {
        if (this.result == ExecutionResult.ERROR) {
            return;
        }

        result = ExecutionResult.WARN;
    }

    public void error() {
        result = ExecutionResult.ERROR;
    }

    public ExecutionResult getResult() {
        return result;
    }

    public void setResult(ExecutionResult result) {
        if (result == null) {
            result = ExecutionResult.SUCCESS;
        }

        this.result = result;
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

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
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

    @Override
    public String toString() {
        return "Execution{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", ended=" + ended +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobName='" + jobName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", dataMap='" + dataMap + '\'' +
                ", result=" + result +
                '}';
    }
}
