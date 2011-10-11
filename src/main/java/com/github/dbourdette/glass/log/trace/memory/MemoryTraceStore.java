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

package com.github.dbourdette.glass.log.trace.memory;

import java.util.ArrayList;
import java.util.List;

import com.github.dbourdette.glass.log.trace.Trace;
import com.github.dbourdette.glass.log.trace.TraceStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class MemoryTraceStore implements TraceStore {
    private List<Trace> traces = new ArrayList<Trace>();

    private static final int MAX_SIZE = 10000;

    @Override
    public synchronized Page<Trace> getLogs(Long executionId, Query query) {
        List<Trace> matchingTraces = new ArrayList<Trace>();

        for (Trace trace : traces) {
            if (executionId.equals(trace.getExecutionId())) {
                matchingTraces.add(trace);
            }
        }

        return getLogs(matchingTraces, query);
    }

    @Override
    public synchronized Page<Trace> getLogs(Query query) {
        return getLogs(traces, query);
    }

    @Override
    public synchronized void add(Trace trace) {
        traces.add(trace);

        if (traces.size() > MAX_SIZE) {
            traces.remove(0);
        }
    }

    @Override
    public synchronized void clear() {
        traces.clear();
    }

    private Page<Trace> getLogs(List<Trace> matchingTraces, Query query) {
        Page<Trace> page = Page.fromQuery(query);

        page.setItems(query.subList(matchingTraces));
        page.setTotalCount(matchingTraces.size());

        return page;
    }
}
