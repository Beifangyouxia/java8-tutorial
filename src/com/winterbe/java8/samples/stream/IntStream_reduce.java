package com.winterbe.java8.samples.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntStream_reduce {

    /**
     * 求和
     */
    public static void main(String[] args) {

        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);

        // 方式一，底层实现同下面的
        int sum1 = list.stream().mapToInt(t -> t).sum();
        System.out.println(sum1);

        // 方式二
        int sum2 = list.stream().mapToInt(t -> t).reduce(0, (x, y) -> x + y);
        System.out.println(sum2);

        // 求和
        // 手动方式
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double bill = costBeforeTax.stream().map(cost -> cost + .12 * cost).reduce((sum, cost) -> sum + cost).get();
        System.out.println("Total : " + bill);

    }
}
