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

package org.glass.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Service
public class Logs {
    private List<Log> logs = new ArrayList<Log>();

    private static final int MAX_SIZE = 10000;

    private static final int PAGE_SIZE = 100;

    public synchronized void add(Log log) {
        logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }

    public synchronized List<Log> getLogs() {
        List<Log> page = null;

        if (logs.size() > PAGE_SIZE) {
            page = new ArrayList<Log>(logs.subList(0, PAGE_SIZE));
        } else {
            page = new ArrayList<Log>(logs.subList(0, logs.size()));
        }

        Collections.reverse(page);

        return page;
    }
}
