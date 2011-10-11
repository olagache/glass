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

package com.github.dbourdette.glass.log.log;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.github.dbourdette.glass.log.execution.Execution;
import com.github.dbourdette.glass.tools.FormatTool;
import com.github.dbourdette.glass.util.Dates;

/**
 * @author damien bourdette
 */
public class Log {
    private Long executionId;

    private LogLevel level;

    private Date date;

    private String jobClass;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String message;

    private String stackTrace;

    private String rootCause;

    public Log() {

    }

    public static Log message(Execution executionLog, LogLevel level, String message) {
        Log log = new Log();

        if (executionLog != null) {
            log.executionId = executionLog.getId();
            log.jobClass = executionLog.getJobClass();
            log.jobName = executionLog.getJobName();
            log.jobGroup = executionLog.getJobGroup();
            log.triggerGroup = executionLog.getTriggerGroup();
            log.triggerName = executionLog.getTriggerName();
        }

        log.date = new Date();
        log.level = level;
        log.message = message;

        return log;
    }

    public static Log exception(Execution executionLog, LogLevel level, String message, Throwable e) {
        Log log = message(executionLog, level, message);

        log.stackTrace = ExceptionUtils.getFullStackTrace(e);
        log.rootCause = ExceptionUtils.getMessage(ExceptionUtils.getRootCause(e));

        if (StringUtils.isEmpty(log.rootCause)) {
            log.rootCause = ExceptionUtils.getMessage(e);
        }

        if (StringUtils.isEmpty(log.rootCause)) {
            log.rootCause = "no message";
        }

        return log;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public LogLevel getLevel() {
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

    public void setLevel(LogLevel level) {
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
