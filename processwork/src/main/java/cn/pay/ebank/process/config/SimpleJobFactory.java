/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.exception.ExceptionHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 简单的Job工厂，用于创建Job对象。
 *
 * @author
 */
public class SimpleJobFactory extends JobFactorySupport<Job> {
	
	private NodeNameHolder nodeNameHolder;
	
	private Map<String, BeanNameHolder> exceptionHandlers;
	
	private volatile JobContextBuilder jobContextBuilder;
	
	public void setNodeNameHolder (NodeNameHolder nodeNameHolder) {
		this.nodeNameHolder = nodeNameHolder;
	}
	
	public void setExceptionHandlers (Map<String, BeanNameHolder> exceptionHandlers) {
		this.exceptionHandlers = exceptionHandlers;
	}
	
	@SuppressWarnings("unchecked")
	public Job getObject () throws Exception {
		Job job = createJob (Job.class);
		if (job instanceof DefaultJob) {
			// 构建 JobContext
			if (this.jobContextBuilder == null) {
				synchronized (this) {
					if (this.jobContextBuilder == null) {
						XmlConfigJobContextBuilder xmlConfigJobContextBuilder = new XmlConfigJobContextBuilder ();
						xmlConfigJobContextBuilder.setApplicationContext (this.applicationContext);
						xmlConfigJobContextBuilder.setNodeNameHolder (this.nodeNameHolder);
						xmlConfigJobContextBuilder.setClassLoader (this.classLoader);
						xmlConfigJobContextBuilder.setJobContextClassName (this.jobContextClassName);
						if (StringUtils.hasLength (this.projectGlobalParamsRefName)) {
							Map<Object, Object> projectGlobalParams = this.applicationContext.getBean (this.projectGlobalParamsRefName, Map.class);
							xmlConfigJobContextBuilder.setProjectGlobalParams (projectGlobalParams);
						}
						this.jobContextBuilder = xmlConfigJobContextBuilder;
					}
				}
			}
			((DefaultJob) job).setJobContextBuilder (this.jobContextBuilder);
		}
		// 设置异常处理器
		if (CollectionUtils.isEmpty (this.exceptionHandlers)) {
			job.setExceptionHandlers (Collections.<Class<Throwable>, ExceptionHandler>emptyMap ());
		} else {
			Map<Class<Throwable>, ExceptionHandler> eh = new LinkedHashMap<Class<Throwable>, ExceptionHandler> ();
			for (Entry<String, BeanNameHolder> entry : this.exceptionHandlers.entrySet ()) {
				String typeClassName = entry.getKey ();
				Class<?> typeClass;
				try {
					typeClass = this.classLoader.loadClass (typeClassName);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException ("[Job] '" + job.getName () + "'没有找到异常处理类型 '" + typeClassName + "' 对应的类。", e);
				}
				eh.put ((Class<Throwable>) typeClass, this.applicationContext.getBean (entry.getValue ().getRefName (), ExceptionHandler.class));
			}
			job.setExceptionHandlers (eh);
		}
		job.initOrReInt ();
		return job;
	}
	
	public Class<Job> getObjectType () {
		return Job.class;
	}
	
	public boolean isSingleton () {
		return false;
	}
	
	public final Job getJob () throws Exception {
		return getObject ();
	}
	
}
