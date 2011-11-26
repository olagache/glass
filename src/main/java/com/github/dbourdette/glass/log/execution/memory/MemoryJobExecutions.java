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

import com.github.dbourdette.glass.log.execution.JobExecution;
import com.github.dbourdette.glass.log.execution.JobExecutions;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class MemoryJobExecutions implements JobExecutions {
    private final List<JobExecution> executions = new ArrayList<JobExecution>();

    private static final int MAX_SIZE = 1000;

    private static Long identifier = 0l;

    @Override
    public synchronized JobExecution jobStarts(JobExecutionContext context) {
        identifier++;

        JobExecution execution = new JobExecution();

        execution.setId(identifier);
        execution.fillWithContext(context);

        addLog(execution);

        return execution;
    }

    @Override
    public synchronized void jobEnds(JobExecution execution, JobExecutionContext context) {
        execution.setEndDate(new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate());
        execution.setEnded(true);
    }

    @Override
    public synchronized Page<JobExecution> find(Query query) {
        if (query.getResult() != null) {
            List<JobExecution> matchingLogs = new ArrayList<JobExecution>();

            for (JobExecution execution : executions) {
                if (query.getResult() == execution.getResult()) {
                    matchingLogs.add(execution);
                }
            }

            return getLogs(matchingLogs, query);
        } else {
            return getLogs(executions, query);
        }
    }

    @Override
    public synchronized Page<JobExecution> find(String jobGroup, String jobName, Query query) {
        List<JobExecution> matchingLogs = new ArrayList<JobExecution>();

        for (JobExecution execution : executions) {
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

    private void addLog(JobExecution execution) {
        executions.add(execution);

        if (executions.size() > MAX_SIZE) {
            executions.remove(0);
        }
    }

    private Page<JobExecution> getLogs(List<JobExecution> matchingExecutions, Query query) {
        Page<JobExecution> page = Page.fromQuery(query);

        List<JobExecution> items = new ArrayList<JobExecution>();
        items.addAll(matchingExecutions);

        Collections.reverse(items);
        List<JobExecution> subList = query.subList(items);

        page.setItems(subList);
        page.setTotalCount(matchingExecutions.size());

        return page;
    }
}
