package glass.history;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Service
public class History {
    private List<HistoryLog> logs = new ArrayList<HistoryLog>();

    private static final int MAX_SIZE = 1000;

    private static final int PAGE_SIZE = 100;

    public synchronized void jobStarts(JobExecutionContext context) {
        addLog(HistoryLog.onStart(context));
    }

    public synchronized void jobEnds(JobExecutionContext context, JobExecutionException exception) {
        addLog(HistoryLog.onEnd(context, exception));
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

    private void addLog(HistoryLog log) {
       logs.add(log);

        if (logs.size() > MAX_SIZE) {
            logs.remove(0);
        }
    }
}
