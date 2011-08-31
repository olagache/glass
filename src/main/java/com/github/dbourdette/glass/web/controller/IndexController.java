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
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.github.dbourdette.glass.configuration.Configuration;
import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * The home page !
 */
@Controller
public class IndexController {

    @Inject
    protected Scheduler quartzScheduler;
    
    @Inject
    protected Configuration configuration;

    @RequestMapping({"/", "/index"})
    public String dashboard(Model model) throws SchedulerException {
        List<JobExecutionContext> runningJobs = quartzScheduler.getCurrentlyExecutingJobs();

        List<Trigger> pausedTriggers = new ArrayList<Trigger>();
        List<Trigger> hangedTriggers = new ArrayList<Trigger>();

        List<String> groups = quartzScheduler.getJobGroupNames();

        Collections.sort(groups);

        for (String group : groups) {
            List<JobKey> jobKeys = new ArrayList<JobKey>();
            jobKeys.addAll(quartzScheduler.getJobKeys(groupEquals(group)));

            Collections.sort(jobKeys);

            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = quartzScheduler.getTriggersOfJob(jobKey);

                for (Trigger trigger : triggers) {
                    if (isPaused(trigger)) {
                        pausedTriggers.add(trigger);
                    } else if (isHanged(trigger, runningJobs)) {
                        hangedTriggers.add(trigger);
                    }
                }
            }
        }

        model.addAttribute("runningJobs", runningJobs);
        model.addAttribute("pausedTriggers", pausedTriggers);
        model.addAttribute("hangedTriggers", hangedTriggers);

        return "dashboard";
    }

    @RequestMapping("/start")
    public String start() throws SchedulerException {
        quartzScheduler.start();

        return "redirect:" + configuration.getRoot() + "/";
    }

    @RequestMapping("/standby")
    public String standby() throws SchedulerException {
        quartzScheduler.standby();

        return "redirect:" + configuration.getRoot() + "/";
    }

    @RequestMapping("/restartTrigger")
    public String restartTrigger(String group, String name) throws SchedulerException {
        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(name, group));

        if (trigger == null) {
            return "redirect:" + configuration.getRoot() + "/";
        }

        trigger = trigger.getTriggerBuilder().startAt(new Date()).build();

        quartzScheduler.rescheduleJob(trigger.getKey(), trigger);

        return "redirect:" + configuration.getRoot() + "/";
    }

    @RequestMapping("/interrupt")
    public String interrupt(String group, String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:" + configuration.getRoot() + "/";
        }

        quartzScheduler.interrupt(job.getKey());

        return "redirect:" + configuration.getRoot() + "/";
    }

    private boolean isPaused(Trigger trigger) throws SchedulerException {
        return quartzScheduler.getTriggerState(trigger.getKey()) == TriggerState.PAUSED;
    }

    private boolean isHanged(Trigger trigger, List<JobExecutionContext> runningJobs) throws SchedulerException {
        Date nextFireTime = trigger.getNextFireTime();

        if (nextFireTime == null) {
            return false;
        }

        if (isRunning(trigger, runningJobs)) {
            return false;
        }

        Date oneMinuteAgo = new DateTime().minusMinutes(1).toDate();

        return nextFireTime.before(oneMinuteAgo);
    }

    private boolean isRunning(Trigger trigger, List<JobExecutionContext> runningJobs) {
        for (JobExecutionContext runningJob : runningJobs) {
            if (runningJob.getTrigger().getKey().equals(trigger.getKey())) {
                return true;
            }
        }

        return false;
    }

}
