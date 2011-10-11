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

package com.github.dbourdette.glass.log.log.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.dbourdette.glass.log.log.Log;
import com.github.dbourdette.glass.log.log.LogsStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class MemoryLogsStore implements LogsStore {
    private List<Log> logs = new ArrayList<Log>();

    private static final int MAX_SIZE = 10000;

    @Override
    public synchronized Page<Log> getLogs(Long executionId, Query query) {
        List<Log> matchingLogs = new ArrayList<Log>();

        for (Log log : logs) {
            if (executionId.equals(log.getExecutionId())) {
                matchingLogs.add(log);
            }
        }

        return getLogs(matchingLogs, query);
    }

    @Override
    public synchronized Page<Log> getLogs(Query query) {
        return getLogs(logs, query);
    }

    @Override
    public synchronized List<Log> getLogs(Long executionId) {
        List<Log> matchingLogs = new ArrayList<Log>();

        for (Log log : logs) {
            if (executionId.equals(log.getExecutionId())) {
                matchingLogs.add(log);
            }
        }

        return matchingLogs;
    }

    @Override
    public synchronized void add(Log log) {
        logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }

    @Override
    public synchronized void clear() {
        logs.clear();
    }

    private Page<Log> getLogs(List<Log> matchingLogs, Query query) {
        Page<Log> page = Page.fromQuery(query);

        List<Log> subList = query.subList(matchingLogs);
        Collections.reverse(subList);

        page.setItems(subList);
        page.setTotalCount(matchingLogs.size());

        return page;
    }
}
