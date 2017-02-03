/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhyang@ebank.pay.cn 2015-04-17 16:17 创建
 *
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.result.IResult;

/**
 * @author zhyang@ebank.pay.cn
 */
public abstract class SimpleConvertNode<D extends IDomain, R extends IResult> extends SimpleDomainNode<D, R> {
	public SimpleConvertNode (String name) {
		super (name);
	}


	@Override
	protected void doJobProcess (D domain) {
		getJobContext ().setResult (convert (domain));
		setDone (isDone (domain));
	}

	/**
	 * 处理领域对象业务逻辑。
	 *
	 * @param domain 需要处理的领域对象。
	 */
	protected abstract R convert (D domain);

	/**
	 * 是否已经完成节点
	 * @param domain
	 * @return
	 */
	protected boolean isDone (D domain) {
		return false;
	}
}
