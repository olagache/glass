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

package org.glass;

import org.apache.commons.lang.StringUtils;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * Reads parameters from ServletContext and provides easy access to application configuration.
 *
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
@Component
public class Parameters {

    public static final String MEMORY = "memory";

    public static final String DEFAULT_TABLE_PREFIX = "glass_";

    @Inject
    private ServletContext servletContext;

    /**
     * Supported values are : memory (default), oracle and mysql.
     */
    private String store;

    private String tablePrefix;

    @PostConstruct
    public void init() {
        store = servletContext.getInitParameter("org.glass/store");

        if (StringUtils.isEmpty(store)) {
            store = MEMORY;
        }

        tablePrefix = servletContext.getInitParameter("org.glass/tablePrefix");

        if (StringUtils.isEmpty(tablePrefix)) {
            tablePrefix = DEFAULT_TABLE_PREFIX;
        }
    }

    public boolean isInMemory() {
        return MEMORY.equals(store);
    }

    public String getDriverDelegateClass() {
        if ("oracle".equals(store)) {
            return OracleDelegate.class.getName();
        } else if ("mysql".equals(store)) {
            return StdJDBCDelegate.class.getName();
        }

        return "";
    }

    public String getStore() {
        return store;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }
}
