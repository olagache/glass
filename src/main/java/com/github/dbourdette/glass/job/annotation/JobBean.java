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

package com.github.dbourdette.glass.job.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Bean that can be used in jsp files and in json serialisations.
 *
 * @author damien bourdette
 */
public class JobBean {
    @JsonProperty
    private String description;

    @JsonProperty
    private boolean disallowConcurrentExecution;

    @JsonProperty
    private boolean persistJobDataAfterExecution;

    @JsonProperty
    private List<JobArgumentBean> arguments;

    public static JobBean fromClass(Class<?> jobClass) {
        JobBean jobBean = new JobBean();

        jobBean.description = getDescription(jobClass);
        jobBean.disallowConcurrentExecution = isDisallowConcurrentExecution(jobClass);
        jobBean.persistJobDataAfterExecution = isPersistJobDataAfterExecution(jobClass);
        jobBean.arguments = JobArgumentBean.fromClass(jobClass);

        return jobBean;
    }

    public static String getDescription(Class<?> jobClass) {
        Job annotation = getAnnotation(jobClass, Job.class);

        if (annotation == null) {
            return "";
        }

        return annotation.description();
    }

    public static boolean isDisallowConcurrentExecution(Class<?> jobClass) {
        return getAnnotation(jobClass, DisallowConcurrentExecution.class) == null ? false : true;
    }

    public static boolean isPersistJobDataAfterExecution(Class<?> jobClass) {
        return getAnnotation(jobClass, PersistJobDataAfterExecution.class) == null ? false : true;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisallowConcurrentExecution() {
        return disallowConcurrentExecution;
    }

    public boolean isPersistJobDataAfterExecution() {
        return persistJobDataAfterExecution;
    }

    private static <T extends Annotation> T getAnnotation(Class<?> jobClass, Class<T> annotationClass) {
        if (jobClass == null) {
            return null;
        }

        return jobClass.getAnnotation(annotationClass);
    }
}
