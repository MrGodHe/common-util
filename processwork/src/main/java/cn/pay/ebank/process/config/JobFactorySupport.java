/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.common.ParameterHolderSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 实现 JobFactory 的支持类，继承该类可减少实现工作。
 *
 * @author
 */
public abstract class JobFactorySupport<J extends Job> extends ParameterHolderSupport implements JobFactory<Job>, FactoryBean<Job>, ApplicationContextAware {
	
	protected ApplicationContext applicationContext;
	
	protected String jobName;
	
	protected String jobClassName;
	
	protected String jobRefName;
	
	protected ClassLoader classLoader;
	
	protected String jobContextClassName;
	
	protected String projectGlobalParamsRefName;
	
	public void setApplicationContext (ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void setJobName (String jobName) {
		this.jobName = jobName;
	}
	
	public void setJobClassName (String jobClassName) {
		this.jobClassName = jobClassName;
	}
	
	public void setJobRefName (String jobRefName) {
		this.jobRefName = jobRefName;
	}
	
	public void setClassLoader (ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public void setJobContextClassName (String jobContextClassName) {
		this.jobContextClassName = jobContextClassName;
	}
	
	public void setProjectGlobalParamsRefName (String projectGlobalParamsRefName) {
		this.projectGlobalParamsRefName = projectGlobalParamsRefName;
	}
	
	/**
	 * 创建 Job 。
	 * <p/>
	 * 如果 {@link #jobRefName} 为 null 则使用 {@link #jobClassName} 创建 Job ，创建 Job
	 * 的类必须有 Constructor(String) 构造方法。
	 * <p/>
	 * 如果 {@link #classLoader} 为 null 则使用 {@link Thread#getContextClassLoader()}
	 * 。
	 *
	 * @param jobClass Job的类型信息。
	 * @return 创建的 Job 。
	 * @throws RuntimeException 如果发生异常。
	 */
	protected J createJob (Class<J> jobClass) {
		J job;
		if (this.classLoader == null) {
			this.classLoader = Thread.currentThread ().getContextClassLoader ();
		}
		if (this.jobRefName == null) {
			if (!StringUtils.hasLength (this.jobClassName)) {
				throw new RuntimeException ("没有配置[Job]的实例并且没有配置[Job]的完整类名用于生成[Job]实例。");
			}
			Class<?> clazz = null;
			try {
				clazz = this.classLoader.loadClass (this.jobClassName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException ("配置的用于生成[Job]的实例的[jobClassName]值 {" + this.jobClassName + "} 没有找到类。", e);
			}
			Constructor<?> constructor = null;
			try {
				constructor = clazz.getConstructor (String.class);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException ("没有找到类'" + this.jobClassName + "' 的构造方法 Constructor(String) 。", e);
			}
			Object j = null;
			try {
				j = constructor.newInstance (this.jobName);
			} catch (InvocationTargetException e) {
				throw new RuntimeException ("构建 [Job] 的实例时发生错误。", e.getTargetException ());
			} catch (Exception e) {
				throw new RuntimeException ("构建 [Job] 的实例时发生错误。", e);
			}
			if (!jobClass.isInstance (j)) {
				throw new RuntimeException ("类 '" + j.getClass ().getName () + "' 不是 '" + jobClass.getName () + "' 的实例。");
			} else {
				job = jobClass.cast (j);
			}
		} else {
			Object j = this.applicationContext.getBean (this.jobRefName);
			try {
				job = jobClass.cast (j);
			} catch (ClassCastException e) {
				throw new RuntimeException ("类 '" + j.getClass ().getName () + "' 不是 '" + Job.class.getName () + "' 的实例。");
			}
		}
		return job;
	}
	
}
