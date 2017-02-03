/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.dynamic.config;


import cn.pay.ebank.process.common.Immutable;

import java.util.Set;

/**
 * 基于 class 文件的动态节点加载器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 * 
 */
public interface ClassedDynamicNodeLoader extends DynamicNodeLoader {
	
	/**
	 * 得到扫描注解的基本包路径集合。
	 * @return 扫描注解的基本包路径集合。
	 */
	@Immutable
	public Set<String> getBasePackages ();
	
	/**
	 * 设置扫描注解的基本包路径集合。
	 * @param basePackages 扫描注解的基本包路径集合。
	 */
	void setBasePackages (Set<String> basePackages);
	
	/**
	 * 添加一个扫描注解的基本包路径。
	 * @param basePackage 要添加的扫描注解的基本包路径。
	 * @return 如果造成扫描注解的基本包路径集合发生改变返回 true 。
	 */
	boolean addBasePackage (String basePackage);
	
	/**
	 * 移除一个扫描注解的基本包路径。
	 * @param basePackage 要移除的扫描注解的基本包路径。
	 * @return 如果造成扫描注解的基本包路径集合发生改变返回 true 。
	 */
	boolean removeBasePackage (String basePackage);
	
	/**
	 * 判定扫描注解的基本包路径是否在集合中。
	 * @param basePackage 要判定的扫描注解的基本包路径。
	 * @return 存在集合中返回 true ，否则返回 false 。
	 */
	boolean hasBasePackage (String basePackage);
	
	/**
	 * 得到加载的 classpath 集合。
	 * @return 加载的 classpath 集合，从该路径列表中的路径加载类。
	 */
	@Immutable
	Set<String> getClasspaths ();
	
	/**
	 * 设置加载的 classpath 集合。
	 * @param classpaths 加载的 classpath 集合。
	 */
	void setCpasspaths (Set<String> classpaths);
	
	/**
	 * 添加一个加载的 classpath 到集合中。
	 * @param classpath 加载的 classpath 。
	 * @return 如果造成集合改变返回 true 。
	 */
	boolean addClasspath (String classpath);
	
	/**
	 * 从集合中移除一个加载的 classpath 。
	 * @param classpath 移除的 classpath 。
	 * @return 如果造成集合改变返回 true 。
	 */
	boolean removeClasspath (String classpath);
	
	/**
	 * 判定一个 classpath 是否在集合中。
	 * @param classpath 需要判定的 classpath 。
	 * @return 在集合中返回 true ，否则返回 false 。
	 */
	boolean hasClasspath (String classpath);
	
}
