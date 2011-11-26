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

package com.github.dbourdette.glass.log.joblog;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.github.dbourdette.glass.log.execution.JobExecution;
import com.github.dbourdette.glass.tools.FormatTool;
import com.github.dbourdette.glass.util.Dates;

/**
 * @author damien bourdette
 */
public class JobLog {
    private Long executionId;

    private JobLogLevel level;

    private Date date;

    private String jobClass;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String message;

    private String stackTrace;

    private String rootCause;

    public JobLog() {

    }

    public static JobLog message(JobExecution execution, JobLogLevel level, String message) {
        JobLog jobLog = new JobLog();

        if (execution != null) {
            jobLog.executionId = execution.getId();
            jobLog.jobClass = execution.getJobClass();
            jobLog.jobName = execution.getJobName();
            jobLog.jobGroup = execution.getJobGroup();
            jobLog.triggerGroup = execution.getTriggerGroup();
            jobLog.triggerName = execution.getTriggerName();
        }

        jobLog.date = new Date();
        jobLog.level = level;
        jobLog.message = message;

        return jobLog;
    }

    public static JobLog exception(JobExecution execution, JobLogLevel level, String message, Throwable e) {
        JobLog jobLog = message(execution, level, message);

        jobLog.stackTrace = ExceptionUtils.getFullStackTrace(e);
        jobLog.rootCause = ExceptionUtils.getMessage(ExceptionUtils.getRootCause(e));

        if (StringUtils.isEmpty(jobLog.rootCause)) {
            jobLog.rootCause = ExceptionUtils.getMessage(e);
        }

        if (StringUtils.isEmpty(jobLog.rootCause)) {
            jobLog.rootCause = "no message";
        }

        return jobLog;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public JobLogLevel getLevel() {
        return level;
    }

    public Date getDate() {
        return Dates.copy(date);
    }

    public String getFormattedDate() {
        return FormatTool.formatDate(date);
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getFormattedStackTrace() {
        String html = StringEscapeUtils.escapeHtml(stackTrace);

        html = StringUtils.replace(html, "\n", "<br/>");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;");

        return html;
    }

    public String getRootCause() {
        return rootCause;
    }

    public String getJobClass() {
        return jobClass;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public void setLevel(JobLogLevel level) {
        this.level = level;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }
}
