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

import org.glass.history.ExecutionLog;
import org.glass.job.util.Spring;
import org.glass.util.Page;
import org.glass.util.Query;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author damien bourdette
 */
public class Logs {
    private static final Logger LOGGER = LoggerFactory.getLogger(Logs.class);

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

    private void log(LogLevel level, String message) {
        JobExecutionContext context = localContext.get();

        logsStore.add(Log.message(ExecutionLog.getFromContext(context), level, message));
    }

    private void log(LogLevel level, String format, Object... args) {
        JobExecutionContext context = localContext.get();

        logsStore.add(Log.message(ExecutionLog.getFromContext(context), level, format(format, args)));
    }

    private void log(LogLevel level, String message, Throwable throwable) {
        JobExecutionContext context = localContext.get();

        logsStore.add(Log.exception(ExecutionLog.getFromContext(context), level, message, throwable));
    }

    private String format(String format, Object... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }
}
