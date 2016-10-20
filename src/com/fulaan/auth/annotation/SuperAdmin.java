package com.fulaan.auth.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *	对添加该注解的权限进行特殊处理
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface SuperAdmin {
	
}
