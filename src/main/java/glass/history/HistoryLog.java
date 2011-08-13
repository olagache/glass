package glass.history;

import glass.job.JobUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class HistoryLog {
    private Date date;

    private HistoryLogType type;

    private String jobGroup;

    private String jobName;

    private String triggerGroup;

    private String triggerName;

    private String jobClass;

    private String properties;

    private String stackTrace;

    public static HistoryLog onStart(JobExecutionContext context) {
        HistoryLog log = onEvent(context);

        log.type = HistoryLogType.START;
        log.date = context.getFireTime();

        return log;
    }

    public static HistoryLog onEnd(JobExecutionContext context, JobExecutionException exception) {
        HistoryLog log = onEvent(context);

        log.type = HistoryLogType.END;
        log.date = new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate();

        if (exception != null) {
            log.stackTrace = ExceptionUtils.getFullStackTrace(exception);
        }

        return log;
    }

    public HistoryLog() {

    }

    public Date getDate() {
        return date;
    }

    public String getJobClass() {
        return jobClass;
    }

    public HistoryLogType getType() {
        return type;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "HistoryLog{" +
                "date=" + date +
                ", type=" + type +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobName='" + jobName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", properties='" + properties + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    /**
     * Fill common attributes
     */
    private static HistoryLog onEvent(JobExecutionContext context) {
        HistoryLog log = new HistoryLog();

        log.jobClass = context.getJobDetail().getJobClass().getName();
        log.jobGroup = context.getJobDetail().getKey().getGroup();
        log.jobName = context.getJobDetail().getKey().getName();
        log.triggerGroup = context.getTrigger().getKey().getGroup();
        log.triggerName = context.getTrigger().getKey().getName();
        log.properties = JobUtils.buildProperties(context.getMergedJobDataMap(), "\n");

        return log;
    }
}
