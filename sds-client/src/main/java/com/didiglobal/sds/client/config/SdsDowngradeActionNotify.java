package com.didiglobal.sds.client.config;

import com.didiglobal.sds.client.enums.DowngradeActionType;
import com.didiglobal.sds.client.listener.DowngradeActionListener;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.util.Date;
import java.util.concurrent.*;

/**
 * 降级通知类
 */
public class SdsDowngradeActionNotify {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private final static ExecutorService notifyPool = new ThreadPoolExecutor(10, 10, 1L,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "SdsDowngradeActionNotify");
                    thread.setDaemon(true);
                    return thread;
                }
            },
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    logger.warn("SdsDowngradeActionNotify notifyPool已经满了，只能将该降级Action时间丢弃。");
                }
            }
    );

    /**
     * 监听器列表
     */
    private final static CopyOnWriteArrayList<DowngradeActionListener> listeners = new CopyOnWriteArrayList<>();

    private SdsDowngradeActionNotify() {
    }

    /**
     * 添加一个降级动作的监听器
     *
     * @param downgradeActionListener
     * @return
     */
    public static boolean addDowngradeActionListener(DowngradeActionListener downgradeActionListener) {
        if (downgradeActionListener == null) {
            return false;
        }

        if (listeners.size() > 100) {
            logger.warn("SdsDowngradeActionNotify#addDowngradeActionListener 难道你在死循环调用这个方法？");
            return false;
        }

        return listeners.add(downgradeActionListener);
    }

    /**
     * 触发事件
     *
     * @param point
     * @param downgradeActionType
     * @param time
     */
    public static void notify(final String point, final DowngradeActionType downgradeActionType, final Date time) {
        try {
            notifyPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (DowngradeActionListener listener : listeners) {
                        try {
                            listener.downgradeAction(point, downgradeActionType, time);
                        } catch (Exception e) {
                            logger.warn("SdsDowngradeActionNotify#notify 监听器执行时产生了异常，downgradeActionType:" +
                                    downgradeActionType + ", time:" + time + ", point:" + point, e);
                        }
                    }
                }
            });

        } catch (Exception e) {
            logger.warn("SdsDowngradeActionNotify#notify 提交降级事件任务时产生了异常，downgradeActionType:" +
                    downgradeActionType + ", time:" + time + ", point:" + point, e);
        }
    }
}
