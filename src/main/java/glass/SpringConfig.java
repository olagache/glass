/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/SpringConfig.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-12 17:34:28 +0200 (ven., 12 août 2011) $
 */
package glass;

import glass.history.QuartzListener;
import org.quartz.Scheduler;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Locale;
import java.util.Properties;

/**
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67598 $
 */
@Configuration
@EnableWebMvc
public class SpringConfig {

    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";

    @Inject
    private QuartzListener quartzListener;

    @Bean
    public DataSource dataSource() throws Exception {
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();

        factoryBean.setJndiName("java:comp/env/jdbc/webportalsds");

        factoryBean.afterPropertiesSet();

        return (DataSource) factoryBean.getObject();
    }

    @Bean
    public Scheduler quartzScheduler(ApplicationContext context) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setApplicationContext(context);
        factory.setDataSource(dataSource());
        factory.setExposeSchedulerInRepository(true);
        factory.setApplicationContextSchedulerContextKey(APPLICATION_CONTEXT_KEY);

        // use our own copy of SpringBeanJobFactory until it is fix in spring 3.1.0 RC1 for quartz 2.0
        factory.setJobFactory(new glass.spring.SpringBeanJobFactory());

        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
        properties.setProperty("org.quartz.threadPool.threadCount", "15");
        properties.setProperty("org.quartz.threadPool.threadPriority", "4");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "chronos_");
        properties.setProperty("org.quartz.jobStore.isClustered", "false");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", OracleDelegate.class.getName());

        factory.setQuartzProperties(properties);

        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getObject();

        scheduler.getListenerManager().addJobListener(quartzListener);

        scheduler.start();

        return scheduler;
    }

    @Bean
    public FixedLocaleResolver fixedLocaleResolver() {
        FixedLocaleResolver resolver = new FixedLocaleResolver();
        resolver.setDefaultLocale(Locale.FRANCE);
        return resolver;
    }

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
