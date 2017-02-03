/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.common;

/**
 * 允许有标题的对象的接口。
 *
 * @author
 *
 */
public interface Captioned {

	/**
	 * 得到标题。
	 * @return 标题。
	 */
	String getCaption();
}
