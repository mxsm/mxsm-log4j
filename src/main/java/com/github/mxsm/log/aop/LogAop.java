package com.github.mxsm.log.aop;

import com.github.mxsm.controller.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author mxsm
 * @date 2021/11/26 12:02
 * @Since 1.0.0
 */
/*@Component
@Aspect*/
public class LogAop {

    private Logger logger = LoggerFactory.getLogger(LogAop.class);

    @Pointcut("@annotation(com.github.mxsm.log.annotation.Log)")
    public void pointCut(){

    }

    @Around("pointCut()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {

        Signature signature = pjp.getSignature();
        String name = signature.getName();
        Class declaringType = signature.getDeclaringType();

        Object retVal = pjp.proceed();

        return retVal;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setAge(1);
        user.setName("aaaaa");
        ExpressionParser parser = new SpelExpressionParser();
        SimpleEvaluationContext build = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        build.setVariable("user",user);

        TemplateParserContext context = new TemplateParserContext();
        Expression expression = parser.parseExpression("#{user.name}",context);
        String value = (String)expression.getValue(build);
        System.out.println(value);
        /*ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("str", "a\0b");

        Expression ex = parser.parseExpression("#str?.split('\0')");
        Object result = ex.getValue(context);
        System.out.println(result);*/
    }

}

