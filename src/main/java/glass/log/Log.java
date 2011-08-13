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
