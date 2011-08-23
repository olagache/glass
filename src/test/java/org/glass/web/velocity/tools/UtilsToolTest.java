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

package org.glass.web.velocity.tools;

import org.glass.job.dummy.DummyJob;
import org.glass.velocity.tools.UtilsTool;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.impl.JobDetailImpl;

/**
 * @author damien bourdette
 */
public class UtilsToolTest {

    private UtilsTool utilsTool = new UtilsTool();

    @Test
    public void testIsInterruptable() throws Exception {
        JobDetailImpl job = new JobDetailImpl();
        job.setJobClass(DummyJob.class);

        Assert.assertEquals(true, utilsTool.isInterruptible(job));
    }

    @Test
    public void duration() throws Exception {
        DateTime start = new DateTime();

        Assert.assertEquals("6s", utilsTool.duration(start.toDate(), start.plusSeconds(6).toDate()));
        Assert.assertEquals("1m 2s", utilsTool.duration(start.toDate(), start.plusSeconds(62).toDate()));
        Assert.assertEquals("2h 2s", utilsTool.duration(start.toDate(), start.plusHours(2).plusSeconds(2).toDate()));
    }
}
