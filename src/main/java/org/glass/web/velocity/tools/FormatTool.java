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

package org.glass.web.velocity.tools;

import org.apache.commons.lang.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class FormatTool {
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public String date(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(date);
    }

    public String html(String text) {
        return StringEscapeUtils.escapeHtml(text);
    }
}
