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

package com.github.dbourdette.glass.web.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.github.dbourdette.glass.job.util.JobDataMapUtils;
import com.github.dbourdette.glass.job.util.TriggerUtils;
import com.github.dbourdette.glass.util.Dates;

/**
 * @author damien bourdette
 */
public class TriggerWrapperForView {
    private String group;

    private String name;

    private Date startTime;

    private Date endTime;

    private String cronExpression;

    private String dataMap;

    private Trigger trigger;

    private boolean running;

    private boolean paused;

    public static List<TriggerWrapperForView> fromList(List<? extends Trigger> triggers, Scheduler scheduler) throws SchedulerException {
        List<TriggerWrapperForView> wrappers = new ArrayList<TriggerWrapperForView>();

        for (Trigger trigger : triggers) {
            wrappers.add(fromTrigger(trigger, scheduler));
        }

        return wrappers;
    }

    public static TriggerWrapperForView fromTrigger(Trigger trigger, Scheduler scheduler) throws SchedulerException {
        List<JobExecutionContext> runningJobs = scheduler.getCurrentlyExecutingJobs();

        TriggerWrapperForView wrapper = new TriggerWrapperForView();

        wrapper.trigger = trigger;
        wrapper.group = trigger.getKey().getGroup();
        wrapper.name = trigger.getKey().getName();
        wrapper.startTime = trigger.getStartTime();
        wrapper.endTime = trigger.getEndTime();
        wrapper.paused = scheduler.getTriggerState(trigger.getKey()) == Trigger.TriggerState.PAUSED;
        wrapper.dataMap = JobDataMapUtils.toProperties(trigger.getJobDataMap(), "\n");

        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;

            wrapper.cronExpression = cronTrigger.getCronExpression();
        }

        for (JobExecutionContext executionContext : runningJobs) {
            if (executionContext.getTrigger().equals(trigger)) {
                wrapper.running = true;

                break;
            }
        }

        return wrapper;
    }

    public String getType() {
        return (trigger instanceof CronTrigger) ? "cron" : "simple";
    }

    public String getPlanification() {
        return TriggerUtils.getPlanification(trigger);
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return Dates.copy(startTime);
    }

    public Date getEndTime() {
        return Dates.copy(endTime);
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getDataMap() {
        return dataMap;
    }

    public Date getPreviousFireTime() {
        return trigger.getPreviousFireTime();
    }

    public Date getNextFireTime() {
        return trigger.getNextFireTime();
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }
}
