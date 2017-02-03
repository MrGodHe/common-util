/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;


import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.SimpleJob;

/**
 * 默认实现的 Job ，通过 {@link JobContextBuilder} 构建 {@link JobContext} 。
 *
 * @author
 */
public class DefaultJob extends SimpleJob {
	
	private JobContextBuilder jobContextBuilder;
	
	/**
	 * 构造一个DefaultJob。
	 *
	 * @param name job的名称。
	 */
	public DefaultJob (String name) {
		super (name);
	}
	
	/**
	 * 设置 需要构建 Job上下文 的构建器。
	 *
	 * @param jobContextBuilder 需要构建 Job上下文 的构建器。
	 */
	public void setJobContextBuilder (JobContextBuilder jobContextBuilder) {
		this.jobContextBuilder = jobContextBuilder;
	}
	
	protected JobContext buildJobContext () {
		return this.jobContextBuilder.build (this);
	}
	
}
