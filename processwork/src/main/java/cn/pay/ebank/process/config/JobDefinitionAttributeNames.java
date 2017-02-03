/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/**
 * Job 定义的参数名定义。
 * <p>
 * 参见 <code>dynamic-process-1.0.xsd</code> 和 <code>process-1.0.xsd</code>
 * 
 * @author
 * 
 */
public interface JobDefinitionAttributeNames {
	
	public static final String JOB_PARSER_NAME = "job";
	
	public static final String DYNAMIC_JOB_PARSER_NAME = "dynamic-job";
	
	public static final String CHILD_NODE_PARSER_NAME = "child-node";
	
	public static final String JOB_NAMESPACE = "http://www.ebank.pay.cn/schema/common/process";
	
	public static final String DYNAMIC_JOB_NAMESPACE = "http://www.ebank.pay.cn/schema/common/dynamic-process";
	
	// 以下是job相关
	
	public static final String JOB_ELEMENT_ID_ATTRIBUTE = BeanDefinitionParserDelegate.ID_ATTRIBUTE;
	
	public static final String JOB_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	public static final String JOB_ELEMENT_REF_ATTRIBUTE = "ref";
	
	public static final String JOB_ELEMENT_ABSTRACT_ATTRIBUTE = "abstract";
	
	public static final String JOB_ELEMENT_INIT_METHOD_ATTRIBUTE = "init-method";
	
	public static final String PROJECT_GLOBAL_PARAMS_ELEMENT = "project-global-params";
	
	public static final String PROJECT_GLOBAL_PARAMS_ELEMENT_REF_ATTRIBUTE = "ref";
	
	public static final String EXCEPTION_HANDLER_ELEMENT = "exception-handler";
	
	public static final String EXCEPTION_HANDLER_ELEMENT_TYPE_ATTRIBUTE = "type";
	
	public static final String EXCEPTION_HANDLER_ELEMENT_HANDLER_ATTRIBUTE = "handler";
	
	public static final String NODE_ELEMENT = "node";
	
	public static final String NODE_ELEMENT_NAME_ATTRIBUTE = "name";
	
	public static final String NODE_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	public static final String NODE_ELEMENT_REF_ATTRIBUTE = "ref";
	
	public static final String NODE_ELEMENT_CHILD_REF_ATTRIBUTE = "child-ref";
	
	public static final String CHILD_NODE_ELEMENT_NAME_ATTRIBUTE = BeanDefinitionParserDelegate.ID_ATTRIBUTE;
	
	public static final String CONTEXT_ELEMENT = "context";
	
	public static final String CONTEXT_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	// 以下是动态job相关
	
	public static final String DYNAMIC_JOB_ELEMENT_ID_ATTRIBUTE = BeanDefinitionParserDelegate.ID_ATTRIBUTE;
	
	public static final String DYNAMIC_JOB_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	public static final String DYNAMIC_JOB_ELEMENT_REF_ATTRIBUTE = "ref";
	
	public static final String DYNAMIC_JOB_ELEMENT_ROOT_ATTRIBUTE = "root";
	
	public static final String DYNAMIC_JOB_ELEMENT_ABSTRACT_ATTRIBUTE = "abstract";
	
	public static final String DYNAMIC_JOB_ELEMENT_CONTEXT_CLASS_ATTRIBUTE = "context-class";
	
	public static final String DYNAMIC_JOB_ELEMENT_EXCEPTION_HANDLER_GROUP_ATTRIBUTE = "exception-handler-group";
	
	public static final String DYNAMIC_JOB_ELEMENT_PROJECT_GLOBAL_PARAMS_ATTRIBUTE = "project-global-params";
	
	public static final String DYNAMIC_NODE_LOADER_ELEMENT = "dynamic-node-loader";
	
	public static final String DYNAMIC_NODE_LOADER_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	public static final String DYNAMIC_NODE_ELEMENT = "dynamic-node";
	
	public static final String DYNAMIC_NODE_ELEMENT_ID_ATTRIBUTE = BeanDefinitionParserDelegate.ID_ATTRIBUTE;
	
	public static final String DYNAMIC_NODE_ELEMENT_CLASS_ATTRIBUTE = "class";
	
	public static final String DYNAMIC_NODE_ELEMENT_REF_ATTRIBUTE = "ref";
	
	public static final String DYNAMIC_NODE_ELEMENT_PARENT_ATTRIBUTE = "parent";
	
	public static final String DYNAMIC_CONTEXT_ELEMENT = "dynamic-context";
	
	public static final String DYNAMIC_CONTEXT_ELEMENT_NAME_ATTRIBUTE = "name";
	
	public static final String DYNAMIC_EXCEPTION_HANDLER_ELEMENT = "dynamic-exception-handler";
	
	public static final String DYNAMIC_EXCEPTION_HANDLER_ELEMENT_ID_ATTRIBUTE = BeanDefinitionParserDelegate.ID_ATTRIBUTE;
	
	public static final String DYNAMIC_EXCEPTION_HANDLER_ELEMENT_GROUP_ATTRIBUTE = "group";
	
	public static final String DYNAMIC_EXCEPTION_HANDLER_ELEMENT_TYPE_ATTRIBUTE = "type";
	
	public static final String DYNAMIC_EXCEPTION_HANDLER_ELEMENT_HANDLER_ATTRIBUTE = "handler";
	
}
