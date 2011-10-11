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

package com.github.dbourdette.glass;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.velocity.exception.VelocityException;
import org.quartz.Scheduler;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.configuration.LogStore;
import com.github.dbourdette.glass.log.execution.Executions;
import com.github.dbourdette.glass.log.execution.QuartzListenerForExecutions;
import com.github.dbourdette.glass.log.execution.jdbc.JdbcExecutions;
import com.github.dbourdette.glass.log.execution.memory.MemoryExecutions;
import com.github.dbourdette.glass.job.GlassJobFactory;
import com.github.dbourdette.glass.log.trace.Traces;
import com.github.dbourdette.glass.log.trace.QuartzListenerForTraces;
import com.github.dbourdette.glass.log.trace.jdbc.JdbcTraceStore;
import com.github.dbourdette.glass.log.trace.memory.MemoryTraceStore;

@org.springframework.context.annotation.Configuration
@EnableWebMvc
public class SpringConfig {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";

    @Inject
    private QuartzListenerForExecutions quartzListenerForExecutions;

    @Inject
    private QuartzListenerForTraces quartzListenerForLogs;

    @Inject
    private GlassJobFactory glassJobFactory;

    @Bean
    public Configuration configuration() throws Exception {
        return new Configuration();
    }

    @Bean
    public DataSource dataSource() throws Exception {
        if (configuration().isInMemory()) {
            return null;
        }

        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();

        factoryBean.setJndiName("java:comp/env/jdbc/glassDb");

        factoryBean.afterPropertiesSet();

        return (DataSource) factoryBean.getObject();
    }

    @Bean
    public Scheduler quartzScheduler(ApplicationContext context) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setApplicationContext(context);
        factory.setExposeSchedulerInRepository(true);
        factory.setApplicationContextSchedulerContextKey(APPLICATION_CONTEXT_KEY);
        factory.setJobFactory(glassJobFactory);

        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
        properties.setProperty("org.quartz.threadPool.threadCount", "15");
        properties.setProperty("org.quartz.threadPool.threadPriority", "4");

        if (configuration().isInMemory()) {
            properties.setProperty("org.quartz.jobStore.class", RAMJobStore.class.getName());
        } else {
            factory.setDataSource(dataSource());

            properties.setProperty("org.quartz.jobStore.tablePrefix", configuration().getTablePrefix());
            properties.setProperty("org.quartz.jobStore.isClustered", "false");
            properties.setProperty("org.quartz.jobStore.driverDelegateClass", configuration().getDriverDelegateClass());
        }

        factory.setQuartzProperties(properties);

        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getObject();

        scheduler.getListenerManager().addJobListener(quartzListenerForExecutions);
        scheduler.getListenerManager().addSchedulerListener(quartzListenerForLogs);

        scheduler.start();

        return scheduler;
    }

    @Bean
    public Executions executions() throws Exception {
        if (configuration().getLogStore() == LogStore.MEMORY) {
            return new MemoryExecutions();
        } else {
            return new JdbcExecutions(dataSource(), configuration());
        }
    }

    @Bean
    public Traces traces() throws Exception {
        if (configuration().getLogStore() == LogStore.MEMORY) {
            return new Traces(new MemoryTraceStore());
        } else {
            return new Traces(new JdbcTraceStore(dataSource(), configuration()));
        }
    }

    @Bean
    public FixedLocaleResolver fixedLocaleResolver() {
        FixedLocaleResolver resolver = new FixedLocaleResolver();
        resolver.setDefaultLocale(Locale.FRANCE);
        return resolver;
    }

    @Bean
    public VelocityViewResolver viewResolver() {
        VelocityViewResolver viewResolver = new VelocityViewResolver();
        viewResolver.setCache(true);
        viewResolver.setPrefix("com/github/dbourdette/glass/velocity/");
        viewResolver.setSuffix(".vm");
        return viewResolver;
    }

    @Bean
    public VelocityConfig velocityConfig() throws IOException, VelocityException {
        Properties config = new Properties();
        config.setProperty("resource.loader", "class");
        config.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        velocityConfigurer.setVelocityProperties(config);
        velocityConfigurer.afterPropertiesSet();

        return velocityConfigurer;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
