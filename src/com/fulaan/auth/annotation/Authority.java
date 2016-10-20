package com.fulaan.auth.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author xusy
 * <br>
 *	自定义权限注解，需要权限控制的需要增加该注解
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Authority {
	
	ModuleType module();
	
	AuthFunctionType function();
	
}
