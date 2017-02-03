/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.JobDefinitionAttributeNames;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * dynamic-process 命名空间持有器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DynamicJobNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser(JobDefinitionAttributeNames.DYNAMIC_NODE_ELEMENT,
			new DynamicNodeBeanDefinitionParser());
		registerBeanDefinitionParser(JobDefinitionAttributeNames.DYNAMIC_EXCEPTION_HANDLER_ELEMENT,
			new DynamicExceptionHanlderBeanDefinitionParser());
	}
	
}
