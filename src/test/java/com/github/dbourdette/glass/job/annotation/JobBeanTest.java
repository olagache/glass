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

import com.github.dbourdette.glass.job.annotation.JobBean;
import com.github.dbourdette.glass.job.dummy.DummyJob;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author damien bourdette
 */
public class JobBeanTest {
    @Test
    public void getDescription() {
        Assert.assertEquals("Description is not correct", "Dummy job for testing purposes", JobBean.getDescription(DummyJob.class));
    }

    @Test
    public void isDisallowConcurrentExecution() {
        Assert.assertEquals("DummyJob do not allow concurent execution", true, JobBean.isDisallowConcurrentExecution(DummyJob.class));
    }

    @Test
    public void isPersistJobDataAfterExecution() {
        Assert.assertEquals("DummyJob do not persist data after execution", false, JobBean.isPersistJobDataAfterExecution(DummyJob.class));
    }
}
