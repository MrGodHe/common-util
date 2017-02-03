/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.JobDefinitionAttributeNames;
import cn.pay.ebank.process.dynamic.DynamicJobApplicationContext;
import cn.pay.ebank.process.dynamic.config.AbstractClassedDynamicNodeLoader;
import cn.pay.ebank.process.dynamic.config.ClassedDynamicNodeLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 基于xml配置与class文件的动态节点加载器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class XmlClassedDynamicNodeLoader extends AbstractClassedDynamicNodeLoader
																					implements
																					ClassedDynamicNodeLoader,
																					JobDefinitionAttributeNames {
	
	/** 配置文件资源路径列表 */
	protected List<Resource> configResources;
	
	/**
	 * 设置配置文件资源路径列表。
	 * @param configResources 配置文件资源路径列表。
	 */
	public void setConfigResources(List<Resource> configResources) {
		this.configResources = configResources;
	}
	
	public void load(DynamicJobApplicationContext applicationContext) throws Exception {
		// 读取 spring 能解析的信息
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
			applicationContext);
		beanDefinitionReader.setNamespaceHandlerResolver(new DynamicJobNamespaceHandlerResolver());
		if (!CollectionUtils.isEmpty(this.configResources)) {
			for (Resource configResource : this.configResources) {
				beanDefinitionReader.loadBeanDefinitions(configResource);
			}
		}
		applicationContext.refresh();
	}
	
}
