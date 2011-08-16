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

package glass.web.util;

import glass.job.JobUtils;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class TriggerWrapperForJsp {
    private String group;

    private String name;

    private Date startTime;

    private Date endTime;

    private String cronExpression;

    private String properties;

    private Trigger trigger;

    private boolean running;

    public static List<TriggerWrapperForJsp> fromList(List<? extends Trigger> triggers, List<JobExecutionContext> runningJobs) {
        List<TriggerWrapperForJsp> wrappers = new ArrayList<TriggerWrapperForJsp>();

        for (Trigger trigger : triggers) {
            wrappers.add(fromTrigger(trigger, runningJobs));
        }

        return wrappers;
    }

    public static TriggerWrapperForJsp fromTrigger(Trigger trigger, List<JobExecutionContext> runningJobs) {
        TriggerWrapperForJsp wrapper = new TriggerWrapperForJsp();

        wrapper.trigger = trigger;
        wrapper.group = trigger.getKey().getGroup();
        wrapper.name = trigger.getKey().getName();
        wrapper.startTime = trigger.getStartTime();
        wrapper.endTime = trigger.getEndTime();
        wrapper.properties = JobUtils.buildProperties(trigger.getJobDataMap(), "\n");

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
        if (trigger instanceof CronTrigger) {
            return cronExpression;
        }

        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;

        String planification = "";

        if (simpleTrigger.getRepeatCount() == -1) {
            planification += "forever every ";
        } else {
            planification += simpleTrigger.getRepeatCount() + " times every ";
        }

        planification += simpleTrigger.getRepeatInterval() + "ms";

        return planification;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getProperties() {
        return properties;
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

    public void setRunning(boolean running) {
        this.running = running;
    }
}
