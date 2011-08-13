/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/test/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/web/util/JobPathScannerTest.java $
 *
 * $LastChangedBy: olivier.lagache $ - $LastChangedDate: 2011-07-12 11:04:19 +0200 (mar., 12 juil. 2011) $
 */
package glass.web.util;

import glass.job.DummyJob;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

/**
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: olivier.lagache $)
 * @version $Revision: 66819 $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/glass/spring-context.xml" })
public class JobPathScannerTest {

    @Inject
    private JobPathScanner jobPathScanner;

    @Test
    public final void testGetJobsPaths() {
        List<String> jobs = jobPathScanner.getJobsPaths();

        Assert.assertNotNull(jobs);
        Assert.assertTrue("DummyJob must be in the list", jobs.contains(DummyJob.class.getName()));
    }

}
