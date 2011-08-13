/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/annotation/Job.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-11 15:34:00 +0200 (jeu., 11 août 2011) $
 */
package glass.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JobSupport metadata: Job self-description.
 * 
 * @copyright RTL Group 2008
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67589 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface Job {

    /**
     * User friendly description of job
     */
    String description();

}
