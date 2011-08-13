/**
 * $URL: https://scm.eng.rtl.fr/svn/dev/rtlnet/webportals/glass.rtl.fr/engine/trunk/src/main/java/fr/rtlgroup/rtlnet/webportals/rtl/glass/web/controller/ChronosController.java $
 *
 * $LastChangedBy: damien.bourdette $ - $LastChangedDate: 2011-08-12 16:25:56 +0200 (ven., 12 août 2011) $
 */
package glass.web.controller;

import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * The home page !
 *
 * @author RTL Group DTIT software development team (last changed by $LastChangedBy: damien.bourdette $)
 * @version $Revision: 67594 $
 */
@Controller
public class ChronosController {

    @Inject
    protected Scheduler quartzScheduler;

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
                JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);

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

        return "redirect:/";
    }

    @RequestMapping("/standby")
    public String standby() throws SchedulerException {
        quartzScheduler.standby();

        return "redirect:/";
    }

    @RequestMapping("/restartTrigger")
    public String restartTrigger(String group, String name) throws SchedulerException {
        Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(name, group));

        if (trigger == null) {
            return "redirect:/";
        }

        trigger = trigger.getTriggerBuilder().startAt(new Date()).build();

        quartzScheduler.rescheduleJob(trigger.getKey(), trigger);

        return "redirect:/";
    }

    @RequestMapping("/interrupt")
    public String interrupt(String group, String name) throws SchedulerException {
        JobDetail job = quartzScheduler.getJobDetail(new JobKey(name, group));

        if (job == null) {
            return "redirect:/";
        }

        quartzScheduler.interrupt(job.getKey());

        return "redirect:/";
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
