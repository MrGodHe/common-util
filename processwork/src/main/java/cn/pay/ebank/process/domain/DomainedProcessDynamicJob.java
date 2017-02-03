/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.dynamic.DefaultDynamicJob;
import cn.pay.ebank.process.result.IResult;

/**
 * 专注处理领域对象的动态任务实现。
 *
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 * @since 1.1
 */
public class DomainedProcessDynamicJob<D extends IDomain, R extends IResult> extends DefaultDynamicJob {

	/**
	 * 构建一个NDomainedProcessDynamicJob 。
	 *
	 * @param name job的名称。
	 */
	public DomainedProcessDynamicJob (String name) {
		super (name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DomainedProcessJobContext<D, R> getJobContext () {
		return (DomainedProcessJobContext<D, R>) super.getJobContext ();
	}
	
}
