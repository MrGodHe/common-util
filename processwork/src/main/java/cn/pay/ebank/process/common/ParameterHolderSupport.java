/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhyang@ebank.pay.cn 2015-04-17 15:37 创建
 *
 */
package cn.pay.ebank.process.common;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 非线程安全的参数持有器
 *
 * @author zhyang@ebank.pay.cn
 */
public class ParameterHolderSupport implements ParameterHolder {

	private final Map<Object, Object> params = new LinkedHashMap<Object, Object> ();

	public void setParameter (Object name, Object value) {
		this.params.put (name, value);
	}

	public Object getParameter (Object name) {
		return this.params.get (name);
	}

	public Object removeParameter (Object name) {
		return this.params.remove (name);
	}

	public Set<Object> getParameterNames () {
		return this.params.keySet ();
	}

	public Collection<Object> getParameterValues () {
		return this.params.values ();
	}

	public boolean hasParameter (Object name) {
		return this.params.containsKey (name);
	}

	public void copy (ParameterHolder parameterHolder) {
		if (parameterHolder == null) {
			return;
		}
		if (parameterHolder instanceof ParameterHolderSupport) {
			ParameterHolderSupport phs = (ParameterHolderSupport) parameterHolder;
			phs.params.putAll (this.params);
			return;
		}
		for (Map.Entry<Object, Object> entry : this.params.entrySet ()) {
			parameterHolder.setParameter (entry.getKey (), entry.getValue ());
		}
	}
}
