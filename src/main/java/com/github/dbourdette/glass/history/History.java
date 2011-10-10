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

package com.github.dbourdette.glass.history;

import org.quartz.JobExecutionContext;

import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * History service to store ExecutionLog
 *
 * @author damien bourdette
 */
public interface History {
    public ExecutionLog jobStarts(JobExecutionContext context);

    public void jobEnds(ExecutionLog log, JobExecutionContext context, boolean success);

    public Page<ExecutionLog> getLogs(Query query);

    public Page<ExecutionLog> getLogs(String jobGroup, String jobName, Query query);

    public void clear();
}
