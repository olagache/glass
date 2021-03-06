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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.job.annotation.JobArgumentBean;
import com.github.dbourdette.glass.web.form.CronTriggerForm;
import com.github.dbourdette.glass.web.form.NewCronTriggerForm;
import com.github.dbourdette.glass.web.form.NewSimpleTriggerForm;
import com.github.dbourdette.glass.web.form.SimpleTriggerForm;
import com.github.dbourdette.glass.web.form.TriggerForm;
import com.github.dbourdette.glass.web.util.JobAndTriggers;
import com.github.dbourdette.glass.web.util.TriggerWrapperForView;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * @author damien bourdette
 */
@Controller
public class TriggersController {
    @Inject
    protected Scheduler quartzScheduler;

    @Inject
    protected Configuration configuration;

    @RequestMapping("/triggers")
    public String all(Model model) throws SchedulerException {
        List<JobAndTriggers> jobsAndTriggers = new ArrayList<JobAndTriggers>();

        List<String> groups = quartzScheduler.getJobGroupNames();

        for (String group : groups) {
            List<JobKey> jobKeys = new ArrayList<JobKey>();
            jobKeys.addAll(quartzScheduler.getJobKeys(groupEquals(group)));

            Collections.sort(jobKeys);

            for (JobKey jobKey : jobKeys) {
                JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);

                JobAndTriggers jobAndTrigger = new JobAndTriggers();
                jobAndTrigger.setJobDetail(jobDetail);
                jobAndTrigger.setTriggers(TriggerWrapperForView.fromList(quartzScheduler.getTriggersOfJob(jobKey), quartzScheduler));

                jobsAndTriggers.add(jobAndTrigger);
            }
        }

        model.addAttribute("jobsAndTriggers", jobsAndTriggers);

        return "triggers";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/new-cron")
    public String createCronTrigger(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        model.addAttribute("form", new NewCronTriggerForm(job));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

        return "new_cron_trigger_form";
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/new-cron", method = RequestMethod.POST)
    public String postCreateCronTrigger(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") NewCronTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

            return "new_cron_trigger_form";
        }

        quartzScheduler.scheduleJob(form.getTrigger());

        return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/new-simple")
    public String createSimpleTrigger(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        model.addAttribute("form", new NewSimpleTriggerForm(job));
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

        return "new_simple_trigger_form";
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/new-simple", method = RequestMethod.POST)
    public String postCreateSimpleTrigger(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") NewSimpleTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

            return "new_simple_trigger_form";
        }

        quartzScheduler.scheduleJob(form.getTrigger());

        return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit")
    public String edit(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));

        if (trigger == null) {
            return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
        }

        model.addAttribute("trigger", trigger);
        model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

        if (trigger instanceof CronTrigger) {
            model.addAttribute("form", new CronTriggerForm(trigger));

            return "cron_trigger_form";
        } else {
            model.addAttribute("form", new SimpleTriggerForm(trigger));

            return "simple_trigger_form";
        }
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit-cron", method = RequestMethod.POST)
    public String postEditCronTrigger(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @Valid @ModelAttribute("form") CronTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        return postEditTrigger(group, name, triggerGroup, triggerName, form, model, result);
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit-simple", method = RequestMethod.POST)
    public String postEditSimpleTrigger(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @Valid @ModelAttribute("form") SimpleTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        return postEditTrigger(group, name, triggerGroup, triggerName, form, model, result);
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/delete")
    public String delete(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @RequestParam(required = false) String redirect) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        quartzScheduler.unscheduleJob(new TriggerKey(triggerName, triggerGroup));

        if (StringUtils.isNotEmpty(redirect)) {
            return "redirect:" + redirect;
        }

        return "redirect:/glass";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/pause")
    public String pause(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @RequestParam(required = false) String redirect) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        quartzScheduler.pauseTrigger(new TriggerKey(triggerName, triggerGroup));

        if (StringUtils.isNotEmpty(redirect)) {
            return "redirect:" + redirect;
        }

        return "redirect:/glass";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/resume")
    public String resume(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @RequestParam(required = false) String redirect) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        quartzScheduler.resumeTrigger(new TriggerKey(triggerName, triggerGroup));

        if (StringUtils.isNotEmpty(redirect)) {
            return "redirect:" + redirect;
        }

        return "redirect:/glass";
    }

    private String postEditTrigger(String group, String name, String triggerGroup, String triggerName, TriggerForm form, Model model, BindingResult result) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/jobs";
        }

        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));

        if (trigger == null) {
            return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
        }

        if (result.hasErrors()) {
            model.addAttribute("trigger", trigger);
            model.addAttribute("jobArguments", JobArgumentBean.fromClass(job.getJobClass()));

            if (trigger instanceof CronTrigger) {
                return "cron_trigger_form";
            } else {
                return "simple_trigger_form";
            }
        }

        quartzScheduler.rescheduleJob(trigger.getKey(), form.getTrigger(trigger));

        return "redirect:" + configuration.getRoot() + "/jobs/{group}/{name}";
    }
}
