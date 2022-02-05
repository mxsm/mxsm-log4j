package com.github.mxsm.log.aop;

import com.github.mxsm.log.EnableMxsmLog;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mxsm
 * @date 2021/11/26 16:34
 * @Since 1.0.0
 */
@Configuration
public class LogConfig implements ImportAware {

    private boolean async = false;

    private String loggerName;

    @Bean("com.github.mxsm.log.aop.logAdvisor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogAdvisor createLogAdvisor(LogAdvice logAdvice, LogPointcut logPointcut) {
        LogAdvisor logAdvisor = new LogAdvisor(logPointcut);
        logAdvisor.setAdvice(logAdvice);
        return logAdvisor;
    }

    @Bean("com.github.mxsm.log.aop.logAdvice")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogAdvice createLogAdvice() {
        return new LogAdvice(this.async, this.loggerName);
    }

    @Bean("com.github.mxsm.log.aop.logPointcut")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogPointcut createLogPointcut() {
        return new LogPointcut();
    }

    /**
     * Set the annotation metadata of the importing @{@code Configuration} class.
     *
     * @param importMetadata
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(EnableMxsmLog.class.getName(), false));
        this.async = attributes.getBoolean("async");
        this.loggerName = attributes.getString("loggerName");
    }
}
