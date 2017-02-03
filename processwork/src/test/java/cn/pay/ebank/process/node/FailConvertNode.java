package cn.pay.ebank.process.node;

import cn.pay.ebank.process.domain.LoginDomain;
import cn.pay.ebank.process.domain.LoginResult;
import cn.pay.ebank.process.domain.SimpleConvertNode;

/**
 * 登录失败的转换节点，通过继承转换节点简化操作。
 *
 * @author Agreal·Lee
 * @version 1.0
 * @filename FailConvertNode.java
 * @Description
 * @email lixiang@yiji.com
 * @history <li>Author: Agrael·Lee</li>
 * <li>Date: 2012-8-10</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public class FailConvertNode extends SimpleConvertNode<LoginDomain, LoginResult> {
	
	public FailConvertNode (String name) {
		super (name);
	}

	@Override
	protected LoginResult convert (LoginDomain domain) {
		LoginResult loginResult = new LoginResult ();
		loginResult.setStatus ("FAIL");
		loginResult.setDescription ("用户 '" + domain.getUsername () + "' 登录失败。");
		getJobContext ().next ();
		return loginResult;
	}
	
}
