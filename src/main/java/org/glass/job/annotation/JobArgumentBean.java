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

import org.codehaus.jackson.annotate.JsonProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean that can be used in jsp files and in json serialisations.
 */
public class JobArgumentBean {

    @JsonProperty
    String name;

    @JsonProperty
    boolean required;

    @JsonProperty
    String description;

    @JsonProperty
    String defaultValue;

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

        return jobArguments;
    }

    public JobArgumentBean() {

    }

    public JobArgumentBean(String name, JobArgument argument) {
        this.name = name;
        required = argument.required();
        description = argument.description();
        sampleValues = argument.sampleValues();
    }

    public JobArgumentBean(String name, boolean required, String description, String defaultValue, String[] sampleValues) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.defaultValue = defaultValue;
        this.sampleValues = sampleValues;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(String[] sampleValues) {
        this.sampleValues = sampleValues;
    }

}
