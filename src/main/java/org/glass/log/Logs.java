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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.glass.util.Page;
import org.glass.util.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author damien bourdette
 */
@Service
public class Logs {
    private static final Logger LOGGER = LoggerFactory.getLogger(Logs.class);

    private List<Log> logs = new ArrayList<Log>();

    private static final int MAX_SIZE = 10000;

    private static final int PAGE_SIZE = 100;

    public void debug(String message) {
        add(Log.message(LogLevel.DEBUG, message));

        LOGGER.debug(message);
    }

    public void debug(String message, java.lang.Throwable throwable) {
        add(Log.exception(LogLevel.DEBUG, message, throwable));

        LOGGER.debug(message, throwable);
    }

    public void info(String message) {
        add(Log.message(LogLevel.INFO, message));

        LOGGER.info(message);
    }

    public void info(String message, java.lang.Throwable throwable) {
        add(Log.exception(LogLevel.INFO, message, throwable));

        LOGGER.info(message, throwable);
    }

    public void warn(String message) {
        add(Log.message(LogLevel.WARN, message));

        LOGGER.warn(message);
    }

    public void warn(String message, java.lang.Throwable throwable) {
        add(Log.exception(LogLevel.WARN, message, throwable));

        LOGGER.warn(message, throwable);
    }

    public void error(String message) {
        add(Log.message(LogLevel.ERROR, message));

        LOGGER.error(message);
    }

    public void error(String message, java.lang.Throwable throwable) {
        add(Log.exception(LogLevel.ERROR, message, throwable));

        LOGGER.error(message, throwable);
    }

    public synchronized Page<Log> getLogs(Query query) {
        Page<Log> page = Page.fromQuery(query);

        List<Log> subList = query.subList(logs);
        Collections.reverse(subList);

        page.setItems(subList);
        page.setTotalCount(logs.size());

        return page;
    }

    private synchronized void add(Log log) {
        logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }
}
