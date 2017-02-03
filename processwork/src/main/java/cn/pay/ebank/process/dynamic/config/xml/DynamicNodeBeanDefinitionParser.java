/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.JobDefinitionAttributeNames;
import cn.pay.ebank.process.config.NodeNameHolder;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * &lt;dynamic-node&gt;解析器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DynamicNodeBeanDefinitionParser extends AbstractBeanDefinitionParser implements
																					JobDefinitionAttributeNames {
	
	protected AbstractBeanDefinition parseInternal(Element nodeElement, ParserContext parserContext) {
		DynamicJobApplicationContext context = (DynamicJobApplicationContext) parserContext
			.getRegistry();
		String id = StringUtils.trimWhitespace(nodeElement
			.getAttribute(DYNAMIC_NODE_ELEMENT_ID_ATTRIBUTE));
		String className = StringUtils.trimWhitespace(nodeElement
			.getAttribute(DYNAMIC_NODE_ELEMENT_CLASS_ATTRIBUTE));
		String ref = StringUtils.trimWhitespace(nodeElement
			.getAttribute(DYNAMIC_NODE_ELEMENT_REF_ATTRIBUTE));
		boolean hasClassName = StringUtils.hasLength(className);
		boolean hasRef = StringUtils.hasLength(ref);
		if (hasClassName && hasRef) {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(nodeElement.getAttribute("id")),
				"[id] 为 '" + id + "' 的 [dynamic-node] 同时配置属性 'class' 和 'ref' 。");
		}
		String parent = StringUtils.trimWhitespace(nodeElement
			.getAttribute(DYNAMIC_NODE_ELEMENT_PARENT_ATTRIBUTE));
		NodeNameHolder nameHolder;
		if (hasClassName) {
			BeanDefinitionBuilder nodeBeanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(className);
			nodeBeanDefinitionBuilder.addConstructorArgValue(id);
			nodeBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
			AbstractBeanDefinition beanDefinition = nodeBeanDefinitionBuilder.getBeanDefinition();
			BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
			delegate.parsePropertyElements(nodeElement, beanDefinition);
			registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, id), context);
			nameHolder = new NodeNameHolder(id);
			nameHolder.setParentName(parent);
			nameHolder.setRefName(id);
			context.addNodeNameHolder(nameHolder);
			return nodeBeanDefinitionBuilder.getBeanDefinition();
		} else if (hasRef) {
			nameHolder = new NodeNameHolder(id);
			nameHolder.setRefName(ref);
			nameHolder.setParentName(parent);
			context.addNodeNameHolder(nameHolder);
		} else {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(id),
				"[id] 为 '" + id + "' 的 [node] 没有配置属性 'class' 或 'ref' 。");
		}
		return null;
	}
	
}
