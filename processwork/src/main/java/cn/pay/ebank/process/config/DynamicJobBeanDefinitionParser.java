/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import cn.pay.ebank.process.dynamic.config.SimpleDynamicJobFactory;
import cn.pay.ebank.process.dynamic.config.xml.DynamicNodeLoadersFactoryBean;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析 &lt;dynamic-job&gt; 的解析器。
 * 
 * @author
 * 
 */
public class DynamicJobBeanDefinitionParser extends AbstractBeanDefinitionParser implements
																				JobDefinitionAttributeNames {
	
	private static final String JOB_NODE_LOADER_BEAN_ID_START = "$job_node_loader";
	
	private static final String JOB_NODE_LOADER_BEAB_ID_SEPARATOR = "$_";
	
	protected AbstractBeanDefinition parseInternal(Element jobElement, ParserContext parserContext) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
			.genericBeanDefinition(SimpleDynamicJobFactory.class);
		// 解析dynamic-job属性
		String className = StringUtils.trimWhitespace(jobElement
			.getAttribute(DYNAMIC_JOB_ELEMENT_CLASS_ATTRIBUTE));
		String ref = StringUtils.trimWhitespace(jobElement
			.getAttribute(DYNAMIC_JOB_ELEMENT_REF_ATTRIBUTE));
		boolean hasClassName = StringUtils.hasLength(className);
		boolean hasRef = StringUtils.hasLength(ref);
		String id = StringUtils.trimWhitespace(jobElement
			.getAttribute(DYNAMIC_JOB_ELEMENT_ID_ATTRIBUTE));
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
				.getDescription(), StringUtils.trimWhitespace(jobElement
				.getAttribute(DYNAMIC_JOB_ELEMENT_ID_ATTRIBUTE)), "没有配置属性 'class' 或 'ref' 。");
		}
		String isAbstract = StringUtils.trimWhitespace(jobElement
			.getAttribute(DYNAMIC_JOB_ELEMENT_ABSTRACT_ATTRIBUTE));
		if (StringUtils.hasLength(isAbstract)) {
			beanDefinitionBuilder.setAbstract(Boolean.parseBoolean(isAbstract));
		}
		String root = StringUtils.trimWhitespace(jobElement
			.getAttribute(DYNAMIC_JOB_ELEMENT_ROOT_ATTRIBUTE));
		if (StringUtils.hasLength(root)) {
			beanDefinitionBuilder.addPropertyValue("root", root);
		}
		Element dynamicJobContextElement = DomUtils.getChildElementByTagName(jobElement,
			DYNAMIC_CONTEXT_ELEMENT);
		if (dynamicJobContextElement != null) {
			beanDefinitionBuilder.addPropertyValue("jobContextClassName", StringUtils
				.trimWhitespace(dynamicJobContextElement
					.getAttribute(DYNAMIC_JOB_ELEMENT_CONTEXT_CLASS_ATTRIBUTE)));
		}
		beanDefinitionBuilder.addPropertyValue("exceptionHandlerGroup", StringUtils
			.trimWhitespace(jobElement
				.getAttribute(DYNAMIC_JOB_ELEMENT_EXCEPTION_HANDLER_GROUP_ATTRIBUTE)));
		beanDefinitionBuilder.addPropertyValue("projectGlobalParamsRefName", StringUtils
			.trimWhitespace(jobElement
				.getAttribute(DYNAMIC_JOB_ELEMENT_PROJECT_GLOBAL_PARAMS_ATTRIBUTE)));
		
		// 解析 dynamic-node-loader
		List<Element> nodeLoaderElements = DomUtils.getChildElementsByTagName(jobElement,
			DYNAMIC_NODE_LOADER_ELEMENT);
		String loadersId = id + JOB_NODE_LOADER_BEAN_ID_START;
		BeanDefinitionBuilder dnlsfBeanBuilder = BeanDefinitionBuilder
			.genericBeanDefinition(DynamicNodeLoadersFactoryBean.class);
		List<BeanNameHolder> beanNameHolders = new ArrayList<BeanNameHolder>(
			nodeLoaderElements.size());
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		int i = 0;
		for (Element nodeLoaderElement : nodeLoaderElements) {
			String loaderClass = StringUtils.trimWhitespace(nodeLoaderElement
				.getAttribute(DYNAMIC_NODE_LOADER_ELEMENT_CLASS_ATTRIBUTE));
			if (loaderClass == null) {
				throw new BeanDefinitionStoreException(parserContext.getReaderContext()
					.getResource().getDescription(), id,
					"[dynamic-job] '" + id + "' 下的 [dynamic-node-loader] 没有配置属性 'class' 。");
			}
			BeanDefinitionBuilder nodeLoaderBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(loaderClass);
			AbstractBeanDefinition beanDefinition = nodeLoaderBuilder.getBeanDefinition();
			delegate.parseConstructorArgElements(nodeLoaderElement, beanDefinition);
			delegate.parsePropertyElements(nodeLoaderElement, beanDefinition);
			String loaderId = loadersId + JOB_NODE_LOADER_BEAB_ID_SEPARATOR + i;
			registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, loaderId),
				parserContext.getRegistry());
			i++;
			BeanNameHolder holder = new BeanNameHolder(loaderId);
			holder.setRefName(loaderId);
			beanNameHolders.add(holder);
		}
		dnlsfBeanBuilder.addPropertyValue("nodeLoaderNameHolders", beanNameHolders);
		registerBeanDefinition(new BeanDefinitionHolder(dnlsfBeanBuilder.getBeanDefinition(),
			loadersId), parserContext.getRegistry());
		beanDefinitionBuilder.addPropertyReference("dynamicNodeLoaders", loadersId);
		return beanDefinitionBuilder.getBeanDefinition();
	}
}
