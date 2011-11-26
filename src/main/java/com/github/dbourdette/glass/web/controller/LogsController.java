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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.log.execution.JobExecutions;
import com.github.dbourdette.glass.log.joblog.JobLogs;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
@Controller
public class LogsController {
    public static final int PAGE_SIZE = 100;

    @Inject
    protected JobExecutions executions;

    @Inject
    protected JobLogs jobLogs;

    @Inject
    protected Configuration configuration;

    @RequestMapping("/logs")
    public String logs(@RequestParam(defaultValue = "0") int index, Model model) {
        model.addAttribute("page", executions.find(Query.oneBasedIndex(index)));

        return "logs";
    }

    @RequestMapping("/logs/{result}")
    public String logs(@PathVariable String result, @RequestParam(defaultValue = "0") int index, Model model) {
        Query query = Query.oneBasedIndex(index).withResult(result);

        model.addAttribute("page", executions.find(query));

        return "logs";
    }

    @RequestMapping("/logs/{jobGroup}/{jobName}")
    public String logs(@PathVariable String jobGroup, @PathVariable String jobName, @RequestParam(defaultValue = "1") int index, Model model) {
        model.addAttribute("page", executions.find(jobGroup, jobName, Query.oneBasedIndex(index)));

        return "logs";
    }

    /**
     * Used for details popup from log list
     */
    @RequestMapping("/traces/{executionId}")
    public String traces(@PathVariable Long executionId, @RequestParam(defaultValue = "1") Integer index, Model model) {
        model.addAttribute("page", jobLogs.getLogs(executionId, Query.oneBasedIndex(index).withSize(PAGE_SIZE)));

        return "traces";
    }

    @RequestMapping("/logs/clear")
    public String clear(@RequestParam(defaultValue = "0") int index, Model model) {
        JobLogs.clear();
        executions.clear();

        return "redirect:" + configuration.getRoot() + "/logs";
    }
}
