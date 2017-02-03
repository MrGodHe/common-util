/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 简单的实现了 {@link #start()} 方法的 Job ，该 Job 使用 {@link #getJobContext()} 得到 任务上下文 。
 *
 * @author
 */
public abstract class SimpleJob extends Job {
	

	protected JobContextHelper jobContextHelper = new JobContextHelper ();
	
	/**
	 * 构造一个SimpleJob。
	 *
	 * @param name job的名称。
	 */
	public SimpleJob (String name) {
		super (name);
	}
	
	public void start () throws Exception {
		this.logger.info ("任务'" + getName () + "'开始运行。");
		this.jobContextHelper.execute (getJobContext ());
	}
}
