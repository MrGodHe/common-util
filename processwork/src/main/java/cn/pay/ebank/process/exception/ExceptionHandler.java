/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.exception;


import cn.pay.ebank.process.JobContext;

/**
 * 异常处理程序，用于处理任何由 Job（及其内部） 产生的异常/错误。
 * 
 * @author
 * 
 */
public interface ExceptionHandler {
	
	/**
	 * 处理异常/错误。
	 * @param context 任务上下文。
	 * @param throwable 需要处理的异常/错误。
	 */
	void resolveException (JobContext context, Throwable throwable);
	
}
