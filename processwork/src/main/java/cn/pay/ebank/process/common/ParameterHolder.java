/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhyang@ebank.pay.cn 2015-04-17 15:36 创建
 *
 */
package cn.pay.ebank.process.common;

import java.util.Collection;
import java.util.Set;

/**
 * 参数持有器
 *
 * @author zhyang@ebank.pay.cn
 */
public interface ParameterHolder {

	/**
	 * 设置一组参数。
	 *
	 * @param name  参数的名称。
	 * @param value 名称对应的值。
	 */
	void setParameter (Object name, Object value);

	/**
	 * 通过name得到对应的值。
	 *
	 * @param name 需要得到值的name。
	 * @return name对应的值，如果没有name对应的值则返回null。
	 */
	Object getParameter (Object name);

	/**
	 * 移除name所对应的值。
	 *
	 * @param name 需要移除的值的name。
	 * @return 被移除的值。
	 */
	Object removeParameter (Object name);

	/**
	 * 得到所有参数名称的集合。
	 *
	 * @return 所有参数名称的集合。
	 */
	Set<Object> getParameterNames ();

	/**
	 * 得到所有参数值的集合。
	 *
	 * @return 所有参数值的集合。
	 */
	Collection<Object> getParameterValues ();

	/**
	 * 判断上下文中是否有name所对应的参数。
	 *
	 * @param name 需要判断的参数的name。
	 * @return 如果存在name对应的参数则返回true。
	 */
	boolean hasParameter (Object name);

	/**
	 * 复制当前参数持有器的参数到 parameterHolder 中。
	 *
	 * @param parameterHolder 复制参数到的 parameterHolder 。
	 */
	void copy (ParameterHolder parameterHolder);
}
