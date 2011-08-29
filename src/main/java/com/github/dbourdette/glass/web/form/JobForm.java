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

package com.github.dbourdette.glass.web.form;

import com.github.dbourdette.glass.job.util.JobDataMapUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

/**
 * @author damien bourdette
 */
public class JobForm {
    private String dataMap;

    public JobForm() {}

    public JobForm(JobDetail jobDetail) {
        this.dataMap = JobDataMapUtils.toProperties(jobDetail.getJobDataMap(), "\n");
    }

    public JobDetail getJobDetails(JobDetail job) {
        return JobBuilder.newJob(job.getJobClass())
                         .withIdentity(job.getKey())
                         .usingJobData(JobDataMapUtils.fromProperties(dataMap))
                         .storeDurably()
                         .build();
    }

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }
}
