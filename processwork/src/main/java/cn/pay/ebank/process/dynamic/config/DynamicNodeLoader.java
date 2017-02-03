/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.common.Named;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * 动态节点加载器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public interface DynamicNodeLoader extends Named {
	
	/**
	 * 创建动态任务应用上下文。
	 * <p>
	 * 注意，该方法只是创建，加载上下文在 {@link #load(DynamicJobApplicationContext)} 中。
	 * @param jobId 任务id。
	 * @param parentApplicationContext 父应用上下文。
	 * @return 创建的动态任务应用上下文。
	 * @throws org.springframework.context.ApplicationContextException 发生异常时。
	 */
	DynamicJobApplicationContext createApplicationContext (String jobId, ApplicationContext parentApplicationContext);
	
	/**
	 * 加载。
	 * @param applicationContext 动态任务应用上下文。
	 * @throws Exception 发生异常时。
	 */
	void load (DynamicJobApplicationContext applicationContext) throws Exception;
	
}
