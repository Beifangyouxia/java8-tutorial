package com.winterbe.java8.samples.functional;

import com.winterbe.java8.samples.util.Score;

import java.util.function.BiFunction;

/**
 * @author onlyone
 */
public class BiFunction1 {

    public static void main(String[] args) {
        Score s1 = new Score("xiaohong", 90L, 91L);

        // Function的升级版，支持两个入参
        BiFunction<String, Long, String> biFunction = (t1, t2) -> {
            return t1 + "的成绩单，语文：" + t2;
        };
        // 结果：xiaohong的语文成绩：90
        s1.printYuwenScore(biFunction);

    }
}
