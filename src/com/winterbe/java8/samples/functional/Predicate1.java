package com.winterbe.java8.samples.functional;

import com.winterbe.java8.samples.util.Score;

import java.util.function.Predicate;

/**
 * @author onlyone
 */
public class Predicate1 {

    public static void main(String[] args) {
        Score s1 = new Score("xiaohong", 90L, 91L);

        Predicate<Score> p1 = score -> {
            return score.getYuwen() > 90;
        };

        Predicate<Score> p2 = score -> {
            return score.getShuxue() > 89;
        };

        // 两个条件表达式，逻辑与
        System.out.println(p1.and(p2).test(s1));

        // 两个条件表达式，逻辑或
        System.out.println(p1.or(p2).test(s1));
    }
}
