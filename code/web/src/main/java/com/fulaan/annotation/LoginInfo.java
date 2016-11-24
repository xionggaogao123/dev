package com.fulaan.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by jerry on 2016/9/8.
 * 需要将一些登录信息放在model里的方法
 * 加上此注解
 */

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginInfo {
}
