/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.config.JobContextBuilderSupport;

/**
 * 可动态配置或者刷新的动态任务上下文构造器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public abstract class DynamicJobContextBuilder extends JobContextBuilderSupport {
	
	/**
	 * 刷新。
	 * @throws UnsupportedOperationException 如果不支持上下文构造器级别的刷新。
	 * @throws Exception 发生异常时。
	 */
	public abstract void refresh() throws Exception;
	
	/**
	 * 得到与之关联的工厂。
	 * @return 与之关联的工厂。
	 */
	public abstract DynamicJobFactory<?> getDynamicJobFactory();
	
}
