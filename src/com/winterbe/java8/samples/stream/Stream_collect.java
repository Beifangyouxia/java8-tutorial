package com.winterbe.java8.samples.stream;

import com.winterbe.java8.samples.util.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

        test1();
        test2(studentList);
    }

    // 将字符串换成大写并用逗号链接起来
    private static void test1() {
        List<String> citys = Arrays.asList("USA", "Japan", "France");
        String G7Countries = citys.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        System.out.println(G7Countries);
    }

    // 按性别分组
    private static void test2(List<Student> studentList) {
        Map<String, List<Student>> maps = studentList.stream().collect(Collectors.groupingBy(Student::getSex));
        System.out.println(maps);

    }
}
