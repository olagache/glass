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

package com.github.dbourdette.glass.log.trace;

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
public class Trace {
    private Long executionId;

    private TraceLevel level;

    private Date date;

    private String jobClass;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String message;

    private String stackTrace;

    private String rootCause;

    public Trace() {

    }

    public static Trace message(JobExecution execution, TraceLevel level, String message) {
        Trace trace = new Trace();

        if (execution != null) {
            trace.executionId = execution.getId();
            trace.jobClass = execution.getJobClass();
            trace.jobName = execution.getJobName();
            trace.jobGroup = execution.getJobGroup();
            trace.triggerGroup = execution.getTriggerGroup();
            trace.triggerName = execution.getTriggerName();
        }

        trace.date = new Date();
        trace.level = level;
        trace.message = message;

        return trace;
    }

    public static Trace exception(JobExecution execution, TraceLevel level, String message, Throwable e) {
        Trace trace = message(execution, level, message);

        trace.stackTrace = ExceptionUtils.getFullStackTrace(e);
        trace.rootCause = ExceptionUtils.getMessage(ExceptionUtils.getRootCause(e));

        if (StringUtils.isEmpty(trace.rootCause)) {
            trace.rootCause = ExceptionUtils.getMessage(e);
        }

        if (StringUtils.isEmpty(trace.rootCause)) {
            trace.rootCause = "no message";
        }

        return trace;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public TraceLevel getLevel() {
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

    public void setLevel(TraceLevel level) {
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
