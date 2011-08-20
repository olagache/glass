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

package org.glass.velocity;

import org.glass.Parameters;
import org.glass.SpringConfig;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class GlassJobFactory implements JobFactory {
    // TODO : make this configuragble through context params
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        Job job = createJob(bundle.getJobDetail());

        setProperties(bundle, job);

        return job;
    }

    private Job createJob(JobDetail jobDetail) throws SchedulerException {
        Class<? extends Job> jobClass = jobDetail.getJobClass();

        try {
            return (Job) jobClass.newInstance();
        } catch (Exception e) {
            throw new SchedulerException("Problem instantiating class '" + jobDetail.getJobClass().getName() + "'", e);
        }
    }

    private void setProperties(TriggerFiredBundle bundle, Job job) {
        MutablePropertyValues pvs = new MutablePropertyValues();

        pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
        pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());

        buildAccessor(job).setPropertyValues(pvs, true);
    }

    private DirectFieldAccessor buildAccessor(Job job) {
        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(job);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        CustomDateEditor customDateEditor = new CustomDateEditor(simpleDateFormat, true);
        directFieldAccessor.registerCustomEditor(Date.class, customDateEditor);

        return directFieldAccessor;
    }
}
