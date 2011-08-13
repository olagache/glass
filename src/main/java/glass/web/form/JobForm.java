package glass.web.form;

import glass.job.JobUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class JobForm {
    private String properties;

    public JobForm() {}

    public JobForm(JobDetail jobDetail) {
        this.properties = JobUtils.buildProperties(jobDetail.getJobDataMap(), "\n");
    }

    public JobDetail getJobDetails(JobDetail job) {
        return JobBuilder.newJob(job.getJobClass())
                         .withIdentity(job.getKey())
                         .usingJobData(JobUtils.buildDataMap(properties))
                         .storeDurably()
                         .build();
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
