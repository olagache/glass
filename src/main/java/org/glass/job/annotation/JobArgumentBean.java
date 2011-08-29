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

package org.glass.job.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.glass.log.LogLevel;
import org.glass.util.Arrays;

/**
 * Bean that can be used in jsp files and in json serialisations.
 *
 * @author damien bourdette
 */
public class JobArgumentBean {

    public static final String LOG_LEVEL_ARGUMENT = "logLevel";

    @JsonProperty
    String name;

    @JsonProperty
    boolean required;

    @JsonProperty
    String description;

    @JsonProperty
    String[] sampleValues;

    public static List<JobArgumentBean> fromClass(Class<?> jobClass) {
        List<JobArgumentBean> jobArguments = new ArrayList<JobArgumentBean>();

        if (jobClass == null) {
            return null;
        }

        for (Field field : jobClass.getDeclaredFields()) {
            JobArgument argument = field.getAnnotation(JobArgument.class);

            if (argument != null) {
                jobArguments.add(new JobArgumentBean(field.getName(), argument));
            }
        }

        jobArguments.add(new JobArgumentBean(LOG_LEVEL_ARGUMENT, false, "Log level used for this job.",
                new String[] {LogLevel.DEBUG.name(), LogLevel.INFO.name(), LogLevel.WARN.name(), LogLevel.ERROR.name() }));

        return jobArguments;
    }

    public JobArgumentBean() {

    }

    public JobArgumentBean(String name, JobArgument argument) {
        this.name = name;
        required = argument.required();
        description = argument.description();
        sampleValues = Arrays.copyOf(argument.sampleValues());
    }

    public JobArgumentBean(String name, boolean required, String description, String[] sampleValues) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.sampleValues = Arrays.copyOf(sampleValues);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getSampleValues() {
        return Arrays.copyOf(sampleValues);
    }

    public void setSampleValues(String[] sampleValues) {
        this.sampleValues = Arrays.copyOf(sampleValues);
    }

}
