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
import org.glass.job.JobUtils;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;

import javax.servlet.ServletContext;

/**
 * Reads parameters from ServletContext and provides easy access to application configuration.
 *
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class Parameters {

    public static final String MEMORY = "memory";

    public static final String DEFAULT_TABLE_PREFIX = "glass_";

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * Supported values are : memory (default), oracle and mysql.
     */
    private String store = MEMORY;

    private String tablePrefix = DEFAULT_TABLE_PREFIX;

    private String jobBasePackage = JobUtils.class.getPackage().getName();

    private String dateFormat = DEFAULT_DATE_FORMAT;

    public void init(ServletContext servletContext) {
        if (StringUtils.isNotEmpty(servletContext.getInitParameter("glass/store"))) {
            store = servletContext.getInitParameter("glass/store");
        }

        if (StringUtils.isNotEmpty(servletContext.getInitParameter("glass/tablePrefix"))) {
            tablePrefix = servletContext.getInitParameter("glass/tablePrefix");
        }

        if (StringUtils.isNotEmpty(servletContext.getInitParameter("glass/jobBasePackage"))) {
            jobBasePackage = servletContext.getInitParameter("glass/jobBasePackage");
        }

        if (StringUtils.isNotEmpty(servletContext.getInitParameter("glass/dateFormat"))) {
            dateFormat = servletContext.getInitParameter("glass/dateFormat");
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

    public String getJobBasePackage() {
        return jobBasePackage;
    }

    public String getDateFormat() {
        return dateFormat;
    }
}
