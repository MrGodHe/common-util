/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.SimpleJobContext.NodeWrapper;
import cn.pay.ebank.process.common.ParameterHolderSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Job上下文 构建器。
 *
 * @author
 */
public abstract class JobContextBuilder extends ParameterHolderSupport implements ApplicationContextAware {
	
	protected ApplicationContext applicationContext;
	
	protected String jobContextClassName;
	
	protected Map<Object, Object> projectGlobalParams;
	
	protected ClassLoader classLoader;
	
	protected volatile NodeWrapper root;
	
	public void setApplicationContext (ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void setJobContextClassName (String jobContextClassName) {
		this.jobContextClassName = jobContextClassName;
	}
	
	public void setProjectGlobalParams (Map<Object, Object> projectGlobalParams) {
		this.projectGlobalParams = projectGlobalParams;
	}
	
	public void setClassLoader (ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * 构建 JobContext 。
	 *
	 * @param job 相关联的 Job 的实例。
	 * @return 构建的 JobContext 实例。
	 */
	public abstract JobContext build (Job job);
	
}
