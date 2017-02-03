/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;


import cn.pay.ebank.process.exception.ExceptionHandler;

import java.util.Map;

/**
 * 任务上下文执行帮助者。
 * 
 * @author
 * 
 */
public class JobContextHelper {
	
	/**
	 * 执行任务上下文。 在任务执行过程中如果任务被发送中断请求，那么应该相应中断抛出 {@link InterruptedJobException} 。
	 * @param context 需要执行的上下文。
	 * @throws InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws JobException 任务执行的错误。
	 * @throws Exception 从节点中抛出的异常或者其他异常时。
	 */
	public void execute(JobContext context) throws Exception {
		Job job = context.getJob();
		try {
			while (!context.isDone()) {
				context.invoke();
				if (context.isInterrupted()) {
					throw new InterruptedJobException("任务 '" + job.getName() + "' 由节点 '"
														+ context.getCurrentNode().getName()
														+ "' 中断。");
				}
			}
		} catch (Throwable t) {
			boolean isSuccess = false;
			for (Map.Entry<Class<Throwable>, ExceptionHandler> entry : job.getExceptionHandlers()
				.entrySet()) {
				if (entry.getKey().isInstance(t)) {
					entry.getValue().resolveException(context, t);
					isSuccess = true;
					break;
				}
			}
			if (!isSuccess) {
				if (t instanceof Exception) {
					throw (Exception) t;
				} else {
					throw new JobException("执行任务时发生异常。", t);
				}
			}
		}
	}
	
}
