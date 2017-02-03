/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * Job的异常。
 *
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 */
public class JobException extends RuntimeException {
	
	private static final long serialVersionUID = 5254741258834158650L;
	
	public JobException (String msg) {
		super (msg);
	}
	
	public JobException (String msg, Throwable cause) {
		super (msg, cause);
	}
	
}
