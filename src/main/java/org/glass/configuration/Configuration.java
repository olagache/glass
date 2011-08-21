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

package org.glass.configuration;

import org.apache.commons.lang.StringUtils;
import org.glass.job.JobUtils;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads parameters from ServletContext and provides easy access to application configuration.
 *
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    public static final String DEFAULT_TABLE_PREFIX = "glass_";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Store store = Store.MEMORY;

    private String tablePrefix = DEFAULT_TABLE_PREFIX;

    private String jobBasePackage = JobUtils.class.getPackage().getName();

    private String dateFormat = DEFAULT_DATE_FORMAT;

    private InjectionType injectionType = InjectionType.SETTER;

    public void init(ServletContext servletContext) {
        String value = servletContext.getInitParameter("glass/store");
        if (StringUtils.isNotEmpty(value)) { store = Store.valueOf(value); }

        value = servletContext.getInitParameter("glass/store.tablePrefix");
        if (StringUtils.isNotEmpty(value)) { tablePrefix = value; }

        value = servletContext.getInitParameter("glass/job.basePackage");
        if (StringUtils.isNotEmpty(value)) { jobBasePackage = value; }

        value = servletContext.getInitParameter("glass/job.dateFormat");
        if (StringUtils.isNotEmpty(value)) { dateFormat = servletContext.getInitParameter("glass/dateFormat"); }

        value = servletContext.getInitParameter("glass/job.injectionType");
        if (StringUtils.isNotEmpty(value)) { injectionType = InjectionType.valueOf(value); }

        LOG.info("Using store " + store);
        LOG.info("Using store tablePrefix " + tablePrefix);
        LOG.info("Using job basePackage " + jobBasePackage);
        LOG.info("Using job dateFormat " + dateFormat);
        LOG.info("Using job injectionType " + injectionType);
    }

    public boolean isInMemory() {
        return Store.MEMORY.equals(store);
    }

    public String getDriverDelegateClass() {
        if ("oracle".equals(store)) {
            return OracleDelegate.class.getName();
        } else if ("mysql".equals(store)) {
            return StdJDBCDelegate.class.getName();
        }

        return "";
    }

    public Store getStore() {
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

    public InjectionType getInjectionType() {
        return injectionType;
    }
}
