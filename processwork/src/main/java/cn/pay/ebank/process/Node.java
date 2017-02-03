/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

import cn.pay.ebank.process.common.Captioned;
import cn.pay.ebank.process.common.Immutable;
import cn.pay.ebank.process.common.Named;

/**
 * 任务节点，每个节点代表着每一个流程点需要做的事情的具体业务逻辑。
 * <p>
 * 注意：节点的实现需要为无状态的，一个节点的实例会在不同的任务中重复使用。
 *
 * @author
 *
 */
@Immutable
public interface Node extends Named, Captioned {
	
	/**
	 * 请求执行当前任务节点。
	 *
	 * @param context 任务上下文。
	 * @throws Exception 如果发生异常时。
	 */
	void invoke (JobContext context) throws Exception;
	
	/**
	 * 处理由 {@link #invoke(JobContext)} 发生的异常。
	 *
	 * @param context 任务上下文。
	 * @param exception 发生的异常。
	 * @throws Exception 重新抛出的异常。
	 */
	void resolveException (JobContext context, Exception exception) throws Exception;
	
}
