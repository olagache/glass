/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/annotation/JobArgument.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-11 15:34:00 +0200 (jeu., 11 août 2011) $
 */
package glass.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JobSupport arguments metatdata: self-description and automatic params binding.
 * 
 * @copyright RTL Group 2008
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67589 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface JobArgument {

    /**
     * Name of Job Parameter Map to bind into argument
     */
    String name();

	/**
	 * Whether the parameter is required in Job Parameters Map
     */
    boolean required() default false;

    /**
     * User friendly description of parameter
     */
    String description() default "";

    /**
     * Sample values to illustrate what kind of entry the user is expected to enter.
     */
    String[] sampleValues() default {};

}
