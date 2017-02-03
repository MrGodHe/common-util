/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Job命名空间内容解析持有器。
 * 
 * @author
 * 
 */
public class JobNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser(JobDefinitionAttributeNames.JOB_PARSER_NAME,
			new JobBeanDefinitionParser());
		registerBeanDefinitionParser(JobDefinitionAttributeNames.CHILD_NODE_PARSER_NAME,
			new ChildNodeBeanDefinitionParser());
		registerBeanDefinitionParser(JobDefinitionAttributeNames.DYNAMIC_JOB_PARSER_NAME,
			new DynamicJobBeanDefinitionParser());
	}
	
}
