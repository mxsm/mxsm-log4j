package com.github.mxsm.controller;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author mxsm
 * @date 2021/11/27 9:25
 * @Since 1.0.0
 */
public class User {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        String greetingExp = "Hello, #{#user} ---> #{T(System).getProperty('user.home')}";
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", "fsx");

        Expression expression = parser.parseExpression(greetingExp, new
            TemplateParserContext());
        System.out.println(expression.getValue(context, String.class));
    }
}
