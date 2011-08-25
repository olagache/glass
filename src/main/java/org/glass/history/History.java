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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * @author damien bourdette
 */
@Service
public class History {
    private final List<HistoryLog> logs = new ArrayList<HistoryLog>();

    private static final int MAX_SIZE = 1000;

    private static final int PAGE_SIZE = 100;

    public synchronized void jobStarts(JobExecutionContext context) {
        addLog(HistoryLog.onStart(context));
    }

    public synchronized void jobEnds(JobExecutionContext context, JobExecutionException exception) {
        addLog(HistoryLog.onEnd(context, exception));
    }

    public synchronized List<HistoryLog> getLogs() {
        List<HistoryLog> page = null;

        if (logs.size() > PAGE_SIZE) {
            page = new ArrayList<HistoryLog>(logs.subList(0, PAGE_SIZE));
        } else {
            page = new ArrayList<HistoryLog>(logs.subList(0, logs.size()));
        }

        Collections.reverse(page);

        return page;
    }

    private void addLog(HistoryLog log) {
       logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }
}
