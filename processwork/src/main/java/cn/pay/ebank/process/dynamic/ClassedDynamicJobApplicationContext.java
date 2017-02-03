/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic;

import cn.pay.ebank.process.config.NodeNameHolder;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 基于 class 文件的动态任务应用上下文实现。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class ClassedDynamicJobApplicationContext extends AnnotationConfigApplicationContext
																							implements
																							DynamicJobApplicationContext {
	
	/** 动态异常处理器信息仓库，key 为组名，value为组内的动态异常处理器信息集合 */
	protected ConcurrentMap<String, Set<ExceptionHandlerInformation>> exceptionHandlerNamesMap = new ConcurrentHashMap<String, Set<ExceptionHandlerInformation>>();
	
	/** node名称持有器仓库，key 为node名称，value为持有器。 */
	protected ConcurrentMap<String, NodeNameHolder> nodeNameHolderMap = new ConcurrentHashMap<String, NodeNameHolder>();
	
	/**
	 * 构建一个 ClassedDynamicJobApplicationContext 。
	 */
	public ClassedDynamicJobApplicationContext() {
		super();
	}
	
	public Set<ExceptionHandlerInformation> getExceptionHandlerInformations(String groupName) {
		Set<ExceptionHandlerInformation> exceptionHandlerInformations = this.exceptionHandlerNamesMap
			.get(groupName);
		return exceptionHandlerInformations == null ? null : Collections
			.unmodifiableSet(exceptionHandlerInformations);
	}
	
	public boolean addExceptionHandlerInformations(	String groupName,
													Set<ExceptionHandlerInformation> exceptionHandlerInformations) {
		if (groupName == null || exceptionHandlerInformations == null) {
			return false;
		}
		return this.exceptionHandlerNamesMap.put(groupName, exceptionHandlerInformations) != null;
	}
	
	public boolean removeExceptionHandlerInformations(String groupName) {
		if (groupName == null) {
			return false;
		}
		return this.exceptionHandlerNamesMap.remove(groupName) != null;
	}
	
	public boolean appendExceptionHandlerInformation(	String groupName,
														ExceptionHandlerInformation... exceptionHandlerInformations) {
		if (groupName == null || ArrayUtils.isEmpty(exceptionHandlerInformations)) {
			return false;
		}
		Set<ExceptionHandlerInformation> exceptionHandlerInformationsSet = this.exceptionHandlerNamesMap
			.get(groupName);
		if (exceptionHandlerInformationsSet == null) {
			exceptionHandlerInformationsSet = new CopyOnWriteArraySet<ExceptionHandlerInformation>();
			Set<ExceptionHandlerInformation> old = this.exceptionHandlerNamesMap.putIfAbsent(
				groupName, exceptionHandlerInformationsSet);
			if (old != null) {
				exceptionHandlerInformationsSet = old;
			}
		}
		return exceptionHandlerInformationsSet.addAll(Arrays.asList(exceptionHandlerInformations));
	}
	
	public Set<String> getExceptionHandlerGroupNames() {
		return Collections.unmodifiableSet(this.exceptionHandlerNamesMap.keySet());
	}
	
	public boolean addNodeNameHolder(NodeNameHolder nodeNameHolder) {
		if (nodeNameHolder == null || nodeNameHolder.getBeanName() == null) {
			return false;
		}
		return this.nodeNameHolderMap.put(nodeNameHolder.getBeanName(), nodeNameHolder) != null;
	}
	
	public boolean removeNodeNameHolder(NodeNameHolder nodeNameHolder) {
		if (nodeNameHolder == null || nodeNameHolder.getBeanName() == null) {
			return false;
		}
		return this.nodeNameHolderMap.remove(nodeNameHolder.getBeanName()) != null;
	}
	
	public NodeNameHolder getNodeNameHolder(String nodeName) {
		if (nodeName == null) {
			return null;
		}
		return this.nodeNameHolderMap.get(nodeName);
	}
	
	public Set<String> getNodeNameHolderNames() {
		return Collections.unmodifiableSet(this.nodeNameHolderMap.keySet());
	}
	
}
