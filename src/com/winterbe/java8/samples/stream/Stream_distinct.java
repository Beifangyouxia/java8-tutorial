package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlyone
 */
public class Stream_distinct {

    public static void main(String[] args) {
        // 去重
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.println("原数据 : " + numbers);
        System.out.println("去重后的数据 : " + distinct);
    }
}
