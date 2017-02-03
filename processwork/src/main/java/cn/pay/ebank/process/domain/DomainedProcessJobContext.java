/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.DefaultJobContext;
import cn.pay.ebank.process.result.IResult;

import java.util.Map;

/**
 * 专注于领域对象处理的任务上下文。
 *
 * @author
 */
public class DomainedProcessJobContext<D extends IDomain, R extends IResult> extends DefaultJobContext {

	private D domain;

	private R result;

	/**
	 * 构建一个 NDomainedProcessJobContext 。
	 *
	 * @param root                Job节点树的根节点。
	 * @param projectGlobalParams 项目全局参数，如果没有则为 null 。
	 */
	public DomainedProcessJobContext (NodeWrapper root, Map<Object, Object> projectGlobalParams) {
		super (root, projectGlobalParams);
	}
	
	/**
	 * 得到 领域对象。
	 *
	 * @return 领域对象实例。
	 */
	public D getDomain () {
		return this.domain;
	}
	
	/**
	 * 得到 结果对象。
	 *
	 * @return 结果对象实例。
	 */
	public R getResult () {
		return this.result;
	}
	
	/**
	 * 设置 领域对象。
	 *
	 * @param domain 领域对象实例。
	 */
	public void setDomain (D domain) {
		this.domain = domain;
	}
	
	/**
	 * 设置 结果对象。
	 *
	 * @param result 结果对象实例。
	 */
	public void setResult (R result) {
		this.result = result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DomainedProcessJob<D, R> getJob () {
		return (DomainedProcessJob<D, R>) super.getJob ();
	}
	
}
