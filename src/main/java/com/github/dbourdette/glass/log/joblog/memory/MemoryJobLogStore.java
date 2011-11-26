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

package com.github.dbourdette.glass.log.joblog.memory;

import java.util.ArrayList;
import java.util.List;

import com.github.dbourdette.glass.log.joblog.JobLog;
import com.github.dbourdette.glass.log.joblog.JobLogStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class MemoryJobLogStore implements JobLogStore {
    private List<JobLog> jobLogs = new ArrayList<JobLog>();

    private static final int MAX_SIZE = 10000;

    @Override
    public synchronized Page<JobLog> getLogs(Long executionId, Query query) {
        List<JobLog> matchingJobLogs = new ArrayList<JobLog>();

        for (JobLog jobLog : jobLogs) {
            if (executionId.equals(jobLog.getExecutionId())) {
                matchingJobLogs.add(jobLog);
            }
        }

        return getLogs(matchingJobLogs, query);
    }

    @Override
    public synchronized Page<JobLog> getLogs(Query query) {
        return getLogs(jobLogs, query);
    }

    @Override
    public synchronized void add(JobLog jobLog) {
        jobLogs.add(jobLog);

        if (jobLogs.size() > MAX_SIZE) {
            jobLogs.remove(0);
        }
    }

    @Override
    public synchronized void clear() {
        jobLogs.clear();
    }

    private Page<JobLog> getLogs(List<JobLog> matchingJobLogs, Query query) {
        Page<JobLog> page = Page.fromQuery(query);

        page.setItems(query.subList(matchingJobLogs));
        page.setTotalCount(matchingJobLogs.size());

        return page;
    }
}
