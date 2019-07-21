package com.winterbe.java8.samples.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * match 方法各种场景
 */
public class Stream_match {

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

        // 只需要一个条件满足
        boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
        System.out.println("anyMatch：" + anyStartsWithA); // true

        // 所有条件都要满足
        boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
        System.out.println("allMatch：" + allStartsWithA); // false

        // 所有的条件都要不满足
        boolean noneStartsWithZ = stringCollection.stream().noneMatch((s) -> s.startsWith("z"));
        System.out.println("noneMatch：" + noneStartsWithZ); // true

        // 返回任意一个元素
        Optional<String> anyE = stringCollection.stream().findAny();
        System.out.println("findAny：" + anyE.get());

        //返回第一个元素
        Optional<String> firstE = stringCollection.stream().findFirst();
        System.out.println("findFirst：" + firstE.get());

    }

}
