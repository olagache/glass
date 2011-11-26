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

package com.github.dbourdette.glass.log.joblog;

import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.glass.log.joblog.memory.MemoryTraceStore;
import com.github.dbourdette.glass.util.Query;

import static junit.framework.Assert.assertEquals;

/**
 * @author damien bourdette
 */
public class JobLogsTest {
    private MemoryTraceStore store = new MemoryTraceStore();

    @Before
    public void init() {
        store.clear();

        JobLogs.traceStore = store;
    }

    @Test
    public void info() {
        JobLogs.setLevel(TraceLevel.INFO);

        JobLogs.debug("this should not go in traces");
        assertEquals(0, store.getLogs(Query.firstPage()).getTotalCount());

        JobLogs.info("this one should do");
        assertEquals(1, store.getLogs(Query.firstPage()).getTotalCount());
    }
}
