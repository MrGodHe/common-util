/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic;

import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.JobContextHelper;
import cn.pay.ebank.process.dynamic.config.DynamicJobContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认实现的 Job ，通过 {@link DynamicJobContextBuilder} 构建 {@link JobContext} 。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 * 
 */
public class DefaultDynamicJob extends DynamicJob {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected JobContextHelper jobContextHelper = new JobContextHelper();
	
	private DynamicJobContextBuilder jobContextBuilder;
	
	/**
	 * 构建一个 DefaultDynamicJob 。
	 * @param name job的名称。
	 */
	public DefaultDynamicJob(String name) {
		super(name);
	}
	
	/**
	 * 设置 需要构建 Job上下文 的构建器。
	 * @param jobContextBuilder 需要构建 Job上下文 的构建器。
	 */
	public void setJobContextBuilder(DynamicJobContextBuilder jobContextBuilder) {
		this.jobContextBuilder = jobContextBuilder;
	}
	
	protected JobContext buildJobContext() {
		return this.jobContextBuilder.build(this);
	}
	
	protected void lock() {
		this.jobContextBuilder.getDynamicJobFactory().refreshLock();
	}
	
	protected void unlock() {
		this.jobContextBuilder.getDynamicJobFactory().refreshUnlock();
	}
	
	protected void doStart() throws Exception {
		this.logger.info("任务'" + getName() + "'开始运行。");
		this.jobContextHelper.execute(getJobContext());
	}
	
}
