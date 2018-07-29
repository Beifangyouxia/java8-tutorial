package com.winterbe.java8.samples.lambda;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Common standard functions from the Java API.
 *
 * @author Benjamin Winterberg
 */
public class Lambda3 {

    @FunctionalInterface
    interface Fun {

        void foo();
    }

    static Person getPerson() {
        return new Person("TOm1111", "tom lastename");
    }

    public static void main(String[] args) throws Exception {

        // Predicates

        Predicate<String> predicate = (s) -> s.length() > 0;

        predicate.test("foo"); // true
        predicate.negate().test("foo"); // false

        Predicate<String> nonNull = Objects::nonNull;
        System.out.println("Objects::nonNull=== " + nonNull.test(null));

        Predicate<Boolean> isNull = Objects::isNull;
        System.out.println("Objects::isNull=== " + isNull.test(true));

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println("String::isEmpty ==== " + isEmpty.test(""));
        Predicate<String> isNotEmpty = isEmpty.negate();

        // Functions

        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123"); // "123"

        Function<String, String> f1 = (t) -> t + " is comming";
        Function<String, String> f2 = f1.compose(t -> "Hi, " + t);
        System.out.println("Function test=" + f2.apply("Tom"));

        // Suppliers

        Supplier<Person> personSupplier = () -> {
            return new Person("Tom", "last");
        };
        System.out.println("Suppliers " + personSupplier.get().firstName);// new Person

        Supplier<Person> personSupplier1 = Person::new;
        System.out.println("Suppliers " + personSupplier1.get().firstName);// new Person

        Supplier<Person> personSupplier2 = Lambda3::getPerson;
        System.out.println("Suppliers " + personSupplier2.get().firstName);// new Person

        // Consumers

        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));

        // Comparators

        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        comparator.compare(p1, p2); // > 0
        comparator.reversed().compare(p1, p2); // < 0

        // Runnables

        Runnable runnable = () -> System.out.println(UUID.randomUUID());
        runnable.run();

        // Callables

        Callable<UUID> callable = UUID::randomUUID;
        callable.call();
    }

}
