/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/job/DummyJob.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-12 17:34:28 +0200 (ven., 12 août 2011) $
 */
package glass.job;

import glass.annotation.Job;
import glass.annotation.JobArgument;
import glass.log.Log;
import glass.log.Logs;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * A dummy quartz job for testing purposes
 * 
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67598 $
 */
@Job(description = "Dummy job for testing purposes")
@DisallowConcurrentExecution
public class DummyJob implements InterruptableJob {

    @JobArgument(name = "duration", required = true, description = "Duration of the job, in seconds", sampleValues = "10, 60")
    private Long duration = 10l;

    private Thread runningThread;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        runningThread = Thread.currentThread();

        JobUtils.getSpringBean(context, Logs.class).add(Log.info("Running dummy job for " + duration));

        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            throw new JobExecutionException(e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        runningThread.interrupt();
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
