package com.tunan.java.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TunanBean {

    String table();
    String from() default "tunan";

}
