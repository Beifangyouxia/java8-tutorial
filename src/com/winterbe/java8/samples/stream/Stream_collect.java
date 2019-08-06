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
//        test4(studentList);
        // test5(studentList);
        // test6(studentList);
//        test7(studentList);
        test8(studentList);
    }

    // 将字符串换成大写，并用逗号连接起来
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

    // 先按性别分组，然后再按年龄段分组
    private static void test3(List<Student> studentList) {
        Map<String, Map<String, List<Student>>> maps = studentList.stream().collect(Collectors.groupingBy(Student::getSex,
                Collectors.groupingBy(s -> {
                    if (s.getAge() < 20) {
                        return "低age";
                    } else {
                        return "高age";
                    }
                })));
        System.out.println(maps);
    }

    // 按年龄25分成两个组
    private static void test4(List<Student> studentList) {
        Map<Boolean, List<Student>> maps = studentList.stream().collect(Collectors.partitioningBy(s -> {
            if (s.getAge() < 25) {
                return true;
            } else {
                return false;
            }
        }));
        System.out.println(maps);
    }

    // 找出年龄最大的人
    private static void test5(List<Student> studentList) {
        Optional<Student> optional1 = studentList.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
        optional1.ifPresent(System.out::println);

        // 年龄最小
        Optional<Student> optional2 = studentList.stream().collect(Collectors.minBy(Comparator.comparing(Student::getAge)));
        optional2.ifPresent(System.out::println);
    }

    // 年龄总和
    private static void test6(List<Student> studentList) {
        // reducing的参数，第一个：初始值。第二个：转换函数。第三个：累积函数
        int sum = studentList.stream().collect(Collectors.reducing(0, Student::getAge, Integer::sum));
        System.out.println(sum);
    }

    // 转换成一个Map<sex,name>，注意：如果key重复会抛异常
    // https://cloud.tencent.com/developer/article/1351931
    private static void test7(List<Student> studentList) {
        Map<String, String> sexNameMap = studentList.stream().collect(Collectors.toMap(p -> {
            return p.getSex();
        }, p2 -> {
            return p2.getName();
        }));
        System.out.println(sexNameMap);
    }

    // 转换成一个Map<sex,name>，注意：如果key重复会抛异常
    // 如果key重复，用后一个的value值覆盖前一个
    private static void test8(List<Student> studentList) {
        Map<String, String> sexNameMap = studentList.stream().collect(Collectors.toMap(p -> {
            return p.getSex();
        }, p2 -> {
            return p2.getName();
        }, (oldValue, newValue) -> newValue));
        System.out.println(sexNameMap);
    }
}
