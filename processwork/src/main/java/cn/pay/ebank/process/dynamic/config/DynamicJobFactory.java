/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;

import cn.pay.ebank.process.Job;
import cn.pay.ebank.process.common.Immutable;
import cn.pay.ebank.process.common.ParameterHolder;
import cn.pay.ebank.process.config.JobFactory;

import java.util.List;

/**
 * 支持动态配置或者刷新的 JobFactory 。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public interface DynamicJobFactory<J extends Job> extends JobFactory<Job>,ParameterHolder {
	
	/**
	 * 刷新。
	 * @throws Exception 发生异常时。
	 */
	void refresh () throws Exception;
	
	/**
	 * 设置动态节点加载器列表。
	 * @param dynamicNodeLoaders 动态节点加载器列表。
	 */
	void setDynamicNodeLoaders (List<DynamicNodeLoader> dynamicNodeLoaders);
	
	/**
	 * 得到动态节点加载器列表。
	 * @return 动态节点加载器列表。
	 */
	@Immutable
	List<DynamicNodeLoader> getDynamicNodeLoaders ();
	
	/**
	 * 添加一个动态节点加载器。
	 * @param dynamicNodeLoader 要添加的动态节点加载器。
	 * @return 如果造成动态节点加载器列表发生改变返回 true 。
	 */
	boolean addDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader);
	
	/**
	 * 移除一个动态节点加载器。
	 * @param dynamicNodeLoader 要移除的动态节点加载器。
	 * @return 如果造成动态节点加载器列表发生改变返回 true 。
	 */
	boolean removeDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader);
	
	/**
	 * 判定 dynamicNodeLoader 指定的动态节点加载器是否存在于动态节点加载器中。
	 * @param dynamicNodeLoader 要判定的动态节点加载器。
	 * @return 如果存在返回 true ，否则返回 false 。
	 */
	boolean hasDynamicNodeLoader (DynamicNodeLoader dynamicNodeLoader);
	
	/**
	 * 刷新加锁，在刷新之前或者执行任务时必须加锁。
	 */
	void refreshLock ();
	
	/**
	 * 刷新解锁，在刷新之后或者执行任务完成后必须加锁。
	 */
	void refreshUnlock ();
	
}
