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

import org.glass.history.ExcecutionLog;
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
    private final List<ExcecutionLog> logs = new ArrayList<ExcecutionLog>();

    private static final int MAX_SIZE = 1000;

    private static Long identifier = 0l;

    private static final String ID_KEY_IN_CONTEXT = "__GLASS_EXECUTION_ID_KEY_IN_CONTEXT";

    @Override
    public synchronized void jobStarts(JobExecutionContext context) {
        ExcecutionLog log = ExcecutionLog.fromContext(context);

        identifier++;

        log.setId(identifier);

        context.put(ID_KEY_IN_CONTEXT, identifier);

        addLog(log);
    }

    @Override
    public synchronized void jobEnds(JobExecutionContext context, JobExecutionException exception) {
        Long id = (Long) context.get(ID_KEY_IN_CONTEXT);

        ExcecutionLog log = findLog(id);

        if (log == null) {
            return;
        }

        log.setStackTrace(exception);
        log.setEndDate(new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate());
        log.setEnded(true);
    }

    @Override
    public synchronized Page<ExcecutionLog> getLogs(Query query) {
        Page<ExcecutionLog> page = Page.fromQuery(query);

        page.setItems(query.subList(logs));
        page.setTotalCount(logs.size());

        Collections.reverse(page.getItems());

        return page;
    }

    private ExcecutionLog findLog(Long id) {
        for (ExcecutionLog log : logs) {
            if (id.equals(log.getId())) {
                return log;
            }
        }

        return null;
    }

    private void addLog(ExcecutionLog log) {
       logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }
}
