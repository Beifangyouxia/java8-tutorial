package com.winterbe.java8.samples.functional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author onlyone
 */
public class Predicate2 {

    public static void main(String[] args) {
        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");

        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> str.startsWith("J"));

        System.out.println("Languages which ends with a ");
        filter(languages, (str) -> str.endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

        System.out.println("Print no language : ");
        filter(languages, (str) -> false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str) -> str.length() > 4);

        // 多个判断条件组合
        System.out.print("多个判断条件组合");
        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
        languages.stream().filter(startsWithJ.and(fourLetterLong)).forEach((n) -> System.out.print(", which starts with 'J' and four letter long is : "
                                                                                                   + n));
    }

    /**
     * 对同一数据源采用不同的判断条件处理
     */
    public static void filter(List<String> names, Predicate<String> condition) {
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }
}
