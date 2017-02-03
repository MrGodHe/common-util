/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 对任务节点的骨干实现，继承该类可以简化实现 {@link Node}。
 *
 * @author
 */
public abstract class AbstractNode implements Node {
	
	private final String name;
	
	private String caption;
	
	/**
	 * 子类使用的构造方法。
	 *
	 * @param name 节点的名称。
	 */
	public AbstractNode (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public String getCaption () {
		return this.caption;
	}
	
	/**
	 * 设置标题。
	 *
	 * @param caption 标题。
	 */
	public void setCaption (String caption) {
		this.caption = caption;
	}
	
	/**
	 * 处理由 {@link #invoke(JobContext)} 发生的异常。
	 * <p/>
	 * 该方法仅仅重新抛出了异常 。
	 *
	 * @param context   任务上下文。
	 * @param exception 发生的异常。
	 * @throws Exception 重新抛出的异常。
	 */
	public void resolveException (JobContext context, Exception exception) throws Exception {
		throw exception;
	}
	
	@Override
	public String toString () {
		return getClass ().getSimpleName () + " [name=" + this.name + ", caption=" + this.caption + "]";
	}
	
}