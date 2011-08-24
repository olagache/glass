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

package org.glass.job.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;

public class JobDataMapUtils {

    public static String toProperties(JobDataMap jobDataMap, String separator) {
        String[] keys = jobDataMap.getKeys();

        StringBuilder stringBuilder = new StringBuilder();

        for (String key : keys) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(jobDataMap.getString(key));
            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    public static JobDataMap fromProperties(String dataMap) {
        if (StringUtils.isEmpty(dataMap)) {
            return new JobDataMap();
        }

        Properties props = new Properties();

        try {
            props.load(new StringReader(dataMap));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JobDataMap map = new JobDataMap();

        for (Object key : props.keySet()) {
            map.put(key, props.getProperty((String) key));
        }

        return map;
    }

}
