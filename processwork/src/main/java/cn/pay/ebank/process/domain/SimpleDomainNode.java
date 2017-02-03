/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;


import cn.pay.ebank.process.common.LRUCache;
import cn.pay.ebank.process.result.IResult;


/**
 * 简单的流程节点基类
 */
public abstract class SimpleDomainNode<D extends IDomain, R extends IResult> extends DomainNode<D, R> {

	/**
	 * 其他地方已经使用到了ThreadLocal，可能导致被清空的情况
	 */
	private static LRUCache<Thread, DomainedProcessJobContext> processJobContextLocal = new LRUCache<Thread, DomainedProcessJobContext> (10000);

	public SimpleDomainNode (String name) {
		super (name);
	}


	private DomainedProcessJobContext<D, R> get () {
		return processJobContextLocal.get (Thread.currentThread ());
	}

	private void set (DomainedProcessJobContext<D, R> jobContext) {
		processJobContextLocal.put (Thread.currentThread (), jobContext);
	}

	private void remove () {
		processJobContextLocal.remove (Thread.currentThread ());
	}

	protected DomainedProcessJobContext<D, R> getJobContext () {
		return get ();
	}

	protected D getDomain () {
		return getJobContext ().getDomain ();
	}

	protected R getResult () {
		return getJobContext ().getResult ();
	}

	protected void setResult (R result) {
		getJobContext ().setResult (result);
	}

	protected void setDone (boolean isDone) {
		getJobContext ().setDone (isDone);
	}

	/**
	 * 流程方法
	 *
	 * @param context
	 * @param domain
	 */
	protected final void process (DomainedProcessJobContext<D, R> context, D domain) {
		set (context);
		doJobProcess (domain);
		//释放资源
		remove ();
	}

	/**
	 * 执行Job处理流程
	 *
	 * @param domain
	 */
	protected abstract void doJobProcess (D domain);

	/**
	 * 创建一个Result实例
	 *
	 * @param resultClass
	 * @return
	 */
	protected R newResult (Class<R> resultClass) {
		try {
			return resultClass.newInstance ();
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
}
