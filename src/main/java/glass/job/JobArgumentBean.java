/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/web/util/JobArgumentBean.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-11 15:34:00 +0200 (jeu., 11 août 2011) $
 */
package glass.job;

import glass.annotation.JobArgument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.lang.annotation.Annotation;

/**
 * Used to show up JobArgument objects in JSP in an "EL friendly" way (JobArgument is not a bean)
 * 
 * @copyright RTL Group 2008
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67589 $
 */

public class JobArgumentBean {

    @JsonProperty
    String name;

    @JsonProperty
    boolean required;

    @JsonProperty
    String description;

    @JsonProperty
    String defaultValue;

    @JsonProperty
    String[] sampleValues;

    public JobArgumentBean() {

    }

    public JobArgumentBean(JobArgument argument) {
        name = argument.name();
        required = argument.required();
        description = argument.description();
        sampleValues = argument.sampleValues();
    }

    public JobArgumentBean(String name, boolean required, String description, String defaultValue, String[] sampleValues) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.defaultValue = defaultValue;
        this.sampleValues = sampleValues;
    }

    @JsonIgnore
    public JobArgument getJobArgument() {
        return new JobArgument() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return JobArgument.class;
            }

            @Override
            public String[] sampleValues() {
                return getSampleValues();
            }

            @Override
            public boolean required() {
                return isRequired();
            }

            @Override
            public String name() {
                return getName();
            }

            @Override
            public String description() {
                return getDescription();
            }
        };
    }

    /*
     * Accessors
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(String[] sampleValues) {
        this.sampleValues = sampleValues;
    }

}
