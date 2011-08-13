/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/web/util/JobPathScanner.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-11 15:34:00 +0200 (jeu., 11 août 2011) $
 */
package glass.web.util;

import glass.SpringConfig;
import glass.annotation.Job;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scans for classes which are @Job annotated.
 *
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67589 $
 */
@Component
public class JobPathScanner {

    List<String> jobPaths = new ArrayList<String>();

    public List<String> getJobsPaths() {
        return jobPaths;
    }

    @PostConstruct
    protected void scanPaths() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Job.class));

        for (BeanDefinition definition : provider.findCandidateComponents(SpringConfig.class.getPackage().getName())) {
            jobPaths.add(definition.getBeanClassName());
        }

        Collections.sort(jobPaths);
    }
}
