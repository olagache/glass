package glass.log;

import glass.history.HistoryLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Service
public class Logs {
    private List<Log> logs = new ArrayList<Log>();

    private static final int MAX_SIZE = 10000;

    private static final int PAGE_SIZE = 100;

    public synchronized void add(Log log) {
        logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }

    public synchronized List<HistoryLog> getLogs() {
        List<HistoryLog> page = null;

        if (logs.size() > PAGE_SIZE) {
            page = new ArrayList(logs.subList(0, PAGE_SIZE));
        } else {
            page = new ArrayList(logs.subList(0, logs.size()));
        }

        Collections.reverse(page);

        return page;
    }
}
