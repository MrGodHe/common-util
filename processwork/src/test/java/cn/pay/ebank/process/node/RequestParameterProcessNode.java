package cn.pay.ebank.process.node;

import cn.pay.ebank.process.domain.LoginDomain;
import cn.pay.ebank.process.domain.LoginResult;
import cn.pay.ebank.process.domain.SimpleDomainNode;

/**
 * 请求参数处理节点，向领域设置请求的参数（模拟得到请求参数），通过继承领域节点简化操作。
 *
 * @version 1.0
 * @filename RequestParameterProcessNode.java
 * @Description
 * <li>Date: 2012-8-10</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class RequestParameterProcessNode extends SimpleDomainNode<LoginDomain, LoginResult> {
	
	public RequestParameterProcessNode (String name) {
		super (name);
	}

	@Override
	protected void doJobProcess (LoginDomain domain) {
		domain.setUsername ("lixiang");
		domain.setPassword ("111111");
		getJobContext ().next ();
	}
	
}
