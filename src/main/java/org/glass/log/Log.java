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

package org.glass.log;

import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.glass.history.ExecutionLog;
import org.glass.util.Dates;

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

    private Log() {

    }

    public static Log message(ExecutionLog executionLog, LogLevel level, String message) {
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

    public static Log exception(ExecutionLog executionLog, LogLevel level, String message, Throwable e) {
        Log log = message(executionLog, level, message);

        log.stackTrace = ExceptionUtils.getFullStackTrace(e);
        log.rootCause = ExceptionUtils.getMessage(ExceptionUtils.getRootCause(e));

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

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
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
}
