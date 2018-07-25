package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.List;

/**
 * @author Benjamin Winterberg
 */
public class Streams11 {

    static class Person {

        String name;
        int    age;

        Person(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23),
                                             new Person("David", 12));

        // test1(persons);
        // test2(persons);
        // test3(persons);
        // test4(persons);
        // test5(persons);
        test6(persons);
    }

    // 找到age最大的Person
    private static void test1(List<Person> persons) {
        persons.stream().reduce((p1, p2) -> p1.age > p2.age ? p1 : p2).ifPresent(System.out::println); // Pamela
    }

    // 对identity+stream列表进行函数表达式操作
    private static void test2(List<Person> persons) {
        Person result = persons.stream().reduce(new Person("", 0), (p1, p2) -> {
            p1.age += p2.age;
            p1.name += p2.name;
            return p1;
        });

        System.out.format("name=%s; age=%s", result.name, result.age);
    }

    // (sum1, sum2) -> sum1 + sum2)主要适用于并行，parallelStream（），单线程是无效的。
    // 将流数据列表拆分多批，每批都执行 (sum, p) -> sum += p.age，得到局部的sum总和
    // 最后通过 (sum1, sum2) -> sum1 + sum2)，计算最终的总和
    private static void test3(List<Person> persons) {
        Integer ageSum = persons.stream().reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);
    }

    // 同上
    private static void test4(List<Person> persons) {
        Integer ageSum = persons.stream().reduce(0, (sum, p) -> {
            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
            return sum += p.age;
        }, (sum1, sum2) -> {
            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
            return sum1 + sum2;
        });

        System.out.println(ageSum);
    }

    // 并行处理
    private static void test5(List<Person> persons) {
        Integer ageSum = persons.parallelStream().reduce(0, (sum, p) -> {
            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
            return sum += p.age;
        }, (sum1, sum2) -> {
            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
            return sum1 + sum2;
        });

        System.out.println(ageSum);
    }

    // 并行处理
    // 预处理时，并行
    // 结果合并时，并行
    private static void test6(List<Person> persons) {
        Integer ageSum = persons.parallelStream().reduce(0, (sum, p) -> {
            System.out.format("accumulator: sum=%s; person=%s; thread=%s\n", sum, p, Thread.currentThread().getName());
            return sum += p.age;
        }, (sum1, sum2) -> {
            System.out.format("combiner: sum1=%s; sum2=%s; thread=%s\n", sum1, sum2, Thread.currentThread().getName());
            return sum1 + sum2;
        });

        System.out.println(ageSum);
    }
}
