# Modern Java -  Java 8 学习手册
_原文出自 [my blog](http://winterbe.com/posts/2014/03/16/java-8-tutorial/)._

> [“Java并没有没落，人们依然为之着迷”](https://twitter.com/mreinhold/status/429603588525281280)

感谢你对 [Java 8](https://jdk8.java.net/)的关注。本文将指导你一步步了解该语言的新特性。 通过一些短而简单的代码示例，你可以快速上手 default interface methods, lambda expressions, method references and repeatable annotations。文章结尾，你可以了解常用的[API](http://download.java.net/jdk8/docs/api/) 有哪些调整， 比如 streams, functional interfaces, map extensions 以及 the new Date API。 **本文采用片段式讲解，而非长篇贴代码 。 请尽情享受!**

---

<p align="center">
 ★★★ 如果喜欢的话? 请点下收藏 , <a href="https://twitter.com/winterbe_"> 关注 Twitter</a> or <a href="https://www.paypal.me/winterbe">捐赠</a> 鼓励我继续写下去。 谢谢! ★★★
</p>

---

## Table of Contents

* [接口使用default方法](#接口使用default方法)
* [Lambda表达式](#lambda表达式)
* [Functional接口](#functional接口)
* [Method and Constructor 引用](#method-and-constructor-引用)
* [Lambda 范围](#lambda-范围)
  * [局部变量](#局部变量)
  * [访问全局变量或静态变量](#访问全局变量或静态变量)
  * [default方法](#default方法)
* [内置 Functional Interfaces](#内置-functional-interfaces)
  * [Predicates](#predicates)
  * [Functions](#functions)
  * [Suppliers](#suppliers)
  * [Consumers](#consumers)
  * [Comparators](#comparators)
* [可选择 Optionals](#可选择-optionals)
* [Streams](#streams)
  * [Filter](#filter)
  * [Sorted](#sorted)
  * [Map](#map)
  * [Match](#match)
  * [Count](#count)
  * [Reduce](#reduce)
* [Parallel Streams](#parallel-streams)
  * [Sequential Sort](#sequential-sort)
  * [Parallel Sort](#parallel-sort)
* [Maps](#maps)
* [Date API](#date-api)
  * [Clock](#clock)
  * [Timezones](#timezones)
  * [LocalTime](#localtime)
  * [LocalDate](#localdate)
  * [LocalDateTime](#localdatetime)
* [Annotations](#annotations)
* [Where to go from here?](#where-to-go-from-here)


## 接口使用default方法

Java 8 允许我们在接口类中使用非结构化方法比如`default`修饰词，来实现接口。
 
这一特性在 [virtual extension methods](http://stackoverflow.com/a/24102730)有详细介绍。

示例:

```
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
```
`Formula` 接口类中除了定义抽象方法 `calculate`，还定义default方法`sqrt`。实现类必须实现抽象方法`calculate`。default修饰的方法`sqrt`在类外同样可以使用。


```
Formula formula = new Formula() {
    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
};

formula.calculate(100);     // 100.0
formula.sqrt(16);           // 4.0
```

公式采用匿名实现类。代码非常冗余，需要6行代码来实现一个简单的计算 `sqrt(a * 100)`。下一节中，用Java 8 能更加巧妙的实现一个方法。



## Lambda表达式

一个简单例子，对字符串集合排序

```java
List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return b.compareTo(a);
    }
});
```

静态方法 `Collections.sort` 接收list集合和一个comparator比较器从而实现排序功能。通常你需要创建一个匿名比较器，并传递给排序方法。

为了替换匿名类实例，Java 8 引入一种非常简洁的语法， **lambda 表达式**:


```java
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

当然你也可以采用更短更易读的写法，如上。


```java
Collections.sort(names, (String a, String b) -> b.compareTo(a));
```

可以进一步精简，只剩一行代码，省略`{}`和`return`方法，如上。


```java
names.sort((a, b) -> b.compareTo(a));
```
List类现在提供`sort`方法。同时java编译器能自动识别参数类型，所以编码时你可以忽略它们。接下来让我们深入学习lambda表达式如何广泛使用。



## Functional接口

lambda表达式是如何识别Java的系统类型？每个lambda对应一个由接口指定的类型。因此每一个_functional 接口_ 的定义必须包含一个抽象方法声明。每个lambda表达式的类型需要与抽象方法匹配。由于默认方法不是抽象的，你需要将默认方法添加到函数接口。

我们可以任意定义一个接口作为lambda表达式，其内部需要包含一个抽象方法。 为了确保接口满足规范，你需要添加 `@FunctionalInterface` 注解。一旦你试图添加第二个抽象方法，编译器会自动检测并抛出一个编译错误。


示例:

```java
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}
```

```java
Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);    // 123
```
代码：com.winterbe.java8.samples.lambda.Lambda2

记住，如果省略`@FunctionalInterface`，代码也是有效的。


## Method and Constructor 引用

上面的代码示例可以采用静态方法引用进一步简化：

```java
Converter<String, Integer> converter = Integer::valueOf;
Integer converted = converter.convert("123");
System.out.println(converted);   // 123
```

Java 8允许你通过方法或构造器的引用，如 `::` 。上面示例演示了引用一个静态方法。另外我们也可以用类实例对象的方法。


```java
class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
```

```java
Something something = new Something();
Converter<String, String> converter = something::startsWith;
String converted = converter.convert("Java");
System.out.println(converted);    // "J"
```

让我们了解下 `::` 字键字如何用在构造器中。首先定义一个类如下结构：


```java
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

接下来，我们定义一个person工厂接口，用于创建新的persons：

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

与传统的实现方式不同，我们通过调用构造器方法来实现：

```java
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
```

我们通过 `Person::new`来触发Person的构造器函数。Java编译器能自动选择合适的构造器函数来匹配`PersonFactory.create`。

代码：com.winterbe.java8.samples.lambda.Lambda2

## Lambda 范围

lambda表达式访问外部变量同匿名对象相似。你可以访问final修饰的本地局部变量。

### 局部变量

我们可以读取final修饰的本地变量

```java
final int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```
与匿名对象不同，变量`num`不必强制一定用final修饰。下面写法也是有效的：

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

`num`在代码编译时必须是隐式的final类型。下面的写法编译会报错：

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

### 访问全局变量或静态变量

与局部变量相反，我们可以在lambda表达式中读或写全局变量或静态全局变量。

```java
class Lambda4 {
    static int outerStaticNum;
    int outerNum;
	
	// 方法
    void testScopes() {
        Converter<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            return String.valueOf(from);
        };

        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }
}
```

代码：com.winterbe.java8.samples.lambda.Lambda4

### default方法

还记得第一节的示例公式吗？`Formula`接口类中定义一个默认方法 `sqrt` ，它可以被每一个formula实例对象包括匿名对象访问。但这并适用于lambda表达式。

lambda表达式语句不能直接访问default方法。下面写法编译不过：

```java
Formula formula = (a) -> sqrt(a * 100);
```


## 内置 Functional Interfaces


JDK 1.8 API提供了许多内置的功能接口。其中一些来自Java旧版本，如`Comparator`或`Runnable`。通过`@FunctionalInterface`注解扩展那些已存在的接口以实现lambda支持。

同时Java 8 API还提供一些新的functional interfaces满足多场景需求。一些新特性出自[Google Guava](https://code.google.com/p/guava-libraries/) 三方库。

### Predicates

Predicates内部定义了一个boolean类型判断方法，带有一个入参。这个接口还包含很多default方法满足各种复杂的逻辑表达式，如（与、或、非）

```java
Predicate<String> predicate = (s) -> s.length() > 0;

predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();
```

代码：com.winterbe.java8.samples.lambda.Lambda3

### Functions

函数接收一个入参并返回一个结果。`default`方法被用于将多个功能函数链接在一起，（compose 之前执行、andThen 之后执行）


```java
Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);

backToString.apply("123");     // "123"
```
代码：com.winterbe.java8.samples.lambda.Lambda3

### Suppliers

Suppliers 生产指定类型的结果。不同于Functions，Suppliers 不接受任何参数。

```java
Supplier<Person> personSupplier = Person::new;
personSupplier.get();   // new Person
```

代码：com.winterbe.java8.samples.lambda.Lambda3

### Consumers

Consumers 表示对单个输入参数加工处理，并提供 andThen 'default'方法进行后续处理。

```java
Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
greeter.accept(new Person("Luke", "Skywalker"));
```
代码：com.winterbe.java8.samples.lambda.Lambda3

### Comparators

Comparators 在Java老版本就已经存在。Java 8增加了各种各样的`default`方法

```java
Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");

comparator.compare(p1, p2);             // > 0
comparator.reversed().compare(p1, p2);  // < 0
```
代码：com.winterbe.java8.samples.lambda.Lambda3

## 可选择 Optionals

Optionals 不是一个函数式接口，主要是为了防止`NullPointerException`。下一节会重点介绍，现在让我们先了解下Optionals是如何工作的。

Optional是一个用于存放 null 或非null值的简易容器。试想，一个带返回值的方法有时会返回空。相反，Java 8 返回的是`Optional` ，而非 `null`。

```java
Optional<String> optional = Optional.of("bam");

optional.isPresent();           // true
optional.get();                 // "bam"
optional.orElse("fallback");    // "bam"

optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

代码：com.winterbe.java8.samples.stream.Optional1

## Streams

`java.util.Stream` 可以对元素列表进行一次或多次操作。Stream操作可以是中间值也可以是最终结果。
最后的操作返回的是某种类型结果，而中间操作返回的是stream本身。因此你可以在一行代码链接多个方法调用。Streams被创建于`java.util.Collection` like lists or sets (maps 并不支持)。Stream可以顺序执行，也可以并行执行。


> Streams 非常强大， 因此我单独写了一篇文章介绍 [Java 8 Streams Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)。 **你可以直接拉代码  [Sequency](https://github.com/winterbe/sequency) **

让我们看看顺序streams是如何运作的。首先我们创建String类型的List集合。


```java
List<String> stringCollection = new ArrayList<>();
stringCollection.add("ddd2");
stringCollection.add("aaa2");
stringCollection.add("bbb1");
stringCollection.add("aaa1");
stringCollection.add("bbb3");
stringCollection.add("ccc");
stringCollection.add("bbb2");
stringCollection.add("ddd1");
```

在Java 8中，对Collections进行了扩展。你可以通过 `Collection.stream()` 或`Collection.parallelStream()`来创建一个streams。下文将介绍一些常见的流操作。

代码：com.winterbe.java8.samples.stream.Streams1

### Filter

Filter通过predicate判断函数来过滤所有的元素。这个操作是中间态的，直到我们触发另一个流操作`forEach`得到结果。ForEach 对每一个过滤后的元素执行consumer函数。ForEach是一个终止操作。返回类型是`void`，因为后面不能继续接受stream操作。


```java
stringCollection
    .stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa2", "aaa1"
```

### Sorted

Sorted是一个中间态操作，它返回流的有序视图。 除非你传递自定义的`Comparator`，否则元素按自然顺序排序。

```java
stringCollection
    .stream()
    .sorted()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa1", "aaa2"
```

记住 ，`sorted`只是创建流的排序视图，并没有改变原始集合的顺序。所以说`stringCollection`的顺序并没有改变。

```java
System.out.println(stringCollection);
// ddd2, aaa2, bbb1, aaa1, bbb3, ccc, bbb2, ddd1
```
代码：com.winterbe.java8.samples.stream.Streams2

### Map

The _intermediate_ operation `map` converts each element into another object via the given function. The following example converts each string into an upper-cased string. But you can also use `map` to transform each object into another type. The generic type of the resulting stream depends on the generic type of the function you pass to `map`.

```java
stringCollection
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))
    .forEach(System.out::println);

// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
```

### Match

Various matching operations can be used to check whether a certain predicate matches the stream. All of those operations are _terminal_ and return a boolean result.

```java
boolean anyStartsWithA =
    stringCollection
        .stream()
        .anyMatch((s) -> s.startsWith("a"));

System.out.println(anyStartsWithA);      // true

boolean allStartsWithA =
    stringCollection
        .stream()
        .allMatch((s) -> s.startsWith("a"));

System.out.println(allStartsWithA);      // false

boolean noneStartsWithZ =
    stringCollection
        .stream()
        .noneMatch((s) -> s.startsWith("z"));

System.out.println(noneStartsWithZ);      // true
```

#### Count

Count is a _terminal_ operation returning the number of elements in the stream as a `long`.

```java
long startsWithB =
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("b"))
        .count();

System.out.println(startsWithB);    // 3
```


### Reduce

This _terminal_ operation performs a reduction on the elements of the stream with the given function. The result is an `Optional` holding the reduced value.

```java
Optional<String> reduced =
    stringCollection
        .stream()
        .sorted()
        .reduce((s1, s2) -> s1 + "#" + s2);

reduced.ifPresent(System.out::println);
// "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
```

## Parallel Streams

As mentioned above streams can be either sequential or parallel. Operations on sequential streams are performed on a single thread while operations on parallel streams are performed concurrently on multiple threads.

The following example demonstrates how easy it is to increase the performance by using parallel streams.

First we create a large list of unique elements:

```java
int max = 1000000;
List<String> values = new ArrayList<>(max);
for (int i = 0; i < max; i++) {
    UUID uuid = UUID.randomUUID();
    values.add(uuid.toString());
}
```

Now we measure the time it takes to sort a stream of this collection.

### Sequential Sort

```java
long t0 = System.nanoTime();

long count = values.stream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));

// sequential sort took: 899 ms
```

### Parallel Sort

```java
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));

// parallel sort took: 472 ms
```

As you can see both code snippets are almost identical but the parallel sort is roughly 50% faster. All you have to do is change `stream()` to `parallelStream()`.

## Maps

As already mentioned maps do not directly support streams. There's no `stream()` method available on the `Map` interface itself, however you can create specialized streams upon the keys, values or entries of a map via `map.keySet().stream()`, `map.values().stream()` and `map.entrySet().stream()`. 

Furthermore maps support various new and useful methods for doing common tasks.

```java
Map<Integer, String> map = new HashMap<>();

for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((id, val) -> System.out.println(val));
```

The above code should be self-explaining: `putIfAbsent` prevents us from writing additional if null checks; `forEach` accepts a consumer to perform operations for each value of the map.

This example shows how to compute code on the map by utilizing functions:

```java
map.computeIfPresent(3, (num, val) -> val + num);
map.get(3);             // val33

map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);     // false

map.computeIfAbsent(23, num -> "val" + num);
map.containsKey(23);    // true

map.computeIfAbsent(3, num -> "bam");
map.get(3);             // val33
```

Next, we learn how to remove entries for a given key, only if it's currently mapped to a given value:

```java
map.remove(3, "val3");
map.get(3);             // val33

map.remove(3, "val33");
map.get(3);             // null
```

Another helpful method:

```java
map.getOrDefault(42, "not found");  // not found
```

Merging entries of a map is quite easy:

```java
map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9

map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9concat
```

Merge either put the key/value into the map if no entry for the key exists, or the merging function will be called to change the existing value.


## Date API

Java 8 contains a brand new date and time API under the package `java.time`. The new Date API is comparable with the [Joda-Time](http://www.joda.org/joda-time/) library, however it's [not the same](http://blog.joda.org/2009/11/why-jsr-310-isn-joda-time_4941.html). The following examples cover the most important parts of this new API.

### Clock

Clock provides access to the current date and time. Clocks are aware of a timezone and may be used instead of `System.currentTimeMillis()` to retrieve the current time in milliseconds since Unix EPOCH. Such an instantaneous point on the time-line is also represented by the class `Instant`. Instants can be used to create legacy `java.util.Date` objects.

```java
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();

Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```

### Timezones

Timezones are represented by a `ZoneId`. They can easily be accessed via static factory methods. Timezones define the offsets which are important to convert between instants and local dates and times.

```java
System.out.println(ZoneId.getAvailableZoneIds());
// prints all available timezone ids

ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
System.out.println(zone1.getRules());
System.out.println(zone2.getRules());

// ZoneRules[currentStandardOffset=+01:00]
// ZoneRules[currentStandardOffset=-03:00]
```

### LocalTime

LocalTime represents a time without a timezone, e.g. 10pm or 17:30:15. The following example creates two local times for the timezones defined above. Then we compare both times and calculate the difference in hours and minutes between both times.

```java
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);

System.out.println(now1.isBefore(now2));  // false

long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);       // -3
System.out.println(minutesBetween);     // -239
```

LocalTime comes with various factory methods to simplify the creation of new instances, including parsing of time strings.

```java
LocalTime late = LocalTime.of(23, 59, 59);
System.out.println(late);       // 23:59:59

DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.GERMAN);

LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
System.out.println(leetTime);   // 13:37
```

### LocalDate

LocalDate represents a distinct date, e.g. 2014-03-11. It's immutable and works exactly analog to LocalTime. The sample demonstrates how to calculate new dates by adding or subtracting days, months or years. Keep in mind that each manipulation returns a new instance.

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
LocalDate yesterday = tomorrow.minusDays(2);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);    // FRIDAY
```

Parsing a LocalDate from a string is just as simple as parsing a LocalTime:

```java
DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
System.out.println(xmas);   // 2014-12-24
```

### LocalDateTime

LocalDateTime represents a date-time. It combines date and time as seen in the above sections into one instance. `LocalDateTime` is immutable and works similar to LocalTime and LocalDate. We can utilize methods for retrieving certain fields from a date-time:

```java
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);      // WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);          // DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);    // 1439
```

With the additional information of a timezone it can be converted to an instant. Instants can easily be converted to legacy dates of type `java.util.Date`.

```java
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();

Date legacyDate = Date.from(instant);
System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
```

Formatting date-times works just like formatting dates or times. Instead of using pre-defined formats we can create formatters from custom patterns.

```java
DateTimeFormatter formatter =
    DateTimeFormatter
        .ofPattern("MMM dd, yyyy - HH:mm");

LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
String string = formatter.format(parsed);
System.out.println(string);     // Nov 03, 2014 - 07:13
```

Unlike `java.text.NumberFormat` the new `DateTimeFormatter` is immutable and **thread-safe**.

For details on the pattern syntax read [here](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).


## Annotations

Annotations in Java 8 are repeatable. Let's dive directly into an example to figure that out.

First, we define a wrapper annotation which holds an array of the actual annotations:

```java
@interface Hints {
    Hint[] value();
}

@Repeatable(Hints.class)
@interface Hint {
    String value();
}
```
Java 8 enables us to use multiple annotations of the same type by declaring the annotation `@Repeatable`.

### Variant 1: Using the container annotation (old school)

```java
@Hints({@Hint("hint1"), @Hint("hint2")})
class Person {}
```

### Variant 2: Using repeatable annotations (new school)

```java
@Hint("hint1")
@Hint("hint2")
class Person {}
```

Using variant 2 the java compiler implicitly sets up the `@Hints` annotation under the hood. That's important for reading annotation information via reflection.

```java
Hint hint = Person.class.getAnnotation(Hint.class);
System.out.println(hint);                   // null

Hints hints1 = Person.class.getAnnotation(Hints.class);
System.out.println(hints1.value().length);  // 2

Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
System.out.println(hints2.length);          // 2
```

Although we never declared the `@Hints` annotation on the `Person` class, it's still readable via `getAnnotation(Hints.class)`. However, the more convenient method is `getAnnotationsByType` which grants direct access to all annotated `@Hint` annotations.


Furthermore the usage of annotations in Java 8 is expanded to two new targets:

```java
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface MyAnnotation {}
```

## Where to go from here?

My programming guide to Java 8 ends here. If you want to learn more about all the new classes and features of the JDK 8 API, check out my [JDK8 API Explorer](http://winterbe.com/projects/java8-explorer/). It helps you figuring out all the new classes and hidden gems of JDK 8, like `Arrays.parallelSort`, `StampedLock` and `CompletableFuture` - just to name a few.

I've also published a bunch of follow-up articles on my [blog](http://winterbe.com) that might be interesting to you:

- [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [Java 8 Nashorn Tutorial](http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)
- [Java 8 Concurrency Tutorial: Threads and Executors](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)
- [Java 8 Concurrency Tutorial: Synchronization and Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)
- [Java 8 Concurrency Tutorial: Atomic Variables and ConcurrentMap](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)
- [Java 8 API by Example: Strings, Numbers, Math and Files](http://winterbe.com/posts/2015/03/25/java8-examples-string-number-math-files/)
- [Avoid Null Checks in Java 8](http://winterbe.com/posts/2015/03/15/avoid-null-checks-in-java/)
- [Fixing Java 8 Stream Gotchas with IntelliJ IDEA](http://winterbe.com/posts/2015/03/05/fixing-java-8-stream-gotchas-with-intellij-idea/)
- [Using Backbone.js with Java 8 Nashorn](http://winterbe.com/posts/2014/04/07/using-backbonejs-with-nashorn/)

You should [follow me on Twitter](https://twitter.com/winterbe_). Thanks for reading!
