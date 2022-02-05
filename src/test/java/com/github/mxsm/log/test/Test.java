package com.github.mxsm.log.test;

import com.github.mxsm.log.test.asyncfalse.AsyncfalseConfig;
import com.github.mxsm.log.test.asyncfalse.UserServiceImpl;
import com.github.mxsm.log.test.asynctrue.AsyncTrueConfig;
import com.github.mxsm.log.test.asynctrue.UserServiceImplTrue;
import com.github.mxsm.log.test.nolog.NoLogConfig;
import com.github.mxsm.log.test.nolog.UserServiceImplNoLog;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mxsm
 * @date 2021/12/18 17:29
 * @Since 1.0.0
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 5)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Test {

    private ApplicationContext applicationContextFalse;
    private UserServiceImpl userService;

    private ApplicationContext applicationContextTrue;
    private UserServiceImplTrue userServiceImplTrue;

    private ApplicationContext applicationContextNolog;
    private UserServiceImplNoLog userServiceImplNoLog;

    @Setup
    public void init() {

        applicationContextFalse = new AnnotationConfigApplicationContext(AsyncfalseConfig.class);
        userService = applicationContextFalse.getBean(UserServiceImpl.class);


        applicationContextTrue = new AnnotationConfigApplicationContext(AsyncTrueConfig.class);
        userServiceImplTrue = applicationContextTrue.getBean(UserServiceImplTrue.class);

        applicationContextNolog = new AnnotationConfigApplicationContext(NoLogConfig.class);
        userServiceImplNoLog = applicationContextNolog.getBean(UserServiceImplNoLog.class);


    }

    @Benchmark
    public void sync(Blackhole blackhole) {
        User user = new User();
        user.setAddress(UUID.randomUUID().toString());
        user.setName(System.currentTimeMillis()+"");
        user.setAge((int)(Math.random() * 100));
        userService.addUser(user);
        blackhole.consume(user);
    }

    @Benchmark
    public void async(Blackhole blackhole) {
        User user = new User();
        user.setAddress(UUID.randomUUID().toString());
        user.setName(System.currentTimeMillis()+"");
        user.setAge((int)(Math.random() * 100));
        userServiceImplTrue.addUser(user);
        blackhole.consume(user);
    }

    @Benchmark
    public void nolog(Blackhole blackhole) {
        User user = new User();
        user.setAddress(UUID.randomUUID().toString());
        user.setName(System.currentTimeMillis()+"");
        user.setAge((int)(Math.random() * 100));
        userServiceImplNoLog.addUser(user);
        blackhole.consume(user);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(Test.class.getSimpleName())
            .result("result.json")
            .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }
}
