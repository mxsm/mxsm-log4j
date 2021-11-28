package com.github.mxsm.log.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mxsm
 * @date 2021/11/26 16:34
 * @Since 1.0.0
 */
@Configuration
public class Config {

    @Bean
    public LogAdvisor init() {
        LogAdvisor logAdvisor = new LogAdvisor(new LogPointcut());
        logAdvisor.setAdvice(new LogAdvice());
        return logAdvisor;
    }

}
