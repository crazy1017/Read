package com.mzp.libreads.common.proxy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProxy {
	
	public ThreadPoolExecutor poolExecutor;
	
	private int mCorePoolSize;//核心池大小
	private int mMaximumPoolSize;//线程最大线程数
	
	public ThreadPoolProxy(int mCorePoolSize,int mMaximumPoolSize) {
		this.mCorePoolSize = mCorePoolSize;
		this.mMaximumPoolSize = mMaximumPoolSize;
	}
	
	public void initThreadPoolExecutor(){
		if (poolExecutor == null || poolExecutor.isShutdown() || poolExecutor.isTerminated()) {
			synchronized (ThreadPoolProxy.class) {
				if (poolExecutor == null || poolExecutor.isShutdown() || poolExecutor.isTerminated()) {
					long keepAliveTime = 0;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
					poolExecutor =  new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
				}
			}
		}
	}
	
	/**
     * 提交任务
     */
    public Future submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> future = poolExecutor.submit(task);
        return future;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        poolExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        poolExecutor.remove(task);
    }
}
