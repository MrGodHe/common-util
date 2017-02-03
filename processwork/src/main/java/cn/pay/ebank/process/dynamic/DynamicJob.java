/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic;

import cn.pay.ebank.process.InterruptedJobException;
import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.JobException;

/**
 * 动态Job。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public abstract class DynamicJob extends Job {
	
	/**
	 * 构建一个 DynamicJob 。
	 * @param name job的名称。
	 */
	public DynamicJob(String name) {
		super(name);
	}
	
	/**
	 * 执行锁定。
	 */
	protected abstract void lock();
	
	/**
	 * 执行完毕释放锁。
	 */
	protected abstract void unlock();
	
	public void start() throws Exception {
		lock();
		try {
			doStart();
		} finally {
			unlock();
		}
	}
	
	/**
	 * 由 {@link #start()} 委托调用的执行方法，执行当前任务。
	 * <p>
	 * 在任务执行过程中如果任务被发送中断请求，那么应该相应中断抛出 {@link InterruptedJobException} 。
	 * @throws InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws JobException 任务执行的错误。
	 * @throws Exception 从节点中抛出的异常或者其他异常时。
	 */
	protected abstract void doStart() throws Exception;
	
}
