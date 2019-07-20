package com.winterbe.java8.samples.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * filter 方法各种场景
 */
public class Stream_filter {

    public static void main(String[] args) {
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        // filter
        stringCollection.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);
        // "aaa2", "aaa1"

    }
}
