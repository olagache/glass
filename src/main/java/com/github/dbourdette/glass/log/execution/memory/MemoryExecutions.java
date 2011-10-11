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

package com.github.dbourdette.glass.log.execution.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;

import com.github.dbourdette.glass.log.execution.Execution;
import com.github.dbourdette.glass.log.execution.Executions;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class MemoryExecutions implements Executions {
    private final List<Execution> executions = new ArrayList<Execution>();

    private static final int MAX_SIZE = 1000;

    private static Long identifier = 0l;

    @Override
    public synchronized Execution jobStarts(JobExecutionContext context) {
        identifier++;

        Execution execution = new Execution();

        execution.setId(identifier);
        execution.fillWithContext(context);

        addLog(execution);

        return execution;
    }

    @Override
    public synchronized void jobEnds(Execution execution, JobExecutionContext context) {
        execution.setEndDate(new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate());
        execution.setEnded(true);
    }

    @Override
    public synchronized Page<Execution> find(Query query) {
        return getLogs(executions, query);
    }

    @Override
    public synchronized Page<Execution> find(String jobGroup, String jobName, Query query) {
        List<Execution> matchingLogs = new ArrayList<Execution>();

        for (Execution execution : executions) {
            if (jobGroup.equals(execution.getJobGroup()) && jobName.equals(execution.getJobName())) {
                matchingLogs.add(execution);
            }
        }

        return getLogs(matchingLogs, query);
    }

    @Override
    public synchronized void clear() {
        executions.clear();
    }

    private void addLog(Execution execution) {
        executions.add(execution);

        if (executions.size() > MAX_SIZE) {
            executions.remove(0);
        }
    }

    private Page<Execution> getLogs(List<Execution> matchingExecutions, Query query) {
        Page<Execution> page = Page.fromQuery(query);

        List<Execution> subList = query.subList(matchingExecutions);
        Collections.reverse(subList);

        page.setItems(subList);
        page.setTotalCount(matchingExecutions.size());

        return page;
    }
}
