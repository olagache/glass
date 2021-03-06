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

package com.github.dbourdette.glass.web.form;

import java.util.Date;

import javax.validation.constraints.Future;

import com.github.dbourdette.glass.SpringConfig;
import com.github.dbourdette.glass.util.Dates;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Base class for trigger forms.
 *
 * @author damien bourdette
 */
public class TriggerFormSupport {
    @DateTimeFormat(pattern = SpringConfig.DATE_FORMAT)
    @Future
    protected Date startTime;

    @DateTimeFormat(pattern = SpringConfig.DATE_FORMAT)
    protected Date endTime;

    protected String dataMap;

    public Date getStartTime() {
        return Dates.copy(startTime);
    }

    public void setStartTime(Date startTime) {
        this.startTime = Dates.copy(startTime);
    }

    public Date getEndTime() {
        return Dates.copy(endTime);
    }

    public void setEndTime(Date endTime) {
        this.endTime = Dates.copy(endTime);
    }

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }
}
