/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic;

import cn.pay.ebank.process.common.Immutable;
import cn.pay.ebank.process.config.NodeNameHolder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

/**
 * 动态任务应用上下文。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public interface DynamicJobApplicationContext extends BeanDefinitionRegistry,
												ConfigurableApplicationContext, DisposableBean {
	
	/**
	 * 通过组名得到该组异常处理器信息。
	 * @param groupName 组名。
	 * @return 组名对应的该组的异常处理器信息，如果没有则为 null 。
	 */
	@Immutable
	Set<ExceptionHandlerInformation> getExceptionHandlerInformations (String groupName);
	
	/**
	 * 添加一组异常处理器信息到上下文中，该操作会替换到原有的组信息。
	 * @param groupName 组名。
	 * @param exceptionHandlerInformations 异常处理器信息集合。
	 * @return 如果让groupName指定的组名的异常处理器信息列表发生改变返回 true 。
	 */
	boolean addExceptionHandlerInformations (String groupName, Set<ExceptionHandlerInformation> exceptionHandlerInformations);
	
	/**
	 * 添加一组异常处理器信息到上下文中，该操作会在原有的组信息的基础上追加。
	 * @param groupName 组名。
	 * @param exceptionHandlerInformations 异常处理器信息集合。
	 * @return 如果让groupName指定的组名的异常处理器信息列表发生改变返回 true 。
	 */
	boolean appendExceptionHandlerInformation (String groupName, ExceptionHandlerInformation... exceptionHandlerInformations);
	
	/**
	 * 移除一组异常处理器信息。
	 * @param groupName 需要异常的异常上下文信息的组的组名。
	 * @return 如果成功移除返回 true 。
	 */
	boolean removeExceptionHandlerInformations (String groupName);
	
	/**
	 * 得到所有异常处理器信息组的组名。
	 * @return 所有异常处理器信息组的组名。
	 */
	@Immutable
	Set<String> getExceptionHandlerGroupNames ();
	
	/**
	 * 添加一个 node名称持有器 。
	 * @param nodeNameHolder 需要添加的node名称持有器 。
	 * @return 如果 node名称持有器列表发生改变返回 true 。
	 */
	boolean addNodeNameHolder (NodeNameHolder nodeNameHolder);
	
	/**
	 * 移除一个 node名称持有器 。
	 * @param nodeNameHolder 需要移除的node名称持有器 。
	 * @return 如果 node名称持有器列表发生改变返回 true 。
	 */
	boolean removeNodeNameHolder (NodeNameHolder nodeNameHolder);
	
	/**
	 * 通过node名称得到 node名称持有器 。
	 * @param nodeName node名称。
	 * @return nodeName对应的 node名称持有器 ，没有则返回 null 。
	 */
	NodeNameHolder getNodeNameHolder (String nodeName);
	
	/**
	 * 得到所有node名称持有器的名称。
	 * @return 所有node名称持有器的名称。
	 */
	@Immutable
	Set<String> getNodeNameHolderNames ();
	
}
