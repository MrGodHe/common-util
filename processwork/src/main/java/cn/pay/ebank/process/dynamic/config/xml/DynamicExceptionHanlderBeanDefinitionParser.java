/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.JobDefinitionAttributeNames;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import cn.pay.ebank.process.dynamic.ExceptionHandlerInformation;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * &lt;dynamic-exception-handler&gt;解析器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DynamicExceptionHanlderBeanDefinitionParser extends AbstractBeanDefinitionParser
																								implements
																								JobDefinitionAttributeNames {
	
	protected AbstractBeanDefinition parseInternal(Element exceptionHandlerElement,
													ParserContext parserContext) {
		DynamicJobApplicationContext context = (DynamicJobApplicationContext) parserContext
			.getRegistry();
		String id = StringUtils.trimWhitespace(exceptionHandlerElement
			.getAttribute(DYNAMIC_EXCEPTION_HANDLER_ELEMENT_ID_ATTRIBUTE));
		String type = StringUtils.trimWhitespace(exceptionHandlerElement
			.getAttribute(DYNAMIC_EXCEPTION_HANDLER_ELEMENT_TYPE_ATTRIBUTE));
		String handler = StringUtils.trimWhitespace(exceptionHandlerElement
			.getAttribute(DYNAMIC_EXCEPTION_HANDLER_ELEMENT_HANDLER_ATTRIBUTE));
		String groupName = StringUtils.trimWhitespace(exceptionHandlerElement
			.getAttribute(DYNAMIC_EXCEPTION_HANDLER_ELEMENT_GROUP_ATTRIBUTE));
		BeanDefinitionBuilder ehBuilder = BeanDefinitionBuilder.genericBeanDefinition(handler);
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		AbstractBeanDefinition enBeanDefinition = ehBuilder.getBeanDefinition();
		delegate.parseConstructorArgElements(exceptionHandlerElement, enBeanDefinition);
		delegate.parsePropertyElements(exceptionHandlerElement, enBeanDefinition);
		context.appendExceptionHandlerInformation(groupName, new ExceptionHandlerInformation(id,
			handler, groupName, type));
		return enBeanDefinition;
	}
	
}
