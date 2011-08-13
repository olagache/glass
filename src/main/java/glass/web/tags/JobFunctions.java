package glass.web.tags;

import org.quartz.InterruptableJob;
import org.quartz.JobDetail;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class JobFunctions {
    public static boolean isInterruptible(JobDetail job) {
        return InterruptableJob.class.isAssignableFrom(job.getJobClass());
    }
}
