/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.AbstractNode;
import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.result.IResult;

/**
 * 领域处理节点。
 *
 * @author
 */
public abstract class DomainNode<D extends IDomain, R extends IResult> extends AbstractNode {

	/**
	 * 构建一个NDomainNode。
	 *
	 * @param name 节点名称。
	 */
	public DomainNode (String name) {
		super (name);
	}
	
	public final void invoke (JobContext context) throws Exception {
		@SuppressWarnings("unchecked") DomainedProcessJobContext<D, R> domainedProcessJobContext = (DomainedProcessJobContext<D, R>) context;
		process (domainedProcessJobContext, domainedProcessJobContext.getDomain ());
	}
	
	/**
	 * 处理领域对象业务逻辑。
	 *
	 * @param context 任务上下文。
	 * @param domain  需要处理的领域对象。
	 */
	protected abstract void process (DomainedProcessJobContext<D, R> context, D domain);
	
}
