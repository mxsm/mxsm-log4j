package com.github.mxsm.log;

import com.github.mxsm.log.selector.LogImportSelector;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author mxsm
 * @date 2021/12/17 22:04
 * @Since 1.0.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(LogImportSelector.class)
public @interface EnableLog {

    boolean value() default true;

    /**
     * use asynchronization method to record log
     * @return
     */
    boolean async() default false;

    /**
     * log name
     * @return
     */
    String loggerName() default "";

    boolean proxyTargetClass() default false;

}
