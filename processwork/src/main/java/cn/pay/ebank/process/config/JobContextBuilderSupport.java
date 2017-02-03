/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.DefaultJobContext;
import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.JobContext;
import cn.pay.ebank.process.SimpleJobContext.NodeWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * JobContextBuilder 的实现的支持类。
 *
 * @author
 */
public abstract class JobContextBuilderSupport extends JobContextBuilder {
	
	/**
	 * 由 {@link #build(Job)} 调用创建 JobContext 。
	 *
	 * @return 创建的 JobContext 。
	 */
	protected JobContext createJobContext () {
		JobContext jobContext = null;
		Class<?> clazz = null;
		try {
			clazz = this.classLoader.loadClass (this.jobContextClassName == null ? DefaultJobContext.class.getName () : this.jobContextClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException ("配置的用于生成[JobContext]的实例的[jobContextClassName]值 {" + this.jobContextClassName + "} 没有找到类。", e);
		}
		Constructor<?> constructor = null;
		try {
			constructor = clazz.getConstructor (NodeWrapper.class, Map.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException ("没有找到类'" + this.jobContextClassName + "' 的构造方法 Constructor(NodeWrapper, Map<Object, Object>) 。", e);
		}
		Object context = null;
		try {
			context = constructor.newInstance (this.root, this.projectGlobalParams);
		} catch (InvocationTargetException e) {
			throw new RuntimeException ("构建 [JobContext] 的实例时发生错误。", e.getTargetException ());
		} catch (Exception e) {
			throw new RuntimeException ("构建 [JobContext] 的实例时发生错误。", e);
		}
		if (!(context instanceof JobContext)) {
			throw new RuntimeException ("类 '" + context.getClass ().getName () + "' 不是 '" + JobContext.class.getName () + "' 的实例。");
		} else {
			jobContext = (JobContext) context;
		}
		return jobContext;
	}
	
}
