package com.winterbe.java8.samples.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author onlyone
 */
public class Reduce1 {

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

    }
}
