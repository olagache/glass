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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.glass.job.util.JobDataMapUtils;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author damien bourdette
 */
public class HistoryLog {
    private Date date;

    private HistoryLogType type;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String jobClass;

    private String dataMap;

    private String stackTrace;

    public static HistoryLog onStart(JobExecutionContext context) {
        HistoryLog log = onEvent(context);

        log.type = HistoryLogType.START;
        log.date = context.getFireTime();

        return log;
    }

    public static HistoryLog onEnd(JobExecutionContext context, JobExecutionException exception) {
        HistoryLog log = onEvent(context);

        log.type = HistoryLogType.END;
        log.date = new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate();

        if (exception != null) {
            log.stackTrace = ExceptionUtils.getFullStackTrace(exception);
        }

        return log;
    }

    public HistoryLog() {

    }

    public Date getDate() {
        return date;
    }

    public String getJobClass() {
        return jobClass;
    }

    public HistoryLogType getType() {
        return type;
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

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "HistoryLog{" +
                "date=" + date +
                ", type=" + type +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobName='" + jobName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", dataMap='" + dataMap + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    /**
     * Fill common attributes
     */
    private static HistoryLog onEvent(JobExecutionContext context) {
        HistoryLog log = new HistoryLog();

        log.jobClass = context.getJobDetail().getJobClass().getName();
        log.jobGroup = context.getJobDetail().getKey().getGroup();
        log.jobName = context.getJobDetail().getKey().getName();
        log.triggerGroup = context.getTrigger().getKey().getGroup();
        log.triggerName = context.getTrigger().getKey().getName();
        log.dataMap = JobDataMapUtils.toProperties(context.getMergedJobDataMap(), "\n");

        return log;
    }
}
