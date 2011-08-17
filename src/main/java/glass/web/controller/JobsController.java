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

package glass.web.controller;

import glass.annotation.JobArgumentBean;
import glass.job.JobUtils;
import glass.web.form.JobForm;
import glass.web.form.NewJobForm;
import glass.web.util.JobPathScanner;
import glass.web.util.TriggerWrapperForJsp;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    protected JobPathScanner jobPathScanner;

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
            return "redirect:/jobs";
        }

        model.addAttribute("job", job);
        model.addAttribute("jobDescription", JobUtils.getJobDescription(job.getJobClass()));
        model.addAttribute("properties", JobUtils.buildProperties(job.getJobDataMap(), "\n"));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

        List<JobExecutionContext> runningJobs = quartzScheduler.getCurrentlyExecutingJobs();
        List<? extends Trigger> triggers = quartzScheduler.getTriggersOfJob(job.getKey());

        model.addAttribute("triggers", TriggerWrapperForJsp.fromList(triggers, runningJobs));

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

        return "redirect:/jobs/" + form.getGroup() + "/" + form.getName();
    }

    @RequestMapping("/jobs/{group}/{name}/edit")
    public String updateJob(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        return form(model, new JobForm(job), job.getJobClass());
    }

    @RequestMapping(value = "/jobs/{group}/{name}/edit", method = RequestMethod.POST)
    public String postUpdateJob(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") JobForm form, BindingResult bindingResult, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        if (bindingResult.hasErrors()) {
            return form(model, form, job.getJobClass());
        }

        quartzScheduler.addJob(form.getJobDetails(job), true);

        return "redirect:/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/delete")
    public String delete(@PathVariable String group, @PathVariable String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        quartzScheduler.deleteJob(job.getKey());

        return "redirect:/jobs";
    }

    @RequestMapping("/jobs/{group}/{name}/fire")
    public String fire(@PathVariable String group, @PathVariable String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        quartzScheduler.triggerJob(job.getKey());

        return "redirect:/jobs/{group}/{name}";
    }

    /**
     * Get all possible arguments about a job class.
     * Used as a js service from pages.
     */
    @RequestMapping("/jobs/arguments")
    @ResponseBody
    public List<JobArgumentBean> getArguments(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        try {
            return JobArgumentBean.fromClass(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return null;
        }

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
        model.addAttribute("jobDescription", JobUtils.getJobDescription(form.getClazz()));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(form.getClazz()));
        model.addAttribute("form", form);

        return "new_job_form";
    }

    private String form(Model model, JobForm form, Class<?> jobClass) {
        model.addAttribute("jobClass", jobClass);
        model.addAttribute("jobDescription", JobUtils.getJobDescription(jobClass));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(jobClass));
        model.addAttribute("form", form);

        return "job_form";
    }
}
