package com.mierro.robot.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 工具类
 * Created by tlseek on 2017/8/18.
 */
public class Tool {


    /**
     * 获取 T
     * @param t
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getOrDefault(T t, T defaultValue){
        return t == null ? defaultValue : t;
    }

    /**
     * 获取 T
     * @param t
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T getOrDefault(T t, Supplier<T> supplier){
        return t == null ? supplier.get() : t;
    }

    /**
     * 随机获取 区间之间的数字
     * @param min
     * @param max
     * @return
     */
    public static Integer randomBetweem(Integer min, Integer max){
        if (min >= max) {
            throw new RuntimeException("最小值大于或等于最大值");
        }
        return new Random().nextInt(max - min) + min;
    }

    /**
     * 默认异常处理类
     */
    public static final Consumer<Throwable> THROWABLE_TO_RUNTIME_EXCEPTION = t -> {
        if (t instanceof Error)
            throw (Error) t;

        if (t instanceof RuntimeException)
            throw (RuntimeException) t;

        if (t instanceof IOException)
            throw new UncheckedIOException((IOException) t);

        // [#230] Clients will not expect needing to handle this.
        if (t instanceof InterruptedException)
            Thread.currentThread().interrupt();

        throw new RuntimeException(t);
    };

    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return consumer(consumer, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer, Consumer<Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            }
            catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer, BiConsumer<T, Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            }
            catch (Throwable e) {
                handler.accept(t,e);
            }
        };
    }

    @FunctionalInterface
    public interface CheckedConsumer<T> {

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t) throws Throwable;
    }

    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return function(function, THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                handler.accept(e);
                return null;
            }
        };
    }

    /**
     *
     * @author Lukas Eder
     */
    @FunctionalInterface
    public interface CheckedFunction<T, R> {

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(T t) throws Throwable;

    }
}
