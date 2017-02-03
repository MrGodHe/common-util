/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.dynamic.DynamicJob;

/**
 * 简单的 DynamicJob 工厂，用于创建 DynamicJob 对象。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class SimpleDynamicJobFactory extends DynamicJobFactorySupport<DynamicJob> {
	
	@SuppressWarnings("unchecked")
	protected <F extends DynamicJobFactorySupport<DynamicJob>> DynamicJobContextBuilderSupport<F, DynamicJob> createJobContextBuilder(	DynamicJobFactorySupport<DynamicJob> dynamicJobFactory) {
		return (DynamicJobContextBuilderSupport<F, DynamicJob>) new DynamicJobContextBuilder(this);
	}
	
	private static class DynamicJobContextBuilder
													extends
													DynamicJobContextBuilderSupport<SimpleDynamicJobFactory, DynamicJob> {
		
		protected DynamicJobContextBuilder(SimpleDynamicJobFactory dynamicJobFactory) {
			super(dynamicJobFactory);
		}
		
	}
	
}
