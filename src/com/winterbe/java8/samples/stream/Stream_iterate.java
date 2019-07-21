package com.winterbe.java8.samples.stream;

import java.util.stream.Stream;

/**
 * @author onlyone
 */
public class Stream_iterate {
    public static void main(String[] args) {
        test1();
    }

    private static void test1(){
        //创建无限流，一定要与limit配合使用
        Stream.iterate(0,n->n+2)
                .limit(10)
                .forEach(System.out::println);
    }
}
