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

package com.github.dbourdette.glass.web.util;

import java.util.List;

import org.quartz.JobDetail;

/**
 * Allow controllers to send a job and its associated triggers
 *
 * @author damien bourdette
 */
public class JobAndTriggers {
    private JobDetail jobDetail;

    private List<TriggerWrapperForView> triggers;

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public List<TriggerWrapperForView> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerWrapperForView> triggers) {
        this.triggers = triggers;
    }
}
