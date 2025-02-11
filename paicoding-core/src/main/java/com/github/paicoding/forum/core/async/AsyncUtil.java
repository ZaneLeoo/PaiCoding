package com.github.paicoding.forum.core.async;

import java.util.concurrent.*;

public class AsyncUtil {
    private static ExecutorService executorService;

    static {
        // 初始化线程池，核心线程数为 CPU 核数 * 2，最大线程数为 50
        initExecutorService(Runtime.getRuntime().availableProcessors() * 2, 50);
    }

    public static void initExecutorService(int core, int max) {
        max = Math.max(core, max);
        executorService = new ThreadPoolExecutor(
                core,
                max,
                0L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 执行一个异步任务
     *
     * @param call 任务
     */
    public static void execute(Runnable call) {
        executorService.execute(call);
    }

    /**
     * 提交一个异步任务并返回 Future
     *
     * @param t 任务
     * @param <T> 返回类型
     * @return Future 对象
     */
    public static <T> Future<T> submit(Callable<T> t) {
        return executorService.submit(t);
    }

    /**
     * 带超时限制的异步调用
     *
     * @param time 超时时间
     * @param unit 时间单位
     * @param call 任务
     * @param <T> 返回类型
     * @return 返回结果
     * @throws ExecutionException 执行异常
     * @throws InterruptedException 中断异常
     * @throws TimeoutException 超时异常
     */
    public static <T> T callWithTimeLimit(long time, TimeUnit unit, Callable<T> call)
            throws ExecutionException, InterruptedException, TimeoutException {
        Future<T> future = submit(call);
        return future.get(time, unit);
    }

    /**
     * 睡眠方法
     *
     * @param millis 睡眠时间
     * @return 是否成功
     */
    public static boolean sleep(long millis) {
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }
}
