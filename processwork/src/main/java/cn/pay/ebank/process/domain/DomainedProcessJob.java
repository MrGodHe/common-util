/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.config.DefaultJob;
import cn.pay.ebank.process.result.IResult;

/**
 * 专注处理领域对象的任务实现。
 *
 * @author
 * @since 1.1
 */
public class DomainedProcessJob<D extends IDomain, R extends IResult> extends DefaultJob {

	/**
	 * 构建一个NDomainedProcessJob 。
	 *
	 * @param name job的名称。
	 */
	public DomainedProcessJob (String name) {
		super (name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DomainedProcessJobContext<D, R> getJobContext () {
		return (DomainedProcessJobContext<D, R>) super.getJobContext ();
	}
	
}
