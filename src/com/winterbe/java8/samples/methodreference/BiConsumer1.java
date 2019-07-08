package com.winterbe.java8.samples.methodreference;

import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

import com.winterbe.java8.samples.util.Utils;

/**
 * <pre>
 * 方法引用分三种情况：1）类+动态方法 2）类+静态方法 3）类实例对象+动态方法 4）类实例对象+静态方法(无效，不正确写法)
 * 
 * <pre/>
 * 
 * @author onlyone
 */
public class BiConsumer1 {

    public static void main(String[] args) {

        // 1）类+动态方法
        // JDK默认会把当前实例传入到非静态方法，参数名为this，参数位置为第一个，所以我们在非静态方法中才能访问this，那么就可以通过BiConsumer传入实例对象进行实例方法的引用
        BiConsumer<LinkedHashSet, Object> biConsumer1 = LinkedHashSet::add;
        LinkedHashSet s1 = new LinkedHashSet();
        biConsumer1.accept(s1, "aaa");
        System.out.println(s1);

        // 2）类+静态方法
        BiConsumer<String, Long> biConsumer2 = Utils::concatStatic;
        biConsumer2.accept("first_param", 6L);

        // 3）类实例对象+动态方法
        BiConsumer<String, Long> biConsumer3 = new Utils()::concat;
        biConsumer3.accept("first_param", 7L);

        // 4）类实例对象+静态方法
        // Error:(35, 48) java: 方法引用无效 ,静态限制范围方法引用
        // BiConsumer<String, Long> biConsumer4 = new Utils()::concatStatic;
        // biConsumer4.accept("first_param", 8L);

    }
}
