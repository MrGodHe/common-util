/*
 * www.yiji.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package cn.pay.ebank.process.support;

import cn.pay.ebank.process.AbstractNode;
import cn.pay.ebank.process.JobContext;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

/**
 * 通过表达式从JobContext取值作为下一个节点名称的支持节点。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public abstract class SelectNameNodeSupport extends AbstractNode {
	
	protected final ExpressionParser expressionParser;
	
	protected String expression;
	
	protected final PropertyAccessor[] jobContextAccessors;
	
	/**
	 * 构建一个 SelectNameNodeSupport 。
	 * 
	 * @param name 节点的名称。
	 * @param propertyAccessors 熟悉处理器组。
	 */
	protected SelectNameNodeSupport(String name, PropertyAccessor... propertyAccessors) {
		super(name);
		this.expressionParser = new SpelExpressionParser();
		this.jobContextAccessors = propertyAccessors;
	}
	
	/**
	 * 设置取值的表达式。
	 * @param expression 取值表达式。
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void invoke(JobContext context) throws Exception {
		if (StringUtils.hasLength(this.expression)) {
			Expression exp = this.expressionParser.parseExpression(this.expression);
			StandardEvaluationContext evaluationContext = new StandardEvaluationContext(context);
			if (ArrayUtils.isNotEmpty(this.jobContextAccessors)) {
				for (PropertyAccessor jobContextAccessor : this.jobContextAccessors) {
					evaluationContext.addPropertyAccessor(jobContextAccessor);
				}
			}
			String value = exp.getValue(evaluationContext, String.class);
			doInvoke(context, value);
		}
	}
	
	/**
	 * 由 {@link #invoke(JobContext)} 调用执行跳转标记的方法。
	 * @param context 任务上下文。
	 * @param value 表达式的值。
	 */
	protected void doInvoke(JobContext context, String value) {
		context.next(value);
	}
	
}
