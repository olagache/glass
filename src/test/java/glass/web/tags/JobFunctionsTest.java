package glass.web.tags;

import glass.job.DummyJob;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.impl.JobDetailImpl;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class JobFunctionsTest {
    @Test
    public void testIsInterruptable() throws Exception {
        JobDetailImpl job = new JobDetailImpl();
        job.setJobClass(DummyJob.class);

        Assert.assertEquals(true, JobFunctions.isInterruptible(job));
    }
}
