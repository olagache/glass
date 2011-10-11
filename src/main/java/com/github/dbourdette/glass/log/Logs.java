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

package com.github.dbourdette.glass.log;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import com.github.dbourdette.glass.log.execution.Execution;
import com.github.dbourdette.glass.job.annotation.JobArgumentBean;
import com.github.dbourdette.glass.job.util.Spring;
import com.github.dbourdette.glass.log.log.Log;
import com.github.dbourdette.glass.log.log.LogLevel;
import com.github.dbourdette.glass.log.log.LogsStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * Sends logs to log store and through slf4j.
 *
 * @author damien bourdette
 */
public class Logs {
    private static final Logger LOGGER = LoggerFactory.getLogger(Logs.class);

    private static final String[] EMPTY_ARGS = new String[]{};

    private ThreadLocal<JobExecutionContext> localContext = new ThreadLocal<JobExecutionContext>();

    private LogsStore logsStore;

    public Logs(LogsStore logsStore) {
        this.logsStore = logsStore;
    }

    public static Logs getLogs(JobExecutionContext context) throws JobExecutionException {
        Logs logs = Spring.getBean(context, Logs.class);

        logs.localContext.set(context);

        return logs;
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);

        LOGGER.debug(message);
    }

    public void debug(String format, Object... args) {
        log(LogLevel.DEBUG, format(format, args));

        LOGGER.debug(format, args);
    }

    public void debug(String message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);

        LOGGER.debug(message, throwable);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);

        LOGGER.info(message);
    }

    public void info(String format, Object... args) {
        log(LogLevel.INFO, format(format, args));

        LOGGER.info(format, args);
    }

    public void info(String message, Throwable throwable) {
        log(LogLevel.INFO, message, throwable);

        LOGGER.info(message, throwable);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);

        LOGGER.warn(message);
    }

    public void warn(String format, Object... args) {
        log(LogLevel.WARN, format(format, args));

        LOGGER.warn(format, args);
    }

    public void warn(String message, Throwable throwable) {
        log(LogLevel.WARN, message, throwable);

        LOGGER.warn(message, throwable);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);

        LOGGER.error(message);
    }

    public void error(String format, Object... args) {
        log(LogLevel.ERROR, format(format, args));

        LOGGER.error(format, args);
    }

    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);

        LOGGER.error(message, throwable);
    }

    public Page<Log> getLogs(Long executionId, Query query) {
        return logsStore.getLogs(executionId, query);
    }

    public Page<Log> getLogs(Query query) {
        return logsStore.getLogs(query);
    }

    public void clear() {
        logsStore.clear();
    }

    private void log(LogLevel level, String message) {
        log(level, message, EMPTY_ARGS);
    }

    private void log(LogLevel level, String format, Object... args) {
        JobExecutionContext context = localContext.get();

        LogLevel logLevelFromContext = getLogLevelFromContext(context);

        if (level.ordinal() >= logLevelFromContext.ordinal()) {
            logsStore.add(Log.message(Execution.getFromContext(context), level, format(format, args)));
        }

        if (level.ordinal() >= LogLevel.WARN.ordinal()) {
            Execution.failed(context);
        }
    }

    private void log(LogLevel level, String message, Throwable throwable) {
        JobExecutionContext context = localContext.get();

        LogLevel logLevelFromContext = getLogLevelFromContext(context);

        if (level.ordinal() >= logLevelFromContext.ordinal()) {
            logsStore.add(Log.exception(Execution.getFromContext(context), level, message, throwable));
        }

        if (level.ordinal() >= LogLevel.WARN.ordinal()) {
            Execution.failed(context);
        }
    }

    private String format(String format, Object... args) {
        if (args.length == 0) {
            return format;
        }

        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

    private LogLevel getLogLevelFromContext(JobExecutionContext context) {
        String value = context.getMergedJobDataMap().getString(JobArgumentBean.LOG_LEVEL_ARGUMENT);

        if (StringUtils.isEmpty(value)) {
            return LogLevel.WARN;
        }

        try {
            return LogLevel.valueOf(value);
        } catch (Exception e) {
            LOGGER.warn("{} has an incorrect value ({}) for job, defaulting to WARN", JobArgumentBean.LOG_LEVEL_ARGUMENT, value);

            return LogLevel.WARN;
        }
    }
}
