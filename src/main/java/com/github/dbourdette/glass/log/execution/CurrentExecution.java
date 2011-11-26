package com.github.dbourdette.glass.log.execution;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class CurrentExecution {
    private static ThreadLocal<Execution> threadExecution = new ThreadLocal<Execution>();

    public static Execution get() {
        return threadExecution.get();
    }

    public static void set(Execution execution) {
        threadExecution.set(execution);
    }

    public static void unset() {
        threadExecution.set(null);
    }
}
