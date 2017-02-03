/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * &lt;node&gt;的解析器帮助者。通过该帮助者可以解析标准格式的&lt;node&gt;。
 * 
 * @author
 * 
 */
public class NodeParserHelper implements JobDefinitionAttributeNames {
	
	public static final String JOB_NODE_BEAN_ID_START = "$job_node$_";
	
	public static final String JOB_NODE_BEAB_ID_LEVEL_SEPARATOR = "$_";
	
	/**
	 * 解析 &lt;node&gt;标签。
	 * @param nodeElement &lt;node&gt;标签的element对象。
	 * @param parentHolder 父节点名称持有器，如果为头节点有则为 null 。
	 * @param parserContext 解析上下文。
	 * @param jobId 任务 id 。
	 * @param beanDefinitionRegistry bean定义的注册器。
	 * @return 解析的结果。
	 */
	public NodeNameHolder parseNode(Element nodeElement, NodeNameHolder parentHolder,
									ParserContext parserContext, String jobId,
									BeanDefinitionRegistry beanDefinitionRegistry) {
		String name = StringUtils.trimWhitespace(nodeElement
			.getAttribute(NODE_ELEMENT_NAME_ATTRIBUTE));
		String className = StringUtils.trimWhitespace(nodeElement
			.getAttribute(NODE_ELEMENT_CLASS_ATTRIBUTE));
		String ref = StringUtils.trimWhitespace(nodeElement
			.getAttribute(NODE_ELEMENT_REF_ATTRIBUTE));
		String childRef = StringUtils.trimWhitespace(nodeElement
			.getAttribute(NODE_ELEMENT_CHILD_REF_ATTRIBUTE));
		boolean hasClassName = StringUtils.hasLength(className);
		boolean hasRef = StringUtils.hasLength(ref);
		boolean hasChildRef = StringUtils.hasLength(childRef);
		if ((hasChildRef && hasRef) || (hasChildRef && hasClassName)) {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(jobId),
				"[name] 为 '" + name
						+ "' 的 [node] 同时配置属性 'child-ref' 和 'ref' 或者 'child-ref' 和 'class' 。");
		}
		if (hasClassName && hasRef) {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(jobId),
				"[name] 为 '" + name + "' 的 [node] 同时配置属性 'class' 和 'ref' 。");
		}
		NodeNameHolder nameHolder;
		boolean hasNext = true;
		if (hasClassName) {
			String refName;
			if (parentHolder == null) {
				refName = JOB_NODE_BEAN_ID_START + StringUtils.trimWhitespace(jobId)
							+ JOB_NODE_BEAB_ID_LEVEL_SEPARATOR + name;
			} else {
				if (!parentHolder.getRefName().startsWith(JOB_NODE_BEAN_ID_START)) {
					refName = JOB_NODE_BEAN_ID_START + StringUtils.trimWhitespace(jobId);
					String n = null;
					int i = 0;
					for (NodeNameHolder parent = parentHolder; (n = parent == null ? null : parent
						.getRefName()) != null; i++, parent = parent.getParent()) {
						refName += JOB_NODE_BEAB_ID_LEVEL_SEPARATOR;
						refName += n;
					}
					refName = refName + (i == 0 ? "" : JOB_NODE_BEAB_ID_LEVEL_SEPARATOR) + name;
				} else {
					refName = parentHolder.getRefName() + JOB_NODE_BEAB_ID_LEVEL_SEPARATOR + name;
				}
			}
			BeanDefinitionBuilder nodeBeanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(className);
			nodeBeanDefinitionBuilder.addConstructorArgValue(name);
			nodeBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
			AbstractBeanDefinition beanDefinition = nodeBeanDefinitionBuilder.getBeanDefinition();
			BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
			
			delegate.parsePropertyElements(nodeElement, beanDefinition);
			BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(
				beanDefinition, refName), beanDefinitionRegistry);
			nameHolder = createNameHolder(parentHolder, name, refName);
		} else if (hasRef) {
			nameHolder = createNameHolder(parentHolder, name, ref);
		} else if (hasChildRef) {
			hasNext = false;
			nameHolder = createNameHolder(parentHolder, name, childRef);
		} else {
			throw new BeanDefinitionStoreException(parserContext.getReaderContext().getResource()
				.getDescription(), StringUtils.trimWhitespace(jobId),
				"[name] 为 '" + name + "' 的 [node] 没有配置属性 'class' 或 'ref' 或 'child-ref' 。");
		}
		if (hasNext) {
			// 如果不是child-ref则进行子标签解析
			List<Element> nodeElements = DomUtils.getChildElementsByTagName(nodeElement,
				NODE_ELEMENT);
			if (!CollectionUtils.isEmpty(nodeElements)) {
				for (Element subNodeElement : nodeElements) {
					parseNode(subNodeElement, nameHolder, parserContext, jobId,
						beanDefinitionRegistry);
				}
			}
		}
		return nameHolder;
	}
	
	private NodeNameHolder createNameHolder(NodeNameHolder parentHolder, String name, String ref) {
		NodeNameHolder nameHolder;
		nameHolder = new NodeNameHolder(name);
		nameHolder.setRefName(ref);
		nameHolder.setParent(parentHolder);
		if (parentHolder != null) {
			parentHolder.getChildren().add(nameHolder);
		}
		return nameHolder;
	}
	
}
