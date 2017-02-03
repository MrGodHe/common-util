/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */
package cn.pay.ebank.process.common;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 表示一个对象是不可变的。
 * <p>
 * 作用于类或者接口上时，则指示类实例是不可变对象或者要求接口的实现是不可变的。
 * <p>
 * 作用于方法参数上时，要求被注解的参数必须为不可变的或者需要将该参数当做不可变的参数对待。
 * <p>
 * 作用于字段上时，表示字段为不可变的。
 * <p>
 * 作用于方法时，表示返回的值是不可变的。
 *
 * @author zhyang (e-mail:zhyang@ebank.pay.cn)
 *
 */
@Documented
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Retention(SOURCE)
public @interface Immutable {
}
