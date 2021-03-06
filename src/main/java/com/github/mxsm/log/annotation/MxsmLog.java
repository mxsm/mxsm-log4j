package com.github.mxsm.log.annotation;

import com.github.mxsm.log.OperateType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mxsm
 * @date 2021/11/26 11:36
 * @Since 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MxsmLog {

    /**
     * log template
     * @return
     */
    String template() default "";

    /**
     *
     * @return
     */
    OperateType operateType() default OperateType.UNKNOWN;
}
