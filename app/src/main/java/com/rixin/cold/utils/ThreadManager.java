package com.rixin.cold.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * 线程管理器
 *
 * Created by 飘渺云轩 on 2017/2/5.
 */
public class ThreadManager {

	private static ThreadPool threadPool;

	private ThreadManager() {
	}

	public static ThreadPool getThreadPool() {
		if (threadPool == null) {
			synchronized (ThreadManager.class) {
				if (threadPool == null) {
					/** 根据手机cpu数量创建最大线程数 */
					int cpuCount = Runtime.getRuntime().availableProcessors(); //获取cpu数量
					int threadCount = cpuCount * 2 + 1;
					
					/** 固定最大线程数 */
//					int threadCount = 10;
					threadPool = new ThreadPool(threadCount, threadCount, 1L);
				}
			}
		}
		return threadPool;
	}

	/** 线程池 */
	public static class ThreadPool {

		private int corePoolSize; // 核心线程数
		private int maximumPoolSize; // 最大线程数
		private long keepAliveTime; // 休息时间
		private ThreadPoolExecutor executor;

		private ThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		public void execute(Runnable r) {
			if (executor == null) {
				/**
				 * 参数1：核心线程数 
				 * 参数2：最大线程数 
				 * 参数3：线程休息时间 
				 * 参数4：时间单位 
				 * 参数5：线程队列 
				 * 参数6：生成线程的工厂
				 * 参数7：线程异常处理策略
				 */
				executor = new ThreadPoolExecutor(corePoolSize,
						maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
						new LinkedBlockingQueue<Runnable>(),
						Executors.defaultThreadFactory(), new AbortPolicy());
			}
			
			//执行
			executor.execute(r);
		}
		
		public void cancal(Runnable r) {
			//移除任务队列中的任务，不包括正在执行的任务
			executor.getQueue().remove(r);
		}

	}

}
