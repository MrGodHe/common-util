/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.JobDefinitionAttributeNames;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;

/**
 * dynamic-process 命名空间持有器提供者。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DynamicJobNamespaceHandlerResolver implements NamespaceHandlerResolver {
	
	private NamespaceHandlerResolver namespaceHandlerResolver;
	
	/**
	 * 构建一个 DynamicJobNamespaceHandlerResolver 。
	 */
	public DynamicJobNamespaceHandlerResolver() {
		this.namespaceHandlerResolver = new DefaultNamespaceHandlerResolver();
	}
	
	public DynamicJobNamespaceHandlerResolver(ClassLoader classLoader) {
		this.namespaceHandlerResolver = new DefaultNamespaceHandlerResolver(classLoader);
	}
	
	public DynamicJobNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver) {
		this.namespaceHandlerResolver = namespaceHandlerResolver;
	}
	
	public NamespaceHandler resolve(String namespaceUri) {
		if (JobDefinitionAttributeNames.JOB_NAMESPACE.equals(namespaceUri)) {
			return null;
		}
		if (JobDefinitionAttributeNames.DYNAMIC_JOB_NAMESPACE.equals(namespaceUri)) {
			DynamicJobNamespaceHandler dynamicJobNamespaceHandler = new DynamicJobNamespaceHandler();
			dynamicJobNamespaceHandler.init();
			return dynamicJobNamespaceHandler;
		}
		return this.namespaceHandlerResolver.resolve(namespaceUri);
	}
	
}
