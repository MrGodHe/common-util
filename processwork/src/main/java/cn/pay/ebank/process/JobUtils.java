/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;


import cn.pay.ebank.process.config.JobFactory;

/**
 * Job 工具。
 */
public abstract class JobUtils {
	
	/**
	 * 执行一次任务。
	 *
	 * @param jobFactory 任务的工厂。
	 * @return 执行任务的上下文。如果 jobFactory 为 null ，则返回 null 。
	 * @throws InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws JobException            由 {@link Job#start()} 抛出的 {@link JobException} 。
	 * @throws RuntimeException        发生其他异常时，通过 {@link RuntimeException#getCause()}
	 *                                 得到发生的真实异常。
	 * @see JobFactory#getJob()
	 * @see Job#start()
	 */
	public static <J extends Job> JobContext execute (JobFactory<J> jobFactory) {
		if (jobFactory == null) {
			return null;
		}
		try {
			J job = jobFactory.getJob ();
			job.start ();
			return job.getJobContext ();
		} catch (InterruptedJobException e) {
			throw e;
		} catch (JobException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
}
