/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package glass.web.interceptor;

import glass.web.velocity.tools.UtilsTool;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
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

    private UtilsTool utilsTool = new UtilsTool();

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        ModelMap model =  modelAndView.getModelMap();

        model.addAttribute("standby", quartzScheduler.isInStandbyMode());
        model.addAttribute("now", new Date());
        model.addAttribute("utils", utilsTool);
    }
}
