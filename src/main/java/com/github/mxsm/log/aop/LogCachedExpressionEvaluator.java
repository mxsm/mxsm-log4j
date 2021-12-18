package com.github.mxsm.log.aop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author mxsm
 * @date 2021/11/27 22:37
 * @Since 1.0.0
 */
public class LogCachedExpressionEvaluator extends CachedExpressionEvaluator {


    private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>();

    private ParserContext parserContext;

    /**
     * Create a new instance with the specified {@link SpelExpressionParser}.
     *
     * @param parser
     */
    public LogCachedExpressionEvaluator(SpelExpressionParser parser) {
        super(parser);
    }

    /**
     * Create a new instance with a default {@link SpelExpressionParser}.
     */
    public LogCachedExpressionEvaluator(SpelExpressionParser parser,ParserContext parserContext) {
        super(parser);
        this.parserContext = parserContext;
    }


    public String parseExpression(String conditionExp, AnnotatedElementKey methodKey, EvaluationContext eltContext) {
        return getExpression(this.expressionCache, methodKey, conditionExp).getValue(eltContext, String.class);
    }

    /**
     * Parse the specified {@code expression}.
     *
     * @param expression the expression to parse
     * @since 5.3.13
     */
    @Override
    public Expression parseExpression(String expression) {

        if(parserContext == null){
            return super.parseExpression(expression);
        }
        return getParser().parseExpression(expression,this.parserContext);
    }

}
