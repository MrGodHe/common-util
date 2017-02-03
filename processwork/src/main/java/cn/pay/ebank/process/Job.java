/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;


import cn.pay.ebank.process.common.Captioned;
import cn.pay.ebank.process.common.Named;
import cn.pay.ebank.process.exception.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * 该类的实现代表着一个任务，一个任务是多个操作节点的管理者，由Job和JobContext负责调度其中的操作节点。
 *
 * @author
 */
public abstract class Job implements Named, Captioned {
	protected final Logger logger = LoggerFactory.getLogger (getClass ());
	private final String name;
	
	private String caption;
	/**
	 * Job的上下文
	 */
	private JobContext jobContext;
	
	private Map<Class<Throwable>, ExceptionHandler> exceptionHandlers;
	
	/**
	 * 构造一个Job。
	 *
	 * @param name job的名称。
	 */
	public Job (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public String getCaption () {
		return this.caption;
	}
	
	/**
	 * 设置该Job的标题。
	 *
	 * @param caption 该Job的标题。
	 */
	public void setCaption (String caption) {
		this.caption = caption;
	}
	
	/**
	 * 得到该Job的上下文。
	 *
	 * @return 该Job的上下文。
	 */
	public JobContext getJobContext () {
		return this.jobContext;
	}
	
	/**
	 * 设置异常处理器组。
	 *
	 * @param exceptionHandlers 异常处理器组。
	 */
	public final void setExceptionHandlers (Map<Class<Throwable>, ExceptionHandler> exceptionHandlers) {
		this.exceptionHandlers = Collections.unmodifiableMap (exceptionHandlers);
	}
	
	/**
	 * 得到异常处理器组。
	 *
	 * @return 异常处理器组。
	 */
	public final Map<Class<Throwable>, ExceptionHandler> getExceptionHandlers () {
		return this.exceptionHandlers;
	}
	
	/**
	 * 完成该Job的初始化或者重新初始化该Job。
	 *
	 * @throws Exception 如果发生异常。
	 */
	public void initOrReInt () throws Exception {
		this.jobContext = buildJobContext ();
	}
	
	/**
	 * 由 {@link #initOrReInt()} 调用的构造 {@link JobContext} 的方法。
	 *
	 * @return 构建的 JobContext 。
	 */
	protected abstract JobContext buildJobContext ();
	
	/**
	 * 启动当前任务。
	 * <p/>
	 * 在任务执行过程中如果任务被发送中断请求，那么应该相应中断抛出 {@link InterruptedJobException} 。
	 *
	 * @throws InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws JobException            任务执行的错误。
	 * @throws Exception               从节点中抛出的异常或者其他异常时。
	 */
	public abstract void start () throws Exception;
	
}
