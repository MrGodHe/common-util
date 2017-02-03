/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config.xml;

import cn.pay.ebank.process.config.BeanNameHolder;
import cn.pay.ebank.process.dynamic.config.DynamicNodeLoader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 动态节点加载器工厂bean。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DynamicNodeLoadersFactoryBean implements FactoryBean<List<DynamicNodeLoader>>,
											ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private List<BeanNameHolder> nodeLoaderNameHolders;
	
	private volatile List<DynamicNodeLoader> dynamicNodeLoaders;
	
	private final Object lock = new Object();
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void setNodeLoaderNameHolders(List<BeanNameHolder> nodeLoaderNameHolders) {
		this.nodeLoaderNameHolders = nodeLoaderNameHolders;
	}
	
	public List<DynamicNodeLoader> getObject() throws Exception {
		if (this.dynamicNodeLoaders == null) {
			synchronized (this.lock) {
				if (this.dynamicNodeLoaders == null) {
					if (this.nodeLoaderNameHolders == null) {
						this.nodeLoaderNameHolders = Collections.emptyList();
					}
					List<DynamicNodeLoader> dynamicNodeLoaders = new ArrayList<DynamicNodeLoader>(
						this.nodeLoaderNameHolders.size());
					for (BeanNameHolder beanNameHolder : this.nodeLoaderNameHolders) {
						DynamicNodeLoader dynamicNodeLoader = this.applicationContext.getBean(
							beanNameHolder.getRefName(), DynamicNodeLoader.class);
						dynamicNodeLoaders.add(dynamicNodeLoader);
					}
					this.dynamicNodeLoaders = dynamicNodeLoaders;
				}
			}
		}
		return this.dynamicNodeLoaders;
	}
	
	public Class<?> getObjectType() {
		return List.class;
	}
	
	public boolean isSingleton() {
		return true;
	}
	
}
