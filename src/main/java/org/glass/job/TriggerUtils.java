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

package org.glass.job;

import org.quartz.CronTrigger;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * @author damien bourdette
 */
public class TriggerUtils {
    public static String getPlanification(Trigger trigger) {
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;

            return cronTrigger.getCronExpression();
        }

        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;

        return getPlanification(simpleTrigger.getRepeatCount(), simpleTrigger.getRepeatInterval());
    }

    public static String getPlanification(int repeatCount, long repeatInterval) {
         String planification = "";

        if (repeatCount == -1) {
            planification += "repeat forever every ";
        } else if (repeatCount == 0) {
            planification += "execute once";

            return planification;
        } else if (repeatCount == 1) {
            planification += "repeat one time in ";
        } else {
            planification += "repeat " + repeatCount + " times every ";
        }

        planification += repeatInterval + "ms";

        return planification;
    }
}
