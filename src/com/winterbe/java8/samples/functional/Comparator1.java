package com.winterbe.java8.samples.functional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.winterbe.java8.samples.util.Score;

/**
 * @author onlyone
 */
public class Comparator1 {

    /**
     * 比较器
     */
    public static void main(String[] args) {

        List<Score> list = new ArrayList<>();
        list.add(new Score("xiaohong", 90L, 91L));
        list.add(new Score("xiaoming", 85L, 90L));
        list.add(new Score("wanggang", 90L, 96L));
        list.add(new Score("xiaoma", 85L, 70L));

        // 先按语文由小到大，如果相等，按数学由小到大
        Collections.sort(list, Comparator.comparing(Score::getYuwen).thenComparing(Score::getShuxue));
        System.out.println("先按语文由小到大，如果相等，按数学由小到大");
        list.forEach(System.out::println);

        // 先按语文由大到小，如果相等，按数学由大到小
        Comparator c1 = Comparator.comparing(Score::getYuwen).reversed();
        Comparator c2 = Comparator.comparing(Score::getShuxue).reversed();
        Collections.sort(list, c1.thenComparing(c2));
        System.out.println("先按语文由大到小，如果相等，按数学由大到小");
        list.forEach(System.out::println);

    }
}
