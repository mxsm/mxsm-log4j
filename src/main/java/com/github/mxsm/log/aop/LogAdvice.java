package com.github.mxsm.log.aop;

import com.github.mxsm.log.annotation.Log;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author mxsm
 * @date 2021/11/26 16:31
 * @Since 1.0.0
 */
public class LogAdvice implements MethodInterceptor, BeanFactoryAware {

    private Logger logger;

    private SpelExpressionParser parser;

    private TemplateParserContext templateParserContext = new TemplateParserContext("${", "}");

    private LogCachedExpressionEvaluator logCachedExpressionEvaluator;

    private ExecutorService logExecutor;

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Map<AnnotatedElementKey, StandardEvaluationContext> secCache = new ConcurrentHashMap<>();

    private BeanFactory beanFactory;

    private boolean async;

    private final String loggerName;

    public LogAdvice() {
        this(false, null);
    }

    public LogAdvice(boolean async, String loggerName) {
        this.parser = new CustomSpelExpressionParser(this.templateParserContext);
        this.logCachedExpressionEvaluator = new LogCachedExpressionEvaluator(this.parser, this.templateParserContext);
        this.async = async;
        this.loggerName = loggerName;
        if (async) {
            initLogExecutor();
        }
        if (null == loggerName || loggerName.trim().isEmpty()) {
            logger = LoggerFactory.getLogger(LogAdvice.class);
        } else {
            logger = LoggerFactory.getLogger(this.loggerName );
        }
    }

    private void initLogExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        this.logExecutor = new ThreadPoolExecutor(processors * 2, processors * 2, 60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024),
            new ThreadFactory() {

                private AtomicInteger threadNum = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable target) {
                    return new Thread(target, "LogThread_" + threadNum.incrementAndGet());
                }
            }, new ThreadPoolExecutor.DiscardOldestPolicy());
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

        try {
            LogWorker worker = new LogWorker(target, method, args);
            if (this.async) {
                logExecutor.submit(worker);
            } else {
                worker.run();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.warn("Failure to record logs!", e);
        }

        return invoker.proceed();
    }

    /**
     * Callback that supplies the owning factory to a bean instance.
     * <p>Invoked after the population of normal bean properties
     * but before an initialization callback such as {@link InitializingBean#afterPropertiesSet()} or a custom
     * init-method.
     *
     * @param beanFactory owning BeanFactory (never {@code null}). The bean can immediately call methods on the
     *                    factory.
     * @throws BeansException in case of initialization errors
     * @see BeanInitializationException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    class LogWorker implements Runnable {

        private Object target;

        private Method method;

        private Object[] args;

        public LogWorker(Object target, Method method, Object[] args) {
            this.target = target;
            this.method = method;
            this.args = args;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
         * causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {

            Log logAnnotation = method.getAnnotation(Log.class);
            AnnotatedElementKey elementKey = new AnnotatedElementKey(method, target.getClass());
            StandardEvaluationContext sec = secCache.get(elementKey);
            if (sec == null) {
                sec = new StandardEvaluationContext();
                sec.setBeanResolver(new BeanFactoryResolver(beanFactory));
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                if (null != parameterNames && parameterNames.length > 0) {
                    for (int index = 0; index < parameterNames.length; ++index) {
                        sec.setVariable(parameterNames[index], args[index]);
                    }
                }
                secCache.put(elementKey, sec);
            }
            String value = logCachedExpressionEvaluator.parseExpression(logAnnotation.template(), elementKey, sec);
            logger.info(value);
        }
    }

}
