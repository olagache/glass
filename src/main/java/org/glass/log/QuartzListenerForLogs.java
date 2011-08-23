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

package org.glass.log;

import javax.inject.Inject;

import org.quartz.SchedulerException;
import org.quartz.listeners.SchedulerListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author damien bourdette
 */
@Component
public class QuartzListenerForLogs extends SchedulerListenerSupport {
    @Inject
    private Logs logs;

    @Override
    public void schedulerError(String message, SchedulerException cause) {
        logs.add(Log.exception(message, cause));
    }
}
