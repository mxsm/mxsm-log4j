package com.github.mxsm.log.aop;

import com.github.mxsm.log.annotation.Log;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * @author mxsm
 * @date 2021/11/26 16:31
 * @Since 1.0.0
 */
public class LogAdvice implements MethodInterceptor {

    private SpelExpressionParser parser;

    private TemplateParserContext templateParserContext = new TemplateParserContext("${", "}");

    private LogCachedExpressionEvaluator logCachedExpressionEvaluator;

    public LogAdvice() {
        this.parser = new SpelExpressionParser();
        this.logCachedExpressionEvaluator = new LogCachedExpressionEvaluator(this.parser, this.templateParserContext);
    }

    /**
     * Implement this method to perform extra treatments before and after the invocation. Polite implementations would
     * certainly like to invoke {@link Joinpoint#proceed()}.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the call to {@link Joinpoint#proceed()}; might be intercepted by the interceptor
     * @throws Throwable if the interceptors or the target object throws an exception
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();

        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {

        Log logAnnotation = method.getAnnotation(Log.class);

        SimpleEvaluationContext eltContext = SimpleEvaluationContext.forReadWriteDataBinding().build();
        Parameter[] parameters = method.getParameters();
        if (null != parameters && parameters.length > 0) {
            for (int index = 0; index < parameters.length; ++index) {
                eltContext.setVariable(parameters[index].getName(), args[index]);
            }
        }
        String value = this.logCachedExpressionEvaluator.parseExpression(logAnnotation.template(),
            new AnnotatedElementKey(method, target.getClass()), eltContext);
        System.out.println(value);
        return invoker.proceed();
    }




}
