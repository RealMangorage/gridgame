package org.mangorage.mangonetwork.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Scheduler {
    public final static ScheduledThreadPoolExecutor RUNNER = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);
}