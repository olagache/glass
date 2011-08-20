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
import org.junit.Assert;
import org.junit.Test;
import org.quartz.impl.JobDetailImpl;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class UtilsToolTest {

    private UtilsTool utilsTool = new UtilsTool();

    @Test
    public void testIsInterruptable() throws Exception {
        JobDetailImpl job = new JobDetailImpl();
        job.setJobClass(DummyJob.class);

        Assert.assertEquals(true, utilsTool.isInterruptible(job));
    }
}
