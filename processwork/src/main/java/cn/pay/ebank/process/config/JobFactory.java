/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.config;


import cn.pay.ebank.process.Job;

/**
 * Job 工厂。
 * 
 * @author
 * 
 */
public interface JobFactory<J extends Job> {
	
	/**
	 * 得到一个新的Job。
	 * @return job。
	 * @throws Exception 发生异常时。
	 */
	J getJob () throws Exception;
	
}
