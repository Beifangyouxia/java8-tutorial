#  Java 8 学习手册

> [“Java并没有没落，人们依然为之着迷”](https://twitter.com/mreinhold/status/429603588525281280)

感谢你对 [Java 8](https://jdk8.java.net/)的关注。本文将指导你一步步了解该语言的新特性。 通过一些短而简单的代码示例，你可以快速上手 default interface methods, lambda expressions, method references and repeatable annotations。文章结尾，你可以了解常用的[API](http://download.java.net/jdk8/docs/api/) 有哪些调整， 比如 streams, functional interfaces, map extensions 以及 the new Date API。 **本文采用片段式讲解，而非长篇贴代码 。 请尽情享受!**


---

## 目录

* [接口使用default方法](#接口使用default方法)
* [Lambda表达式](#lambda表达式)
* [Functional接口](#functional接口)
* [Method and Constructor 引用](#method-and-constructor-引用)
* [Lambda 范围](#lambda-范围)
  * [局部变量](#局部变量)
  * [访问全局变量或静态变量](#访问全局变量或静态变量)
  * [default方法](#default方法)
* [方法引用](#方法引用)
* [内置 Functional Interfaces](#内置-functional-interfaces)
  * [判断 Predicates](#判断-predicates)
  * [函数 Functions](#函数-functions)
  * [生产 Suppliers](#生产-suppliers)
  * [消费 Consumers](#消费-consumers)
  * [比较 Comparators](#比较-comparators)
  * [其它的函数接口](#其它的函数接口)
* [可选择 Optionals](#可选择-optionals)
* [流 Streams](#流-streams)
  * [过滤 Filter](#过滤-filter)
  * [映射 Map](#映射-map)
  * [映射 flatMap](#映射-flatMap)
  * [排序 Sorted](#排序-sorted)
  * [归约 Reduce](#归约-reduce)
  * [计数 Count](#计数-count)
  * [匹配 Match](#匹配-match)
  * [跳过 skip](#跳过-skip)
  * [输出 limit](#输出-limit)
  * [输出 collect](#输出-collect)
* [并行 Streams](#并行-streams)
  * [串行 Sort](#串行-sort)
  * [并行 Sort](#并行-sort)
* [集合 Maps](#集合-maps)
* [日期 API](#日期-api)
  * [时钟](#时钟)
  * [时区](#时区)
  * [本地时间](#本地时间)
  * [本地日期](#本地日期)
  * [本地日期时间](#本地日期时间)
* [注解](#注解)
* [后续计划](#后续计划)


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

```
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


```
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

当然你也可以采用更短更易读的写法，如上。


```
Collections.sort(names, (String a, String b) -> b.compareTo(a));
```

可以进一步精简，只剩一行代码，省略`{}`和`return`方法，如上。


```
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

```
final int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```
与匿名对象不同，变量`num`不必强制一定用final修饰。下面写法也是有效的：

```
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

`num`在代码编译时必须是隐式的final类型。下面的写法编译会报错：

```
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

### 访问全局变量或静态变量

与局部变量相反，我们可以在lambda表达式中读或写全局变量或静态全局变量。

```
代码：com.winterbe.java8.samples.lambda.Lambda4
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

### default方法

还记得第一节的示例公式吗？`Formula`接口类中定义一个默认方法 `sqrt` ，它可以被每一个formula实例对象包括匿名对象访问。但这并适用于lambda表达式。

lambda表达式语句不能直接访问default方法。下面写法编译不过：

```
Formula formula = (a) -> sqrt(a * 100);
```

## 方法引用

方法引用和 lambda 表达式拥有相同的特性（例如，它们都需要一个目标类型，并需要被转化为函数式接口的实例），不过我们并不需要为方法引用提供方法体，我们可以直接通过方法名称引用已有方法。

方法引用分三种情况：

* 1）类+动态方法 
* 2）类+静态方法 
* 3）类实例对象+动态方法 
* 4）类实例对象+静态方法(无效，不正确写法)

```
    public static void main(String[] args) {

        // 1）类+动态方法
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
```

接收 int 参数的数组构造方法

```
IntFunction<int[]> arrayMaker = int[]::new;
int[] array = arrayMaker.apply(10) // 创建数组 int[10]

```

## 内置 Functional Interfaces


JDK 1.8 API提供了许多内置的功能接口。其中一些来自Java旧版本，如`Comparator`或`Runnable`。通过`@FunctionalInterface`注解扩展那些已存在的接口以实现lambda支持。

同时Java 8 API还提供一些新的functional interfaces满足多场景需求。一些新特性出自[Google Guava](https://code.google.com/p/guava-libraries/) 三方库。

### 判断 Predicates

Predicates内部定义了一个boolean类型判断方法，带有一个入参。这个接口还包含很多default方法满足各种复杂的逻辑表达式，如（与、或、非）

```
代码：com.winterbe.java8.samples.lambda.Lambda3

Predicate<String> predicate = (s) -> s.length() > 0;

predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();
```

### 函数 Functions

函数接收一个入参并返回一个结果。`default`方法被用于将多个功能函数链接在一起，（compose 之前执行、andThen 之后执行）

```
代码：com.winterbe.java8.samples.lambda.Lambda3

Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);

backToString.apply("123");     // "123"
```

### 生产 Suppliers

Suppliers 生产指定类型的结果。不同于Functions，Suppliers 不接受任何参数。

```
代码：com.winterbe.java8.samples.lambda.Lambda3

Supplier<Person> personSupplier = Person::new;
personSupplier.get();   // new Person
```

案例：java8引入了一个对log方法的重载版本，这个版本的log方法接受一个Supplier作为参数。这个替代版本的log方法的函数签名如下：

```
public void log(Level level, Supplier<String> msgSupplier)
你可以通过下面的方式对它进行调用：

logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic());
如果日志器的级别设置恰当， log 方法会在内部才执行作为参数传递进来的Lambda表达式（注意：常规写法，先执行所有的入参方法，得到实参，再执行方法）。惰性求值，可以有效避免一些不必要的性能开销。

这里介绍的 Log 方法的内部实现如下：
public void log(Level level, Supplier<String> msgSupplier){
    if(logger.isLoggable(level)){
        log(level, msgSupplier.get());
    }
}
```

https://my.oschina.net/bairrfhoinn/blog/142985

### 消费 Consumers

Consumers 表示对单个输入参数加工处理，并提供 andThen 'default'方法进行后续处理。

```
代码：com.winterbe.java8.samples.lambda.Lambda3

Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
greeter.accept(new Person("Luke", "Skywalker"));
```

### 比较 Comparators

Comparators 在Java老版本就已经存在。Java 8增加了各种各样的`default`方法


```
代码：com.winterbe.java8.samples.lambda.Lambda3

Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");

comparator.compare(p1, p2);             // > 0
comparator.reversed().compare(p1, p2);  // < 0
```

Comparator与reversed组合使用，支持多字段的排序，默认由小到大，或由大到小

```
代码：com.winterbe.java8.samples.functional.Comparator1

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
```

### 其它的函数接口

象BinaryOperator、等

代码位置：com.winterbe.java8.samples.functional

## 可选择 Optionals

Optionals 不是一个函数式接口，主要是为了防止`NullPointerException`。下一节会重点介绍，现在让我们先了解下Optionals是如何工作的。

Optional是一个用于存放 null 或非null值的简易容器。试想，一个带返回值的方法有时会返回空。相反，Java 8 返回的是`Optional` ，而非 `null`。

```
代码：com.winterbe.java8.samples.stream.Optional1

Optional<String> optional = Optional.of("bam");

optional.isPresent();           // true
optional.get();                 // "bam"
optional.orElse("fallback");    // "bam"

optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

## 流 Streams

`java.util.Stream` 可以对元素列表进行一次或多次操作。Stream操作可以是中间值也可以是最终结果。
最后的操作返回的是某种类型结果，而中间操作返回的是stream本身。因此你可以在一行代码链接多个方法调用。Streams被创建于`java.util.Collection` ，比如 list or set (map 并不支持)。Stream可以顺序执行，也可以并行执行。

 Streams 非常强大， 因此我单独写了一篇文章介绍 [Java 8 Streams Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)。代码库  [Sequency](https://github.com/winterbe/sequency) 
 
* 中间操作：filter、map、mapToInt、mapToLong、mapToDouble、flatMap、sorted、distinct、limit、skip、of、iterate
* 终止操作：forEach、count、collect、reduce、toArray、anyMatch、allMatch、noneMatch、findAny、findFirst、max、min
* 原始类型特化流：IntStream、LongStream、DoubleStream

### 过滤 Filter

Filter通过`predicate`判断函数来过滤所有的元素。这个操作是中间操作，需要通过终止操作才会触发执行。

```
代码：com.winterbe.java8.samples.stream.Stream_filter
stringCollection
    .stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa2", "aaa1"
```

### 映射 Map

`map`是一种中间过程操作，借助函数表达式将元素转换成另一种形式。下面的例子将每个字符串转换成大写的字符串。但你也可以使用`map`将每个对象转换为另一种类型。最终输出的结果类型依赖于你传入的函数表达式。

```
stringCollection
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))  //由大到小
    .forEach(System.out::println);

// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
```
### 映射 flatMap

如果涉及到一对多映射，需要将映射结果放入Stream中。使用flatMap方法的效果是，转换后的多个结果并不是分别映射成一个流，而是映射成流的内容。

```
代码：com.winterbe.java8.samples.stream.Stream_flatMap

List<String> lists = Arrays.asList("Hello", "World");
lists.stream().flatMap(word-> Stream.of(word.split("")))
        .distinct()
        .forEach(System.out::println);
```

### 排序 Sorted

Sorted是一个中间态操作，它返回流的有序视图。 除非你传递自定义的`Comparator`，否则元素按默认的`由小到大`排序。

```
代码：com.winterbe.java8.samples.stream.Stream_sorted

//默认排序
stringCollection.stream().sorted().forEach(System.out::println);
System.out.println(stringCollection);
特别注意：`sorted`只是创建流的排序视图，并没有改变原始集合的顺序。所以说`stringCollection`的顺序并没有改变。
```

```
//自定义排序，按字符串，由大到小
stringCollection
        .stream()
        .map(String::toUpperCase)
        .sorted((a, b) -> b.compareTo(a))
        .forEach(System.out::println);
```

### 归约 Reduce

终止型操作，通过给定的函数表达式来处理流中的前后两个元素，或者中间结果与下一个元素。Lambda 反复结合每一个元素，直到流被归约成一个值。例如求和或查找最大元素。

```
代码：com.winterbe.java8.samples.stream.Stream_reduce

// 将流数据列表拆分多批，sum初始为0，每批都执行 (sum, p) -> sum = sum + p.age，得到局部的sum总和。并行计算思想
// 最后通过 (sum1, sum2) -> sum1 + sum2 ，计算最终的总和
// (sum1, sum2) -> sum1 + sum2，主要适用于并行，parallelStream（），单线程是无效的。

private static void test3(List<Person> persons) {
    Integer ageSum = persons.parallelStream().reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);
    System.out.println(ageSum);
}
```

更多reduce用法可参考：https://blog.csdn.net/io_field/article/details/54971679

#### 计数 Count

Count是一个终止型操作，返回一个long类型的元素列表总数。

```
代码：com.winterbe.java8.samples.stream.Stream_count
long startsWithB =
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("b"))
        .count();
        
System.out.println(startsWithB);    // 3
```

### 匹配 Match

各种匹配操作用于判断是否满足stream条件。所有的操作都完成后，返回一个boolean类型结果。

```
代码：com.winterbe.java8.samples.stream.Stream_match
List<String> stringCollection = new ArrayList<>();
stringCollection.add("ddd2");
stringCollection.add("aaa2");
stringCollection.add("bbb1");
stringCollection.add("aaa1");
stringCollection.add("bbb3");
stringCollection.add("ccc");
stringCollection.add("bbb2");
stringCollection.add("ddd1");

// 只需要一个条件满足
boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
System.out.println("anyMatch：" + anyStartsWithA); // true

// 所有条件都要满足
boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
System.out.println("allMatch：" + allStartsWithA); // false

// 所有的条件都要不满足
boolean noneStartsWithZ = stringCollection.stream().noneMatch((s) -> s.startsWith("z"));
System.out.println("noneMatch：" + noneStartsWithZ); // true

// 返回任意一个元素
Optional<String> anyE = stringCollection.stream().findAny();
System.out.println("findAny：" + anyE.get());

//返回第一个元素
Optional<String> firstE = stringCollection.stream().findFirst();
System.out.println("findFirst：" + firstE.get());
```

### 跳过 skip

返回一个扔掉前n个元素的流

```
代码：com.winterbe.java8.samples.stream.Stream_skip
// 扔掉前三个元素
stringCollection
    .stream()
    .skip(3)
    .forEach(System.out::println);

```

### 输出 limit

只取前N个结果

```
代码：com.winterbe.java8.samples.stream.Stream_limit
// 取前三个元素
stringCollection
    .stream()
    .limit(3)
    .forEach(System.out::println);

```

### 输出 collect

接受各种做法作为参数，将流中的元素累积成一个汇总结果

常见例子：

* 对一个交易列表按货币分组，获得该货币的所有交易额总和（返回一个Map\<Currency，Integer>）
* 将交易列表分成两组，贵的和不贵的（返回一个Map<Boolean，List\<Transaction>>）
* 创建多级分组，比如按城市对交易分组，然后进一步按照贵的或不贵分组

Collectors常见方法：

* Collectors.toList，得到List列表
* Collectors.joining ，通过`连接符`拼接字符串
* Collectors.groupingBy(Function<? super T,? extends K>) ，按K值分组，返回Map<K，List>
* Collectors.groupingBy(Function<? super T,? extends K>, Collector<? super T,A,D>)，二级分组，得到两级Map
* Collectors.maxBy，求最大值，需要传一个自定义的Comparator
* Collectors.reducing，广义的归约汇总。


```
代码：com.winterbe.java8.samples.stream.Stream_collect

// 将字符串换成大写，并用逗号链接起来
List<String> citys = Arrays.asList("USA", "Japan", "France");
String cityS = citys.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
        
// 按性别分组
Map<String, List<Student>> maps = studentList.stream().collect(Collectors.groupingBy(Student::getSex));

// 先按性别分组，然后再按年龄段分组
Map<String, Map<String, List<Student>>> maps = studentList.stream()
   .collect(Collectors.groupingBy(Student::getSex,
      Collectors.groupingBy(s -> {
          if (s.getAge() < 20) {
              return "低age";
          } else {
              return "高age";
          }
      })));

// 找出年龄最大的人
Optional<Student> optional1 = studentList.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getAge)));
optional1.ifPresent(System.out::println);

// 年龄总和
// reducing的参数，第一个：初始值。第二个：转换函数。第三个：累积函数
int sum = studentList.stream().collect(Collectors.reducing(0, Student::getAge, Integer::sum));
```


## 并行 Streams

如下所述，流可以是串行执行，也可以并行执行。对于流的串行执行是单个线程完成。而并行流处理则是在多个线程上同时执行。

下面这个例子将会演示如何通过并行流处理来显著提升性能。

首先我们创建一个大容量的List元素集合：

```
代码：com.winterbe.java8.samples.stream.Stream_reduce

int max = 1000000;
List<String> values = new ArrayList<>(max);
for (int i = 0; i < max; i++) {
    UUID uuid = UUID.randomUUID();
    values.add(uuid.toString());
}
```

现在我们测量对此集合的流排序所花费的时间。  

### 串行 Sort

```java
long t0 = System.nanoTime();

long count = values.stream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));

// sequential sort took: 899 ms
```

代码：com.winterbe.java8.samples.stream.Streams3

### 并行 Sort

```java
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));

// parallel sort took: 472 ms
```

代码：com.winterbe.java8.samples.stream.Streams3

正如你所看到的那样，两块代码片段几乎是相同的。但并行排序大约能快50%。所以你要做的是将`stream()`换成`parallelStream()`。

## 集合 Maps

如前所述，Maps不直接支持streams。Map接口并不提供 `stream()`相关方法。然而你能借助于`map.keySet().stream()`, `map.values().stream()` 和 `map.entrySet().stream()`创建基于keys，values或entries的流。

此外maps还提供一些新的有用的方法来支持常规任务。

代码：com.winterbe.java8.samples.misc.Maps1

```java
Map<Integer, String> map = new HashMap<>();

for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((id, val) -> System.out.println(val));
```

 `putIfAbsent`如果为空，执行put，否则返回key对应的value值，这样可以避免一次空判断代码冗余。`forEach`内部通过BiConsumer来操作。

下面的例子讲述了，map如何利用functions函数来计算：

```java
map.computeIfPresent(3, (num, val) -> val + num);
map.get(3);             // val33

map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);     // false

map.computeIfAbsent(23, num -> "val" + num);
map.containsKey(23);    // true

map.computeIfAbsent(3, num -> "bam");
map.get(3);             // val33（缺失才执行）
```

下面，我们学习如何通过给定key来删除entries，前提它当前存在kv映射：

```java

// 3对应的value等于“val3”，才执行删除动作
map.remove(3, "val3");
map.get(3);             // val33

map.remove(3, "val33");
map.get(3);             // null
```

其它有用的方法：

```java
// 返回key关联的值，否则返回后面的默认值
map.getOrDefault(42, "not found");  // not found
```

map中的entries合并也是非常容易的：

```java
map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9

map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9concat
```

如果key-value键值对不存在，则合并到map中。否则执行function函数来改变其value值。


## 日期 API

Java 8提供了全新的日期和时间API，位于 `java.time`包下面。全新的日期API与[Joda-Time](http://www.joda.org/joda-time/) 库相当，但它也[不太一样](http://blog.joda.org/2009/11/why-jsr-310-isn-joda-time_4941.html)。下面的例子涵盖了此新API的最重要部分。

### 时钟

时钟提供了对当前日期和时间的访问。时钟知晓时区，可以用来代替`System.currentTimeMillis()`来检索自Unix EPOCH以来的当前时间（以毫秒为单位）。在时间轴上的某一时刻用`Instant`表示。`Instant`可以创建遗留的`java.util.Date` 对象。

Clock provides access to the current date and time. Clocks are aware of a timezone and may be used instead of `System.currentTimeMillis()` to retrieve the current time in milliseconds since Unix EPOCH. Such an instantaneous point on the time-line is also represented by the class `Instant`. Instants can be used to create legacy `java.util.Date` objects.

```java
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();

Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```
代码：com.winterbe.java8.samples.time.LocalTime1

### 时区

时区是通过 `ZoneId`来表示，它提供了很多静态方法。时区定义了在瞬间和本地日期和时间之间转换的重要偏移。

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

代码：com.winterbe.java8.samples.time.LocalTime1

### 本地时间

LocalTime表示没有时区的时间，如晚上10点 或者 17:30:15。下文例子创建了两个带时区的本地时间。我们比较这两个时间，并计算两者之间的小时或分钟差值。

```java
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);

System.out.println(now1.isBefore(now2));  // false

long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);       // -3
System.out.println(minutesBetween);     // -239
```

LocalTime 提供了很多工厂方法用于创建各种新实例，包括解析时间字符串。

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

代码：com.winterbe.java8.samples.time.LocalTime1

### 本地日期

LocalDate表示不同的日期，如 2014-03-11。它是不可变的并且与LocalTime完全相似。示例演示了加或减天，月，年来计算新日期。请记住，每次操作都会返回一个新实例。


```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
LocalDate yesterday = tomorrow.minusDays(2);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);    // FRIDAY
```

从字符串解析LocalDate就像解析LocalTime一样简单：

```java
DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
System.out.println(xmas);   // 2014-12-24
```

代码：com.winterbe.java8.samples.time.LocalDate1

### 本地日期时间

LocalDateTime表示日期时间。它包含了上面的日期和时间组成一个实例。`LocalDateTime`是不可变的，它的工作方式类似于LocalTime 和 LocalDate。我们可以利用日期时间检索某些字段的方法：

代码：com.winterbe.java8.samples.time.LocalDateTime1

```java
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);      // WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);          // DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);    // 1439
```

通过时区的附加信息，它可以转换为时刻。轻松地将实例转换为`java.util.Date`类型的旧日期。

```java
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();

Date legacyDate = Date.from(instant);
System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
```
格式化日期-时间就象格式化日期或者时间一样。我们可以使用自定义模式创建格式化程序，而不是使用预定义的格式。

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy - HH:mm");
        LocalDateTime parsed = LocalDateTime.parse("07 25, 2017 - 14:00", formatter);
        System.out.println(parsed); // 2017-07-25T14:00
```

与 `java.text.NumberFormat` 不同， `DateTimeFormatter`  是不可改变的， **线程安全**.

更多语法详情， [参考这里](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).


## 注解

Java 8中的注解是可重复的。让我们通过一个例子说明。

首先，我们定义一个包装器注解，它包含实际注解的数组：

代码：com.winterbe.java8.samples.misc.Annotations1

```java
@interface Hints {
    Hint[] value();
}

@Repeatable(Hints.class)
@interface Hint {
    String value();
}
```

Java 8允许我们通过声明注解 `@Repeatable`来使用相同类型的多个注释。

### 形式 1: 使用容器注解 (旧方式)

```java
@Hints({@Hint("hint1"), @Hint("hint2")})
class Person {}
```

### 形式 2: 使用可重复注解 (新方式)

```java
@Hint("hint1")
@Hint("hint2")
class Person {}
```

形式2，java编译器隐式的设置`@Hints` 注解。这对于经由反射读取注解信息非常重要。

```java
Hint hint = Person.class.getAnnotation(Hint.class);
System.out.println(hint);                   // null

Hints hints1 = Person.class.getAnnotation(Hints.class);
System.out.println(hints1.value().length);  // 2

Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
System.out.println(hints2.length);          // 2
```

虽然我们从未在 `Person` 类上定义`@Hints`注解，但它仍然可以通过 `getAnnotation(Hints.class)`读取。然而，更方便的是 `getAnnotationsByType`能访问所有带`@Hint`的注解。


此外，Java 8 注解扩展了两个新的target：


```java
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface MyAnnotation {}
```

## 后续计划

我的Java 8 编程指南到这里就结束了。如果你想了解有关JDK 8 API的所有新类及功能特性，下载代码[JDK8 API Explorer](http://winterbe.com/projects/java8-explorer/)。它会帮助你找到所有新增的类，如`Arrays.parallelSort`, `StampedLock` 以及 `CompletableFuture`，仅举几例。

后续精彩文章请关注我的 [博客](http://winterbe.com) ，有你感兴趣的内容:

- [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [Java 8 Nashorn Tutorial](http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)
- [Java 8 Concurrency Tutorial: Threads and Executors](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)
- [Java 8 Concurrency Tutorial: Synchronization and Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)
- [Java 8 Concurrency Tutorial: Atomic Variables and ConcurrentMap](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)
- [Java 8 API by Example: Strings, Numbers, Math and Files](http://winterbe.com/posts/2015/03/25/java8-examples-string-number-math-files/)
- [Avoid Null Checks in Java 8](http://winterbe.com/posts/2015/03/15/avoid-null-checks-in-java/)
- [Fixing Java 8 Stream Gotchas with IntelliJ IDEA](http://winterbe.com/posts/2015/03/05/fixing-java-8-stream-gotchas-with-intellij-idea/)
- [Using Backbone.js with Java 8 Nashorn](http://winterbe.com/posts/2014/04/07/using-backbonejs-with-nashorn/)

[关注我的Twitter](https://twitter.com/winterbe_)。感谢阅读！
