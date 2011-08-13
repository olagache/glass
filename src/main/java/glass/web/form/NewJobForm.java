package glass.web.form;

import glass.job.JobUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

import javax.validation.constraints.NotNull;

/**
 * Form for job editing
 * 
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67598 $
 */
public class NewJobForm {

    @NotEmpty
    private String group = "DEFAULT";

    @NotEmpty
    private String name;

    @NotNull
    private Class<? extends Job> clazz;

    private String properties;

    public NewJobForm() {
    }

    public NewJobForm(JobDetail jobDetail) {
        super();
        this.group = jobDetail.getKey().getGroup();
        this.name = jobDetail.getKey().getName();
        this.clazz = jobDetail.getJobClass();
        this.properties = JobUtils.buildProperties(jobDetail.getJobDataMap(), "\n");
    }

    /**
     * Builds a {@link JobDetail} using internal state
     * 
     * @return
     */
    public JobDetail getJobDetails() {
        return JobBuilder.newJob(clazz)
                         .withIdentity(name.trim(), group.trim())
                         .usingJobData(JobUtils.buildDataMap(properties))
                         .storeDurably()
                         .build();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Job> clazz) {
        this.clazz = clazz;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
