package com.winterbe.java8.samples.stream;

import com.winterbe.java8.samples.util.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * sorted 方法各种场景
 */
public class Stream_sorted {

    public static void main(String[] args) {

//        test1();
        test2();
    }

    private static void test1(){
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        // 默认排序
        stringCollection.stream().sorted().forEach(System.out::println);
        System.out.println(stringCollection);


        //自定义排序
        //按字符串，由大到小
        stringCollection
                .stream()
                .map(String::toUpperCase)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);
        // "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
    }

    private static  void test2(){
        List<Score> list = new ArrayList<>();
        list.add(new Score("xiaohong", 90L, 91L));
        list.add(new Score("xiaoming", 85L, 90L));
        list.add(new Score("wanggang", 90L, 96L));
        list.add(new Score("xiaoma", 85L, 70L));

        //排序支持传入表达式
        //按语文由小到大
        list.stream().sorted(Comparator.comparing(Score::getYuwen)).forEach(System.out::println);

        //按语文由大到小
        list.stream().sorted(Comparator.comparing(Score::getYuwen).reversed()).forEach(System.out::println);
    }

}
