/*
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package cn.pay.ebank.process.support;

import cn.pay.ebank.process.JobContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/**
 * 通过表达式从JobContext的parameter取值作为下一个节点名称的节点。
 * <p>
 * 表达式为 <code>SPEL</code> ，并以 {@link JobContext#getParameter(Object)}
 * 为表达式第一个参数取值。
 * <p>
 * 如果取值成功，则调用 {@link JobContext#next(String)} 。
 * <p>
 * 如果没有设置表达式，则该节点不执行任何操作。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class SelectNameByParameterNode extends SelectNameNodeSupport {
	
	/**
	 * 构建一个 SelectNameByParameterNode 。
	 * 
	 * @param name 节点的名称。
	 */
	public SelectNameByParameterNode(String name) {
		super(name, new JobContextPropertyAccessor());
	}
	
	private static class JobContextPropertyAccessor implements PropertyAccessor {
		
		public void write(EvaluationContext context, Object target, String name, Object newValue)
																									throws AccessException {
		}
		
		public TypedValue read(EvaluationContext context, Object target, String name)
																						throws AccessException {
			JobContext jobContext = (JobContext) target;
			return new TypedValue(jobContext.getParameter(name));
		}
		
		@SuppressWarnings("rawtypes")
		public Class[] getSpecificTargetClasses() {
			return new Class[] { JobContext.class };
		}
		
		public boolean canWrite(EvaluationContext context, Object target, String name) {
			return false;
		}
		
		public boolean canRead(EvaluationContext context, Object target, String name)
																						throws AccessException {
			if (target != null && target instanceof JobContext) {
				return true;
			}
			return false;
		}
	}
	
}
