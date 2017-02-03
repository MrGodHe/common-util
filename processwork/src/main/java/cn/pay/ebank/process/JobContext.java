/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;


import cn.pay.ebank.process.common.ParameterHolderSupport;

import java.util.Map;

/**
 * 任务上下文，包含了任务执行的所有信息。
 * <p/>
 * 该上下文是有状态的，不能将该上下文用于一个以上的Job中。
 *
 * @author
 */
public abstract class JobContext extends ParameterHolderSupport {
	
	private boolean done;
	
	/**
	 * 代表当前Job是否中断的标志，如果为true表示已经中断
	 */
	protected boolean interrupted;
	
	/**
	 * 当前正在处理的节点
	 */
	protected Node currentNode;
	
	private Job job;
	
	private final Map<Object, Object> projectGlobalParams;
	
	/**
	 * 子类使用的构造方法。
	 *
	 * @param projectGlobalParams 项目全局参数， 如果没有则为 null 。
	 */
	public JobContext (Map<Object, Object> projectGlobalParams) {
		this.projectGlobalParams = projectGlobalParams;
	}
	
	/**
	 * 中断当前任务。
	 * <p/>
	 * 该操作会将 {@link #interrupted} 设置为 true 并调用 {@link #doInterrupt(boolean)} 。
	 */
	public void interrupt () {
		doInterrupt (this.interrupted);
		this.interrupted = true;
	}
	
	/**
	 * 得到 项目全局参数。
	 *
	 * @return 项目全局参数，如果没有则为 null 。
	 */
	public Map<Object, Object> getProjectGlobalParams () {
		return this.projectGlobalParams;
	}
	
	/**
	 * 标识下次调用 {@link #invoke()} 时跳转到下一级name指定的节点。
	 *
	 * @param name   指定的跳到下一级节点的name。
	 * @param isSave 如果为true，则标识当前的节点需要保存直到调用 {@link #back()} 返回到保存的该节点。
	 */
	public abstract void next (String name, boolean isSave);
	
	/**
	 * 标识下次调用 {@link #invoke()} 时跳转到下一级name指定的节点。
	 * <p/>
	 * 该方法不会保存当前节点。
	 *
	 * @param name 指定的跳到下一级节点的name。
	 */
	public abstract void next (String name);
	
	/**
	 * 标识下次调用 {@link #invoke()} 时跳转到下一级的（第一个）节点。
	 * <p/>
	 * 该方法不会保存当前节点。
	 */
	public abstract void next ();
	
	/**
	 * 标识下次调用 {@link #invoke()} 时跳转到下一级的（第一个）节点。
	 *
	 * @param isSave 如果为true，则标识当前的节点需要保存直到调用 {@link #back()} 返回到保存的该节点。
	 */
	public abstract void next (boolean isSave);
	
	/**
	 * 标识下次调用 {@link #invoke()} 时返回到之前 {@link #next(String, boolean)}
	 * 保存的节点或者指定需要返回到的节点。
	 */
	public abstract void back ();
	
	/**
	 * 标识下次调用 {@link #invoke()} 时返回到之前 {@link #next(String, boolean)}
	 * 保存的节点或者指定需要返回到的节点的子节点。
	 *
	 * @param name 子节点的 name 。
	 */
	public abstract void back (String name);
	
	/**
	 * 标识下次调用 {@link #invoke()} 时返回到之前 {@link #next(String, boolean)}
	 * 保存的节点或者指定需要返回到的节点的子节点。
	 *
	 * @param name         子节点的 name 。
	 * @param isSaveIgnore 如果为 true 则保存的节点仍然保持被保存状态，可以再 back 到该节点。
	 */
	public abstract void back (String name, boolean isSaveIgnore);
	
	/**
	 * 判定该次操作后是否向下一级别移动。
	 *
	 * @return 如果是向下一级别移动返回 true 。
	 */
	public abstract boolean isNext ();
	
	/**
	 * 判定该次操作后是否向前移动。
	 *
	 * @return 如果是向前移动返回 true 。
	 */
	public abstract boolean isBack ();
	
	/**
	 * 根据当前上下文的状态，开始（或执行下一个）请求。
	 *
	 * @throws Exception 发生异常时。
	 */
	public abstract void invoke () throws Exception;
	
	/**
	 * 响应中断的逻辑，由 {@link #interrupt()} 调用。
	 *
	 * @param interrupted 当前的中断状态。
	 */
	protected abstract void doInterrupt (boolean interrupted);
	
	/**
	 * 判断当前Job是否被中断的。
	 *
	 * @return 如果当前Job已经被中断，则返回true。
	 */
	public boolean isInterrupted () {
		return this.interrupted;
	}
	
	/**
	 * 判断当前整个Job是否执行完成。
	 *
	 * @return 如果整个Job已经执行完成则返回true。
	 */
	public boolean isDone () {
		return this.done;
	}
	
	/**
	 * 设置整个Job是否完成的状态。
	 *
	 * @param isDone 如果为true，表示整个Job已经执行完成。
	 */
	public void setDone (boolean isDone) {
		this.done = isDone;
	}
	
	/**
	 * 得到当前上下文关联的Job。
	 *
	 * @return 当前上下文关联的Job。
	 */
	public Job getJob () {
		return this.job;
	}
	
	/**
	 * 设置当前上下文关联的Job。
	 *
	 * @param job 当前上下文关联的Job。
	 */
	public void setJob (Job job) {
		this.job = job;
	}
	
	/**
	 * 得到当前正在操作的节点。
	 *
	 * @return 当前正在操作的节点。
	 */
	public Node getCurrentNode () {
		return this.currentNode;
	}
	
	/**
	 * 设置当前正在操作的节点。
	 *
	 * @param currentNode 当前正在操作的节点。
	 */
	protected void setCurrentNode (Node currentNode) {
		this.currentNode = currentNode;
	}
	
}