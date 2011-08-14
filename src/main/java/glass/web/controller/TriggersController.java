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

import glass.job.JobUtils;
import glass.web.form.CronTriggerForm;
import glass.web.form.NewCronTriggerForm;
import glass.web.form.NewSimpleTriggerForm;
import glass.web.form.SimpleTriggerForm;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.validation.Valid;
import java.text.ParseException;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Controller
public class TriggersController {
    @Inject
    protected Scheduler quartzScheduler;

    @RequestMapping("/jobs/{group}/{name}/triggers/new-cron")
    public String createCronTrigger(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        model.addAttribute("form", new NewCronTriggerForm(job));
        model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

        return "new_cron_trigger_form";
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/new-cron", method = RequestMethod.POST)
    public String postCreateCronTrigger(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") NewCronTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

            return "new_cron_trigger_form";
        }

        quartzScheduler.scheduleJob(form.getTrigger());

        return "redirect:/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/new-simple")
    public String createSimpleTrigger(@PathVariable String group, @PathVariable String name, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        model.addAttribute("form", new NewSimpleTriggerForm(job));
        model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

        return "new_simple_trigger_form";
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/new-simple", method = RequestMethod.POST)
    public String postCreateSimpleTrigger(@PathVariable String group, @PathVariable String name, @Valid @ModelAttribute("form") NewSimpleTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

            return "new_simple_trigger_form";
        }

        quartzScheduler.scheduleJob(form.getTrigger());

        return "redirect:/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit")
    public String edit(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, Model model) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));

        if (trigger == null) {
            return "redirect:/jobs/{group}/{name}";
        }

        model.addAttribute("trigger", trigger);
        model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

        if (trigger instanceof CronTrigger) {
            model.addAttribute("form", new CronTriggerForm(trigger));

            return "cron_trigger_form";
        } else {
            model.addAttribute("form", new SimpleTriggerForm(trigger));

            return "simple_trigger_form";
        }
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit-cron", method = RequestMethod.POST)
    public String postEditCron(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @Valid @ModelAttribute("form") CronTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));

        if (trigger == null) {
            return "redirect:/jobs/{group}/{name}";
        }

        if (result.hasErrors()) {
            model.addAttribute("trigger", trigger);
            model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

            return "cron_trigger_form";
        }

        quartzScheduler.rescheduleJob(trigger.getKey(), form.getTrigger(trigger));

        return "redirect:/jobs/{group}/{name}";
    }

    @RequestMapping(value = "/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/edit-simple", method = RequestMethod.POST)
    public String postEditSimple(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName, @Valid @ModelAttribute("form") SimpleTriggerForm form, BindingResult result, Model model) throws SchedulerException, ParseException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(triggerName, triggerGroup));

        if (trigger == null) {
            return "redirect:/jobs/{group}/{name}";
        }

        if (result.hasErrors()) {
            model.addAttribute("trigger", trigger);
            model.addAttribute("jobArguments", JobUtils.getJobArguments(job.getJobClass()));

            return "simple_trigger_form";
        }

        quartzScheduler.rescheduleJob(trigger.getKey(), form.getTrigger(trigger));

        return "redirect:/jobs/{group}/{name}";
    }

    @RequestMapping("/jobs/{group}/{name}/triggers/{triggerGroup}/{triggerName}/delete")
    public String delete(@PathVariable String group, @PathVariable String name, @PathVariable String triggerGroup, @PathVariable String triggerName) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/jobs";
        }

        quartzScheduler.unscheduleJob(new TriggerKey(triggerGroup, triggerName));

        return "redirect:/jobs/{group}/{name}";
    }
}
