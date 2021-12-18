package com.github.mxsm.log.aop;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author mxsm
 * @date 2021/12/18 16:40
 * @Since 1.0.0
 */
public class CustomSpelExpressionParser extends SpelExpressionParser {

    private ParserContext parserContext;

    public CustomSpelExpressionParser(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Expression parseExpression(String expressionString) throws ParseException {
        //兼容Spring 5.3.13以前的版本
        return parserContext == null ? super.parseExpression(expressionString)
            : super.parseExpression(expressionString, this.parserContext);
    }

}
