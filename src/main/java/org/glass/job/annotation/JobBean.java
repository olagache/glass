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

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Bean that can be used in jsp files and in json serialisations.
 *
 * @author damien bourdette
 */
public class JobBean {
    @JsonProperty
    private String description;

    @JsonProperty
    private List<JobArgumentBean> arguments;

    public static JobBean fromClass(Class<?> jobClass) {
        JobBean jobBean = new JobBean();

        jobBean.description = getDescription(jobClass);
        jobBean.arguments = JobArgumentBean.fromClass(jobClass);

        return jobBean;
    }

    public static String getDescription(Class<?> jobClass) {
        if (jobClass == null) {
            return "";
        }

        Job annotation = jobClass.getAnnotation(Job.class);

        if (annotation == null) {
            return "";
        }

        return annotation.description();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
