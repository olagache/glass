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

package com.github.dbourdette.glass.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.github.dbourdette.glass.SpringConfig;

/**
 * @author damien bourdette
 */
public class FormatTool {
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(SpringConfig.DATE_FORMAT);

        return sdf.format(date);
    }

    public String date(Date date) {
        return formatDate(date);
    }

    public String datePattern() {
        return SpringConfig.DATE_FORMAT;
    }

    public String html(Object object) {
        if (object == null) {
            return null;
        }

        return StringEscapeUtils.escapeHtml(object.toString());
    }

    public String dataMap(Object object) {
        if (object == null) {
            return null;
        }

        String html = html(object);

        html = StringUtils.replace(html, "\n", "<br/>");

        return html;
    }
}
