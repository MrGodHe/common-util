/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.domain;

import cn.pay.ebank.process.InterruptedJobException;
import cn.pay.ebank.process.JobException;
import cn.pay.ebank.process.JobUtils;
import cn.pay.ebank.process.common.Immutable;
import cn.pay.ebank.process.config.JobFactory;
import cn.pay.ebank.process.result.IResult;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理领域对象的任务工具。
 *
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 * @since 1.1
 */
public abstract class DomainedJobUtils extends JobUtils {

	/**
	 * 执行任务，次数为 domains 的数量。
	 * <p/>
	 * 执行任务时使用 domains 中的 {@link IDomain} 作为执行的领域对象。
	 * <p/>
	 * 如果 jobFactory 为 null ，则返回的执行结果列表的 {@link IResult} 都为 null 。
	 * <p/>
	 * 如果执行其中一次任务失败，则停止继续执行抛出异常。
	 *
	 * @param jobFactory 任务的工厂。
	 * @param domains    领域对象组。
	 * @return 执行的结果的列表，和领域对象组的下标一一对应。
	 * @throws com.yjf.process.InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws com.yjf.process.JobException            由 {@link com.yjf.process.Job#start()} 抛出的 {@link com.yjf.process.JobException} 。
	 * @throws RuntimeException                        发生其他异常时，通过 {@link RuntimeException#getCause()}
	 *                                                 得到发生的真实异常。
	 * @see com.yjf.process.config.JobFactory#getJob()
	 * @see com.yjf.process.Job#start()
	 */
	@Immutable
	public static <D extends IDomain, R extends IResult> List<R> executes (JobFactory<? extends DomainedProcessJob<D, R>> jobFactory, D... domains) {
		boolean isEmptyDomains = ArrayUtils.isEmpty (domains);
		if (jobFactory == null) {
			return isEmptyDomains ? Collections.<R>emptyList () : Collections.<R>nCopies (domains.length, null);
		}
		List<R> results = isEmptyDomains ? new ArrayList<R> (1) : new ArrayList<R> (domains.length);
		if (!isEmptyDomains) {
			for (D d : domains) {
				if (d != null) {
					try {
						DomainedProcessJob<D, R> job = jobFactory.getJob ();
						job.getJobContext ().setDomain (d);
						job.start ();
						results.add (job.getJobContext ().getResult ());
					} catch (InterruptedJobException e) {
						throw e;
					} catch (JobException e) {
						throw e;
					} catch (Exception e) {
						throw new RuntimeException (e);
					}
				} else {
					execute (jobFactory, results);
				}
			}
		} else {
			execute (jobFactory, results);
		}
		return results;
	}

	private static <R extends IResult> void execute (JobFactory<? extends DomainedProcessJob<?, R>> jobFactory, List<R> results) {
		try {
			DomainedProcessJob<?, R> job = jobFactory.getJob ();
			job.start ();
			results.add (job.getJobContext ().getResult ());
		} catch (InterruptedJobException e) {
			throw e;
		} catch (JobException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}

	/**
	 * 执行任务，次数为 count 的数量。
	 * <p/>
	 * 如果 jobFactory 为 null ，则返回的执行结果列表的 {@link com.yjf.common.lang.result.ResultInfo} 都为 null 。
	 * <p/>
	 * 如果执行其中一次任务失败，则停止继续执行抛出异常。
	 *
	 * @param jobFactory 任务的工厂。
	 * @param count      执行次数。
	 * @return 执行的结果的列表。
	 * @throws com.yjf.process.InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws com.yjf.process.JobException            由 {@link com.yjf.process.Job#start()} 抛出的 {@link com.yjf.process.JobException} 。
	 * @throws RuntimeException                        发生其他异常时，通过 {@link RuntimeException#getCause()}
	 *                                                 得到发生的真实异常。
	 * @see com.yjf.process.config.JobFactory#getJob()
	 * @see com.yjf.process.Job#start()
	 */
	@Immutable
	public static <R extends IResult> List<R> executes (JobFactory<? extends DomainedProcessJob<?, R>> jobFactory, int count) {
		Assert.isTrue (count >= 0, "{count} 不能小于 '0' 。");
		if (jobFactory == null) {
			return count == 0 ? Collections.<R>emptyList () : Collections.<R>nCopies (count, null);
		}
		if (count == 0) {
			return Collections.emptyList ();
		}
		List<R> results = new ArrayList<R> (count);
		for (int i = 0; i < count; i++) {
			execute (jobFactory, results);
		}
		return results;
	}

	/**
	 * 执行任务。
	 *
	 * @param jobFactory 任务的工厂。
	 * @param domain     任务所用的领域对象。
	 * @return 任务执行的结果。
	 * @throws com.yjf.process.InterruptedJobException 如果当前任务在执行过程中被中断。
	 * @throws com.yjf.process.JobException            由 {@link com.yjf.process.Job#start()} 抛出的 {@link com.yjf.process.JobException} 。
	 * @throws RuntimeException                        发生其他异常时，通过 {@link RuntimeException#getCause()}
	 *                                                 得到发生的真实异常。
	 * @see com.yjf.process.config.JobFactory#getJob()
	 * @see com.yjf.process.Job#start()
	 */
	public static <D extends IDomain, R extends IResult> R execute (JobFactory<? extends DomainedProcessJob<D, R>> jobFactory, D domain) {
		@SuppressWarnings("unchecked") List<R> results = executes (jobFactory, domain);
		return results.size () == 0 ? null : results.get (0);
	}
	
}
