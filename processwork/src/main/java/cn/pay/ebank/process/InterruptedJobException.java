/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process;

/**
 * 在Job被发送中断指令时，由Job的实现相应中断并抛出该异常。
 *
 * @author
 */
public class InterruptedJobException extends RuntimeException {
	
	private static final long serialVersionUID = -2780041958926960916L;
	
	public InterruptedJobException () {
		super ();
	}
	
	public InterruptedJobException (String msg) {
		super (msg);
	}
	
}
