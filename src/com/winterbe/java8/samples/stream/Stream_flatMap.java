package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author onlyone
 */
public class Stream_flatMap {

    public static void main(String[] args) {

//        test1();
        test2();
    }

    private static void test1() {
        // map只能是一对一，如果是一对多，会有问题
        List<String> lists = Arrays.asList("Hello", "World");
        lists.stream().map(word->word.split(""))
                .distinct()
                .forEach(System.out::println);
    }

    private static void test2() {
        // map只能是一对一，如果是一对多，需要封装到Stream中
        List<String> lists = Arrays.asList("Hello", "World");
        lists.stream().flatMap(word-> Stream.of(word.split("")))
                .distinct()
                .forEach(System.out::println);
    }
}
