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

package com.github.dbourdette.glass.web.controller;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dbourdette.glass.job.annotation.JobBean;
import com.github.dbourdette.glass.log.Logs;
import com.github.dbourdette.glass.log.log.Log;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
@Controller
public class JsServiceController {
    @Inject
    private Logs logs;

    /**
     * Gets job description for a job class.
     * Used as a js service from pages.
     */
    @RequestMapping("/jsapi/jobs/description")
    @ResponseBody
    public JobBean description(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }

        try {
            return JobBean.fromClass(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @RequestMapping("/jsapi/traces")
    @ResponseBody
    public Page<Log> traces(@RequestParam Long executionId, @RequestParam(defaultValue = "1") Integer page) {
        return logs.getLogs(executionId, Query.oneBasedIndex(page).withSize(LogsController.PAGE_SIZE));
    }
}
