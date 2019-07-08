package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * @author onlyone
 */
public class SummaryStatistics1 {

    public static void main(String[] args) {

        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);

        // 获取数字的个数、最小值、最大值、总和以及平均值
        IntSummaryStatistics stats = primes.stream().mapToInt(x -> x).summaryStatistics();

        System.out.println("Highest prime number in List : " + stats.getMax());

        System.out.println("Lowest prime number in List : " + stats.getMin());

        System.out.println("Sum of all prime numbers : " + stats.getSum());

        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }
}
