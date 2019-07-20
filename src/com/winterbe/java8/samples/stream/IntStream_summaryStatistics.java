package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * 计算的统计摘要方法
 */
public class IntStream_summaryStatistics {

    public static void main(String[] args) {

        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);

        // 获取数字的个数、最小值、最大值、总和以及平均值
        IntSummaryStatistics stats = primes.stream().mapToInt(x -> x).summaryStatistics();

        System.out.println("最大值 : " + stats.getMax());
        System.out.println("最小值 : " + stats.getMin());
        System.out.println("总和 : " + stats.getSum());
        System.out.println("平均值 : " + stats.getAverage());
    }
}
