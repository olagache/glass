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

package org.glass.web.interceptor;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.glass.configuration.Configuration;
import org.glass.velocity.tools.FormatTool;
import org.glass.velocity.tools.UtilsTool;
import org.quartz.Scheduler;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author damien bourdette
 */
public class AddToModelInterceptor extends HandlerInterceptorAdapter {
    @Inject
    private Scheduler quartzScheduler;

    @Inject
    private Configuration configuration;

    private UtilsTool utilsTool = new UtilsTool();

    private FormatTool formatTool = new FormatTool();

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }

        if (StringUtils.startsWith(modelAndView.getViewName(), "redirect:")) {
            return;
        }

        ModelMap model =  modelAndView.getModelMap();

        model.addAttribute("standby", quartzScheduler.isInStandbyMode());
        model.addAttribute("root", configuration.getRoot());
        model.addAttribute("utils", utilsTool);
        model.addAttribute("format", formatTool);
    }
}
