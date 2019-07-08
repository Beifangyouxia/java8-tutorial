package com.winterbe.java8.samples.functional;

import java.util.function.Function;

/**
 * @author onlyone
 */
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

        // 对输入的参数，先执行f1逻辑，再执行f2逻辑
        f1.andThen(f2).apply("hello");

    }
}
