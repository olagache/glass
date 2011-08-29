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

package org.glass.history.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.glass.history.ExecutionLog;
import org.glass.history.History;
import org.glass.util.Page;
import org.glass.util.Query;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author damien bourdette
 */
public class MemoryHistory implements History {
    private final List<ExecutionLog> logs = new ArrayList<ExecutionLog>();

    private static final int MAX_SIZE = 1000;

    private static Long identifier = 0l;

    @Override
    public synchronized ExecutionLog jobStarts(JobExecutionContext context) {
        identifier++;

        ExecutionLog log = new ExecutionLog();

        log.setId(identifier);
        log.fillWithContext(context);

        addLog(log);

        return log;
    }

    @Override
    public synchronized void jobEnds(ExecutionLog log, JobExecutionContext context, JobExecutionException exception) {
        log.setEndDate(new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate());
        log.setEnded(true);
        log.setSuccess(exception == null);
    }

    @Override
    public synchronized Page<ExecutionLog> getLogs(Query query) {
        return getLogs(logs, query);
    }

    @Override
    public Page<ExecutionLog> getLogs(String jobGroup, String jobName, Query query) {
        List<ExecutionLog> matchingLogs = new ArrayList<ExecutionLog>();

        for (ExecutionLog log : logs) {
            if (jobGroup.equals(log.getJobGroup()) && jobName.equals(log.getJobName())) {
                matchingLogs.add(log);
            }
        }

        return getLogs(matchingLogs, query);
    }

    private void addLog(ExecutionLog log) {
        logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }

    private Page<ExecutionLog> getLogs(List<ExecutionLog> matchingLogs, Query query) {
        Page<ExecutionLog> page = Page.fromQuery(query);

        List<ExecutionLog> subList = query.subList(matchingLogs);
        Collections.reverse(subList);

        page.setItems(subList);
        page.setTotalCount(matchingLogs.size());

        return page;
    }
}
