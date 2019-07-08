package com.winterbe.java8.samples.functional;

import com.winterbe.java8.samples.util.Score;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Function1 {

    public static void main(String[] args) {

        Function<String, String> f1 = s -> {
            String _s = s + " world！";
            System.out.println(_s);
            return _s;
        };

        Function<String, String> f2 = s -> {
            String _temp = s + " andThen logistics!";
            System.out.println(_temp);
            return _temp;
        };

        // 对输入的参数，先执行f1表达式逻辑，再执行f2表达式逻辑
        f1.andThen(f2).apply("hello");

        // 无参构造器
        Supplier<Score> supplier1 = Score::new;
        System.out.println("创建了新对象（无参数）：" + supplier1.get());

        // 一个参数构造器
        Function<String, Score> function1 = Score::new;
        System.out.println("创建了新对象（一个参数）：" + function1.apply("Tom哥"));

        // 二个参数构造器
        BiFunction<String, Long, Score> biFunction1 = Score::new;
        System.out.println("创建了新对象（一个参数）：" + biFunction1.apply("Tom哥", 98L));

        // 级联表达式
        // 级联表达式就是多个lambda表达式的组合，这里涉及到一个高阶函数的概念，所谓高阶函数就是一个可以返回函数的函数
        Function<Integer, Function<Integer, Integer>> function2 = x -> y -> x + y;
        System.out.println("计算结果为: " + function2.apply(2).apply(3)); // 计算结果为: 5

        Function<Integer, Function<Integer, Function<Integer, Integer>>> function3 = x -> y -> z -> (x + y) * z;
        System.out.println("计算结果为: " + function3.apply(1).apply(2).apply(3)); // 计算结果为: 9

    }
}
