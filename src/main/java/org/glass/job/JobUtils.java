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

package org.glass.job;

import org.glass.SpringConfig;
import org.glass.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.Job;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JobUtils {

    public static <T> T getSpringBean(JobExecutionContext context, Class<T> beanClass) throws JobExecutionException {
        ApplicationContext applicationContext = getApplicationContext(context);

        return applicationContext.getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext(JobExecutionContext context) throws JobExecutionException {
        try {
            return getApplicationContext(context.getScheduler().getContext());
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to get scheduler from JobExecutionContext");
        }
    }

    public static ApplicationContext getApplicationContext(SchedulerContext schedulerContext) throws JobExecutionException {
        ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(SpringConfig.APPLICATION_CONTEXT_KEY);

        if (applicationContext == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \""
                    + SpringConfig.APPLICATION_CONTEXT_KEY + "\"");
        }

        return applicationContext;
    }

    public static List<JobArgumentWithField> getJobArgumentsWithFields(Class<? extends Job> jobClass) {

        List<JobArgumentWithField> jobArguments = new ArrayList<JobArgumentWithField>();
        JobArgument argument;
        for (Field field : jobClass.getDeclaredFields()) {
            // get annotation
            argument = field.getAnnotation(JobArgument.class);
            if (argument != null) {
                // Disable security constraints for reflection operations.
                // it only affects reflection operations, not regular code execution
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                jobArguments.add(new JobArgumentWithField(argument, field));
            }
        }
        return jobArguments;
    }

    public static String getJobDescription(Class<?> jobClass) {
        if (jobClass == null) {
            return "";
        }

        org.glass.annotation.Job annotation = jobClass.getAnnotation(org.glass.annotation.Job.class);

        if (annotation == null) {
            return "";
        }

        return annotation.description();
    }

    public static String toProperties(JobDataMap jobDataMap, String separator) {
        String[] keys = jobDataMap.getKeys();

        StringBuilder stringBuilder = new StringBuilder();

        for (String key : keys) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(jobDataMap.getString(key));
            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    public static JobDataMap fromProperties(String dataMap) {
        if (StringUtils.isEmpty(dataMap)) {
            return new JobDataMap();
        }

        Properties props = new Properties();

        try {
            props.load(new StringReader(dataMap));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JobDataMap map = new JobDataMap();

        for (Object key : props.keySet()) {
            map.put(key, props.getProperty((String) key));
        }

        return map;
    }

    private static class JobArgumentWithField {

        private final JobArgument argument;

        private final Field field;

        public JobArgumentWithField(JobArgument argument, Field field) {
            this.argument = argument;
            this.field = field;
        }

        public JobArgument getArgument() {
            return argument;
        }

        public Field getField() {
            return field;
        }

    }

}
