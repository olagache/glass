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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.log.execution.Executions;
import com.github.dbourdette.glass.job.annotation.JobBean;
import com.github.dbourdette.glass.job.util.JobDataMapUtils;
import com.github.dbourdette.glass.job.annotation.JobArgumentBean;
import com.github.dbourdette.glass.log.trace.Traces;
import com.github.dbourdette.glass.util.Query;
import com.github.dbourdette.glass.web.form.JobForm;
import com.github.dbourdette.glass.web.form.NewJobForm;
import com.github.dbourdette.glass.web.util.JobPathScanner;
import com.github.dbourdette.glass.web.util.TriggerWrapperForView;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * All currently defined jobs and services around form list.
 *
 */
@Controller
public class JobsController {

    @Inject
    protected Scheduler quartzScheduler;
    
    @Inject
    protected Configuration configuration;

    @Inject
    protected JobPathScanner jobPathScanner;

    @Inject
    protected Executions executions;

    @Inject
    protected Traces logs;

    @RequestMapping("/jobs")
    public String jobs(Model model) throws SchedulerException {
        List<JobDetail> jobs = new ArrayList<JobDetail>();

        List<String> groups = quartzScheduler.getJobGroupNames();
        Collections.sort(groups);

        for (String group : groups) {
            List<JobKey> jobKeys = new ArrayList<JobKey>();

            jobKeys.addAll(quartzScheduler.getJobKeys(groupEquals(group)));
            Collections.sort(jobKeys);

            for (JobKey jobKey : jobKeys) {
                jobs.add(quartzScheduler.getJobDetail(jobKey));
            }
        }

        model.addAttribute("jobs", jobs);

        return "jobs";
    }

    @RequestMapping("/jobs/{group}/{name}")
    public String job(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        model.addAttribute("job", job);
        model.addAttribute("jobBean", JobBean.fromClass(job.getJobClass()));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));
        model.addAttribute("dataMap", JobDataMapUtils.toProperties(job.getJobDataMap(), "\n"));

        List<? extends Trigger> triggers = quartzScheduler.getTriggersOfJob(job.getKey());

        model.addAttribute("triggers", TriggerWrapperForView.fromList(triggers, quartzScheduler));

        model.addAttribute("history", executions.find(group, name, Query.index(0).withSize(5)));

        return "job";
    }

    @RequestMapping("/jobs/new")
    public String createJob(Model model) throws SchedulerException {
        return form(model, new NewJobForm());
    }

    @RequestMapping(value = "/jobs/new", method = RequestMethod.POST)
    public String postCreateJob(@Valid @ModelAttribute("form") NewJobForm form, BindingResult bindingResult, Model model) throws SchedulerException {
        if (bindingResult.hasErrors()) {
            return form(model, form);
        }

        quartzScheduler.addJob(form.getJobDetails(), true);

        return "redirect:" + configuration.getRoot() + "/jobs/" + form.getGroup() + "/" + form.getName();
    }

    @RequestMapping("/jobs/{group}/{name}/edit")
    public String updateJob(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        return form(model, new JobForm(job), job.getJobClass());
    }

    @RequestMapping(value = "/jobs/{group}/{name}/edit", method = RequestMethod.POST)
    public String postUpdateJob(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") JobForm form, BindingResult bindingResult, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        if (bindingResult.hasErrors()) {
            return form(model, form, job.getJobClass());
        }

        quartzScheduler.addJob(form.getJobDetails(job), true);

        return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/delete")
    public String delete(@PathVariable String group, @PathVariable String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        quartzScheduler.deleteJob(job.getKey());

        return "redirect:" + configuration.getRoot() + "/jobs";
    }

    @RequestMapping("/jobs/{group}/{name}/fire")
    public String fire(@PathVariable String group, @PathVariable String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        quartzScheduler.triggerJob(job.getKey());

        return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
    }

    private String form(Model model, NewJobForm form) {
        List<Class<?>> jobClasses = new ArrayList<Class<?>>();

        for (String jobPath : jobPathScanner.getJobsPaths()) {
            try {
                jobClasses.add(Class.forName(jobPath));
            } catch (ClassNotFoundException e) {
                continue;
            }
        }

        model.addAttribute("jobClasses", jobClasses);
        model.addAttribute("jobBean", JobBean.fromClass(form.getClazz()));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(form.getClazz()));
        model.addAttribute("form", form);

        return "new_job_form";
    }

    private String form(Model model, JobForm form, Class<?> jobClass) {
        model.addAttribute("jobClass", jobClass);
        model.addAttribute("jobBean", JobBean.fromClass(jobClass));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(jobClass));
        model.addAttribute("form", form);

        return "job_form";
    }
}
