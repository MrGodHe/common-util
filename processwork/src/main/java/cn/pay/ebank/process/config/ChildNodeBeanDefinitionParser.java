/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * &lt;child-node&gt; 的解析器。
 * 
 * @author
 * 
 * 
 */
public class ChildNodeBeanDefinitionParser extends AbstractBeanDefinitionParser implements
																				JobDefinitionAttributeNames {
	
	private static final NodeParserHelper NODE_PARSER_HELPER = new NodeParserHelper();
	
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
			.genericBeanDefinition(ChildNodeBeanInfo.class);
		String name = StringUtils.trimWhitespace(element
			.getAttribute(CHILD_NODE_ELEMENT_NAME_ATTRIBUTE));
		Element nodeElement = DomUtils.getChildElementByTagName(element, NODE_ELEMENT);
		NodeNameHolder root = NODE_PARSER_HELPER.parseNode(nodeElement, null, parserContext, name,
			parserContext.getRegistry());
		beanDefinitionBuilder.addPropertyValue("root", root);
		return beanDefinitionBuilder.getBeanDefinition();
	}
	
}
