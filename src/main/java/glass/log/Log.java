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

package glass.log;

import java.util.Date;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class Log {
    private Date date;

    private String message;

    private Log() {

    }

    public static Log info(String message) {
        Log log = new Log();

        log.date = new Date();
        log.message = message;

        return log;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
