package com.winterbe.java8.samples.stream;

import com.winterbe.java8.samples.util.Student;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlyone
 */
public class Stream_collect {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Tom", "男", 18));
        studentList.add(new Student("Lily", "女", 30));
        studentList.add(new Student("John", "男", 29));
        studentList.add(new Student("Lucy", "女", 21));
        studentList.add(new Student("Jack", "男", 38));

        // test1();
        // test2(studentList);
        // test3(studentList);
        test4(studentList);
    }

    // 将字符串换成大写并用逗号链接起来
    private static void test1() {
        List<String> citys = Arrays.asList("USA", "Japan", "France");
        String cityS = citys.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(cityS);
    }

    // 按性别分组
    private static void test2(List<Student> studentList) {
        Map<String, List<Student>> maps = studentList.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println(maps);
    }

    // 找出年龄最大的人
    private static void test3(List<Student> studentList) {
        Optional<Student> optional1 = studentList.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
        optional1.ifPresent(System.out::println);

        // 年龄最小
        Optional<Student> optional2 = studentList.stream().collect(Collectors.minBy(Comparator.comparing(Student::getAge)));
        optional2.ifPresent(System.out::println);
    }

    // 年龄总和
    private static void test4(List<Student> studentList) {
        // reducing的参数，第一个：初始值。第二个：转换函数。第三个：累积函数
        int sum = studentList.stream().collect(Collectors.reducing(0, Student::getAge, Integer::sum));
        System.out.println(sum);
    }
}
