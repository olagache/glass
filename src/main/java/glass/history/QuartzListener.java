package glass.history;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Component
public class QuartzListener extends JobListenerSupport {
    @Inject
    private History history;

    @Override
    public String getName() {
        return QuartzListener.class.getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        history.jobStarts(jobExecutionContext);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException exception) {
        history.jobEnds(jobExecutionContext, exception);
    }
}
