package glass.web.interceptor;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class AddToModelInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Scheduler quartzScheduler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("standby", quartzScheduler.isInStandbyMode());
        request.setAttribute("now", new Date());

        return true;
    }
}
