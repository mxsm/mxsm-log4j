package com.github.mxsm.log.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author mxsm
 * @date 2021/11/26 16:32
 * @Since 1.0.0
 */
public class LogAdvisor extends AbstractBeanFactoryPointcutAdvisor{

    private Pointcut logPointcut;

    public LogAdvisor(Pointcut logPointcut) {
        this.logPointcut = logPointcut;
    }

    /**
     * Get the Pointcut that drives this advisor.
     */
    @Override
    public Pointcut getPointcut() {
        return this.logPointcut;
    }
}
