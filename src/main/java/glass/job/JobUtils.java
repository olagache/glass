/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/job/JobUtils.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-12 17:34:28 +0200 (ven., 12 août 2011) $
 */
package glass.job;

import glass.SpringConfig;
import glass.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.Job;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67598 $
 * @copyright RTL Group 2008
 */
public class JobUtils {

    public static <T> T getSpringBean(JobExecutionContext context, Class<T> beanClass) throws JobExecutionException {
        ApplicationContext applicationContext = getApplicationContext(context);

        return applicationContext.getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext applicationContext = null;

        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();

            applicationContext = (ApplicationContext) schedulerContext.get(SpringConfig.APPLICATION_CONTEXT_KEY);
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to get scheduler from JobExecutionContext");
        }

        if (applicationContext == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \""
                    + SpringConfig.APPLICATION_CONTEXT_KEY + "\"");
        }

        return applicationContext;
    }

    public static List<JobArgumentWithField> getJobArgumentsWithFields(Class<? extends Job> jobClass) {

        List<JobArgumentWithField> jobArguments = new ArrayList<JobArgumentWithField>();
        JobArgument argument;
        for (Field field : jobClass.getDeclaredFields()) {
            // get annotation
            argument = field.getAnnotation(JobArgument.class);
            if (argument != null) {
                // Disable security constraints for reflection operations.
                // it only affects reflection operations, not regular code execution
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                jobArguments.add(new JobArgumentWithField(argument, field));
            }
        }
        return jobArguments;
    }

    public static List<JobArgumentBean> getJobArguments(Class<?> jobClass) {
        if (jobClass == null) {
            return null;
        }

        List<JobArgumentBean> jobArguments = new ArrayList<JobArgumentBean>();
        JobArgument argument;
        for (Field field : jobClass.getDeclaredFields()) {
            // get annotation
            argument = field.getAnnotation(JobArgument.class);
            if (argument != null) {
                jobArguments.add(new JobArgumentBean(argument));
            }
        }
        return jobArguments;
    }

    public static String getJobDescription(Class<?> jobClass) {
        if (jobClass == null) {
            return null;
        }

        glass.annotation.Job annotation = jobClass.getAnnotation(glass.annotation.Job.class);

        if (annotation == null) {
            return null;
        }

        return annotation.description();
    }

    public static String buildProperties(JobDataMap jobDataMap, String separator) {
        String[] keys = jobDataMap.getKeys();

        StringBuilder stringBuilder = new StringBuilder();

        for (String key : keys) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(jobDataMap.getString(key));
            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    public static JobDataMap buildDataMap(String properties) {
        if (StringUtils.isEmpty(properties)) {
            return new JobDataMap();
        }

        Properties props = new Properties();

        try {
            props.load(new StringReader(properties));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JobDataMap map = new JobDataMap();

        for (Object key : props.keySet()) {
            map.put(key, props.getProperty((String) key));
        }

        return map;
    }

    private static class JobArgumentWithField {

        private final JobArgument argument;

        private final Field field;

        public JobArgumentWithField(JobArgument argument, Field field) {
            this.argument = argument;
            this.field = field;
        }

        public JobArgument getArgument() {
            return argument;
        }

        public Field getField() {
            return field;
        }

    }

}
