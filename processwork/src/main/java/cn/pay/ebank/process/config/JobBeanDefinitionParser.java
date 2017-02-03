/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.EmptyNode;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用于解析 &lt;job&gt; 的解析器。
 * 
 * @author
 * 
 */
public class JobBeanDefinitionParser extends AbstractBeanDefinitionParser implements
																			JobDefinitionAttributeNames {
	
	private static final String JOB_EXCEPTION_HANDLER_BEAN_ID_START = "$job_exceptionHandler$_";
	
	private static final String JOB_EXCEPTION_HANDLER_BEAB_ID_LEVEL_SEPARATOR = "$_";
	
	private static final Pattern PATTERN = Pattern.compile("\\.");
	
	private static final NodeParserHelper NODE_PARSER_HELPER = new NodeParserHelper();
	
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
			.genericBeanDefinition(SimpleJobFactory.class);
		String className = StringUtils.trimWhitespace(element
			.getAttribute(JOB_ELEMENT_CLASS_ATTRIBUTE));
		String ref = StringUtils.trimWhitespace(element.getAttribute(JOB_ELEMENT_REF_ATTRIBUTE));
		boolean hasClassName = StringUtils.hasLength(className);
		boolean hasRef = StringUtils.hasLength(ref);
		String id = StringUtils.trimWhitespace(element.getAttribute(JOB_ELEMENT_ID_ATTRIBUTE));
		beanDefinitionBuilder.addPropertyValue("jobName", id);
		if (hasClassName && hasRef) {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), id, "同时配置属性 'class' 和 'ref' 。");
		}
		if (hasClassName) {
			beanDefinitionBuilder.addPropertyValue("jobClassName", className);
		} else if (hasRef) {
			beanDefinitionBuilder.addPropertyValue("jobRefName", ref);
		} else {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(element
				.getAttribute(JOB_ELEMENT_ID_ATTRIBUTE)), "没有配置属性 'class' 或 'ref' 。");
		}
		String isAbstract = StringUtils.trimWhitespace(element
			.getAttribute(JOB_ELEMENT_ABSTRACT_ATTRIBUTE));
		if (StringUtils.hasLength(isAbstract)) {
			beanDefinitionBuilder.setAbstract(Boolean.parseBoolean(isAbstract));
		}
		String initMethod = StringUtils.trimWhitespace(element
			.getAttribute(JOB_ELEMENT_INIT_METHOD_ATTRIBUTE));
		if (StringUtils.hasLength(initMethod)) {
			beanDefinitionBuilder.setInitMethodName(initMethod);
		}
		beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
		// 解析 context
		Element contextElement = DomUtils.getChildElementByTagName(element, CONTEXT_ELEMENT);
		if (contextElement != null) {
			beanDefinitionBuilder.addPropertyValue("jobContextClassName", StringUtils
				.trimWhitespace(contextElement.getAttribute(CONTEXT_ELEMENT_CLASS_ATTRIBUTE)));
		}
		// 解析 project-global-params
		Element projectGlobalParamsElement = DomUtils.getChildElementByTagName(element,
			PROJECT_GLOBAL_PARAMS_ELEMENT);
		if (projectGlobalParamsElement != null) {
			beanDefinitionBuilder.addPropertyValue("projectGlobalParamsRefName", StringUtils
				.trimWhitespace(projectGlobalParamsElement
					.getAttribute(PROJECT_GLOBAL_PARAMS_ELEMENT_REF_ATTRIBUTE)));
		}
		// 解析 exception-handler
		List<Element> exceptionHandlerElements = DomUtils.getChildElementsByTagName(element,
			EXCEPTION_HANDLER_ELEMENT);
		if (!CollectionUtils.isEmpty(exceptionHandlerElements)) {
			Map<String, BeanNameHolder> ehs = new LinkedHashMap<String, BeanNameHolder>();
			int i = 1;
			for (Element exceptionHandlerElement : exceptionHandlerElements) {
				String type = StringUtils.trimWhitespace(exceptionHandlerElement
					.getAttribute(EXCEPTION_HANDLER_ELEMENT_TYPE_ATTRIBUTE));
				String handler = StringUtils.trimWhitespace(exceptionHandlerElement
					.getAttribute(EXCEPTION_HANDLER_ELEMENT_HANDLER_ATTRIBUTE));
				String handlerName = JOB_EXCEPTION_HANDLER_BEAN_ID_START + id
										+ JOB_EXCEPTION_HANDLER_BEAB_ID_LEVEL_SEPARATOR
										+ PATTERN.matcher(handler).replaceAll("_") + "_"
										+ String.valueOf(i);
				BeanDefinitionBuilder ehBuilder = BeanDefinitionBuilder
					.genericBeanDefinition(handler);
				BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
				AbstractBeanDefinition enBeanDefinition = ehBuilder.getBeanDefinition();
				delegate.parseConstructorArgElements(exceptionHandlerElement, enBeanDefinition);
				delegate.parsePropertyElements(exceptionHandlerElement, enBeanDefinition);
				parserContext.getRegistry().registerBeanDefinition(handlerName, enBeanDefinition);
				i++;
				BeanNameHolder holder = new BeanNameHolder(handlerName);
				holder.setRefName(handlerName);
				ehs.put(type, holder);
			}
			beanDefinitionBuilder.addPropertyValue("exceptionHandlers", ehs);
		}
		// 解析 node
		Element nodeElement = DomUtils.getChildElementByTagName(element, NODE_ELEMENT);
		NodeNameHolder holder;
		if (nodeElement == null) {
			// 如果没有配置 node 节点则创建 EmtpyNode
			String startNodeBeanId = NodeParserHelper.JOB_NODE_BEAN_ID_START + "emptyNode";
			BeanDefinitionBuilder emptyNodeBeanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(EmptyNode.class);
			emptyNodeBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
			registerBeanDefinition(
				new BeanDefinitionHolder(emptyNodeBeanDefinitionBuilder.getBeanDefinition(),
					startNodeBeanId), parserContext.getRegistry());
			holder = new NodeNameHolder(startNodeBeanId);
		} else {
			holder = NODE_PARSER_HELPER.parseNode(nodeElement, null, parserContext, id,
				parserContext.getRegistry());
		}
		beanDefinitionBuilder.addPropertyValue("nodeNameHolder", holder);
		return beanDefinitionBuilder.getBeanDefinition();
	}
	
}
