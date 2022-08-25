## 1.run

run 是任何类型T的通用扩展函数，执行了`返回类型为R的扩展函数block`,最终返回该扩展函数的结果。

```kotlin

@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}
```

1.对象配置并且计算结果

```kotlin
 val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}
```

2.在需要表达式的地方运行语句：非扩展的run

除了在接收者对象上调用 `run` 之外，还可以将其用作非扩展函数。 非扩展 `run` 可以使你在需要表达式的地方执行一个由多个语句组成的块。

```kotlin
fun main() {
//此处run返回一个对象，且初始化对象并返回
    val hexNumberRegex = run {
        val digits = "0-9"
        val hexDigits = "A-Fa-f"
        val sign = "+-"

        Regex("[$sign]?[$digits$hexDigits]+")
    }

    for (match in hexNumberRegex.findAll("+1234 -FFFF not-a-number")) {
        println(match.value)
    }
}
```

## 2.let

`let` 可用于在调用链的结果上调用一个或多个函数，或者说基于代码的结果再执行相关逻辑

```kotlin

@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}
```

```kotlin
tabList?.let { it: ArrayList<Tab> ->
    createWebView(tabList)
}
//tabList不为null，就执行逻辑        
```

it参数：表示主体对象，可以基于当前对象做下一步操作。it也可以修改为其他标识

`let` 经常用于仅使用非空值执行代码块，返回的也是高阶函数的结果

## 3. run  let 区别：

相同点：都返回一个lambda表达式 run方法的高阶函数为block: T.() -> R let方法的高阶函数为block: (T) -> R,T作为参数传入，所以it代码T对象，也就是A对象。 不同点： run 高阶内部的无参数
let 高阶内部有参数，且为主体自身对象

例子：

```kotlin
class A {
    val x = 11
    fun test1(): String {
        return "this is A.test1()"
    }
    fun test2() {}
}

fun testRun() {
    val len = A().run { //此处有个无形的this 
        test1()
    }.length
    println("A的run 方法返回字符串长度为$len")
}

fun testLet() {
    val result = A().let { it ->
        it.test1()
    }.length
    println("A的let 方法返回字符串为$result")
}

fun main() {
    testRun()
    testLet()
}

```

通过代码可以看到A对象，`run方法的作用域就是A对象，可以直接调用test1()方法`， 但是let不同，是通过it关键字才能执行test1()方法，这是最大的不同，也是唯一的不同的地方。

## 4.with

```kotlin
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}
```

查看源码，个人认为with和run 区别不大， 1.with需要一个接收者 2.尾部的高阶函数的作用域就是接收者的作用域，也是this表示。返回的结果是高阶函数的结果。

例子：

```kotlin
fun testWith() {
    with(A()) {
        this.test1()
    }
}
```

## 5.apply

apply也类似于run,高阶函数的作用域为当前接收者对象作用域，高阶函数无返回值，但是方法为返回值，为接收者对象。

```kotlin
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}
```

主要应用于对象的配置，返回**对象自身**

```kotlin
val adam = Person("Adam").apply { //this ->
    age = 32
    city = "London"
}
println(adam)
```

## 6.also

参数为it，返回**自身对象**
also 类似于let，但是注意高阶函数的返回值为自身T对象

```kotlin

public inline fun <T> T.also(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}
```

```kotlin
val numbers = mutableListOf("one", "two", "three")
numbers.also {
    println("The list elements before adding new one: $it")
}.add("four")
```

案列：

```kotlin
fun testAlso() {
    A().also { it ->
        it.x == 44
    }.test1()
}
```

it代码A对象，高阶函数的返回值还是A对象，所以后缀还可以调用test1()

## 7.takeIf 、takeUnless

takeIf 可以即判断，还可以加入条件。

```kotlin
public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? {
    contract {
        callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
    }
    return if (predicate(this)) this else null
}
```

只操作单条数据，相反的有takeUnless函数

```kotlin
val result = Student.takeif { it.age > 18 }.let { ... }
```

