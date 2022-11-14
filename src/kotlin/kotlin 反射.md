




## 1.元编程

描述数据的数据为元数据，操作元数据的编程叫元编程。通常需要获取程序本身的信息或者直接生产程序的一部分。元编程可以消除某些样板代码。

程序即是数据，数据即是程序

元编程像高阶函数一样，是一种高阶的抽象。高阶函数将函数作为输入输出，而元编程则是将程序作为输入输出。

### 1.1.常见元编程技术

实现方式：

1.运行时通过api暴漏程序信息

2.动态执行代码

3.通过外部程序实现目的

有几种实现方式：反射 宏模板元编程 路径依赖类型

## 2. Class和KClass
 ![请添加图片描述](https://img-blog.csdnimg.cn/0e6517e7417a40e684fc794ff8c10b66.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATk8gRXhjZXB0aW9uPw==,size_18,color_FFFFFF,t_70,g_se,x_16)
java 主要类结构

 ![请添加图片描述](https://img-blog.csdnimg.cn/998ee8f9808c4fe0bd262ce35624a35f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATk8gRXhjZXB0aW9uPw==,size_13,color_FFFFFF,t_70,g_se,x_16)


kotlin类结构

1. kotlin的KClass和Java的Class类似，代表类文件对象。且可以实现kotlin和Java互相转换
2. KCallable和AccessiableObject都为可访问对象.可以理解被执行的对象。
3. kotlin的KProperty和Java中的field不太一样，KProperty对应setter和getter方法。field指向字段本身。

> 注意：kotlin在某些情况下，编译器会在生产的字节码中存储额外信息，这些信息通过Kotlin.Metadata注解来实现。kotlin编译器会将Metadata标注这些类。



使用Java 实现把bean的每个字段 存入map集合

```java
 public static <A> Map<String,Object> toMap(A a){
        Field[] fs = a.getClass().getDeclaredFields();
        Map<String,Object> kvs =new java.util.HashMap<String,Object>();
        java.util.Arrays.stream(fs).forEach((f) -> {
            
            f.setAccessible(true);
            kvs.put(f.getName(),f.get(a))
        })
        
        return kvs;
    }
```

### 1.java 的Class

我们在kotlin中也可以使用Java的Class对象，反之不行，因为kotlin是兼容Java，Java不兼容kotlin.

```kotlin
//在kotlin中获取Class对象

class ReflectDemo(val x: Int = 0) {

    constructor() : this(0) {
    }

    fun test() {
        println(x)
    }
}



//获取Class对象实例的两种方法
val class1 = ReflectDemo().javaClass

val class2: Class<ReflectDemo> = ReflectDemo::class.java

```

### 2.kotlin 的 KClass

在kotlin中获取KClass对象。包含常用的属性

```kotlin
//    获取KClass对象实例方法
val kClass1 = ReflectDemo::class
val kClass2 = ReflectDemo().javaClass.kotlin



//    抽象类
kClass1.isAbstract
//    伴生对象
kClass1.isCompanion
//    数据类
kClass1.isData
//    是否final修饰
kClass1.isFinal
//    函数式接口
kClass1.isFun
//    内部类
kClass1.isInner
//是否open
kClass1.isOpen
//是否为密封类
kClass1.isSealed
//未理解，欢迎交流
kClass1.isValue
//返回该实例对象
kClass1.objectInstance
//伴生object实例
kClass1.companionObjectInstance
//    扩展属性
kClass1.declaredMemberExtensionProperties
//扩展函数 返回这个类声明的扩展函数，不是其他位置的声明的本类扩展函数
kClass1.declaredMemberExtensionFunctions
//    本类及超类扩展属性
kClass1.memberExtensionProperties
//本类及超类扩展函数
kClass1.memberExtensionFunctions
//泛型通配类型？？？未理解
kClass1.starProjectedType
    
```

kotlin对象转换为map

```kotlin
fun <A : Any> toMap(a: A): Map<String, Any?> {
    //遍历a的所有属性，name to value
    return a::class.members.map { m ->
        val p = m as KProperty
        p.name to p.call(a)
    }.toMap()
}
```

## 3. KCallable 可调用的对象

class中的属性和方法 构造函数都是callable对象。他们都可以调用。

 KMutableProperty extends KProperty extends KCallable

KMutableProperty 代表对一个字段可以修改的对象。

KProperty 代表只读的对象

执行方法调用call()

```kotlin
/**
 * Represents a callable entity, such as a function or a property.
 * 代表一个可调用的实体对象，一个方法或者一个属性
 * @param R return type of the callable.
 */
public actual interface KCallable<out R> : KAnnotatedElement {
    public actual val name: String

    public val parameters: List<KParameter>

    public val returnType: KType


    public val typeParameters: List<KTypeParameter>

    public fun call(vararg args: Any?): R

    /**
         * Calls this callable with the specified mapping of parameters to arguments and returns the result.
         * If a parameter is not found in the mapping and is not optional (as per [KParameter.isOptional]),
         * or its type does not match the type of the provided value, an exception is thrown.
         */
    public fun callBy(args: Map<KParameter, Any?>): R

    /**
         * Visibility of this callable, or `null` if its visibility cannot be represented in Kotlin.
         */
    @SinceKotlin("1.1")
    public val visibility: KVisibility?

    /**
         * `true` if this callable is `final`.
         */
    @SinceKotlin("1.1")
    public val isFinal: Boolean

    /**
         * `true` if this callable is `open`.
         */
    @SinceKotlin("1.1")
    public val isOpen: Boolean

    /**
         * `true` if this callable is `abstract`.
         */
    @SinceKotlin("1.1")
    public val isAbstract: Boolean

    /**
         * `true` if this is a suspending function.
         */
    @SinceKotlin("1.3")
    public val isSuspend: Boolean
}    
```

##  4. 获取参数信息

### 1.1 KParameter 函数的参数

使用KCallable.parameters获取一个List<KParameter>,代表函数的参数，api如下：

KParameter 函数的参数，包括扩展函数的参数。

```kotlin

/**
 * Represents a parameter passed to a function or a property getter/setter,
 * including `this` and extension receiver parameters.
	代表传递给方法的参数或者属性的getter/setter方法
 */
public interface KParameter : KAnnotatedElement {
    /**
     * 0-based index of this parameter in the parameter list of its containing callable.
     在参数列表的位置
     */
    public val index: Int

    /**
     * Name of this parameter as it was declared in the source code,
     * or `null` if the parameter has no name or its name is not available at runtime.
     * Examples of nameless parameters include `this` instance for member functions,
     * extension receiver for extension functions or properties, parameters of Java methods
     * compiled without the debug information, and others.
     	参数的名称；如果参数没有名称或其名称在运行时不可用，则返回“null”。
     */
    public val name: String?

    /**
     * Type of this parameter. For a `vararg` parameter, this is the type of the corresponding array,
     * not the individual element. 参数的类型，对于可变参数，返回对应数组的类型
     */
    public val type: KType

    /**
     * Kind of this parameter.待补充
     */
    public val kind: Kind

    /**
     * Kind represents a particular position of the parameter declaration in the source code,
     * such as an instance, an extension receiver parameter or a value parameter.
     Kind 表示参数声明在源代码中的特定位置，例如实例、扩展接收器参数或值参数
     */
    public enum class Kind {
        /** Instance required to make a call to the member, or an outer class instance for an inner class constructor. */
        INSTANCE,

        /** Extension receiver of an extension function or property. */
        EXTENSION_RECEIVER,

        /** Ordinary named value parameter. */
        VALUE,
    }

    /**
     * `true` if this parameter is optional and can be omitted when making a call via [KCallable.callBy], or `false` otherwise.
     *
     * A parameter is optional in any of the two cases:
     * 1. The default value is provided at the declaration of this parameter.
     * 2. The parameter is declared in a member function and one of the corresponding parameters in the super functions is optional. 是否可选参数
     */
    public val isOptional: Boolean

    /**
     * `true` if this parameter is `vararg`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/functions.html#variable-number-of-arguments-varargs)
     * for more information.是否可变参数
     */
    @SinceKotlin("1.1")
    public val isVararg: Boolean 
}

```

对于属性和无参数的函数，它们都有一个隐藏的参数为类的实例，而对于声明参数的函数，类的实例为第一个参数，而声明的参数作为后缀的参数。。对于那些从Any继承过来的参数，kotlin默认它们的第一个参数为Any。

demo

```kotlin
class Person(val name: String, val age: Int, var address: String) {

    fun find(name:String): List<String> {
        return listOf("hebei", "beijing")
    }
}

val person = Person("lisi", 44, "北京")
for (c in Person::class.members) {
    print("${c.name}")
    for (person in c.parameters) {
        println("---> ${person.type}")
    }
}
/**  打印结果：  
    address---> com.example.kotlin_sample.ch3_reflect.Person
    age---> com.example.kotlin_sample.ch3_reflect.Person
    name---> com.example.kotlin_sample.ch3_reflect.Person
    find---> com.example.kotlin_sample.ch3_reflect.Person
    ---> kotlin.String
    
    equals---> com.example.kotlin_sample.ch3_reflect.Person
    ---> kotlin.Any?
    hashCode---> com.example.kotlin_sample.ch3_reflect.Person
    toString---> com.example.kotlin_sample.ch3_reflect.Person

**/
```

> 如果person 是data class 打印结果还有会copy()  `componentN()` 函数 

### 1.2 KType

KParameter的type代表Kcallable的参数的类型.简单理解为属性或者方法的返回值的类型。

每一个Kcallable都可以使用returnType来获取返回值得类型。

```kotlin

package kotlin.reflect

/**
 * Represents a type. Type is usually either a class with optional type arguments,
 * or a type parameter of some declaration, plus nullability.
 */
public actual interface KType : KAnnotatedElement {
    /** 
    该类型在类声明层面的类型，如List<String> 只会得到List,(忽略类型参数)
     * The declaration of the classifier used in this type.
     * For example, in the type `List<String>` the classifier would be the [KClass] instance for [List].
     *
     * Returns `null` if this type is not denotable in Kotlin, for example if it is an intersection type.
     */
    @SinceKotlin("1.1")
    public actual val classifier: KClassifier?

    /**
     * Type arguments passed for the parameters of the classifier in this type.
     * For example, in the type `Array<out Number>` the only type argument is `out Number`.
     *
     * In case this type is based on an inner class, the returned list contains the type arguments provided for the innermost class first,
     * then its outer class, and so on.
     * For example, in the type `Outer<A, B>.Inner<C, D>` the returned list is `[C, D, A, B]`.
     	该类型的类型参数
     */
    @SinceKotlin("1.1")
    public actual val arguments: List<KTypeProjection>

    /**
     * `true` if this type was marked nullable in the source code.
      该类型是否标记为可空类型
     *
     * For Kotlin types, it means that `null` value is allowed to be represented by this type.
     * In practice it means that the type was declared with a question mark at the end.
     * For non-Kotlin types, it means the type or the symbol which was declared with this type
     * is annotated with a runtime-retained nullability annotation such as [javax.annotation.Nullable].
     *
     * Note that even if [isMarkedNullable] is false, values of the type can still be `null`.
     * This may happen if it is a type of the type parameter with a nullable upper bound:
     *
     * ```
     * fun <T> foo(t: T) {
     *     // isMarkedNullable == false for t's type, but t can be null here when T = "Any?"
     * }
     * ```
     */
    public actual val isMarkedNullable: Boolean
}
```

案例

```kotlin
for (c in Person::class.members) {
    println("${c.name}:${ c.returnType.classifier}")
}

address:class kotlin.String
age:class kotlin.Int
name:class kotlin.String
find:class kotlin.collections.List
equals:class kotlin.Boolean
hashCode:class kotlin.Int
toString:class kotlin.String
```

### 1.3 KTypeParameter

类型参数：在KClass 和KCallable中可以通过typeParameters来获取class和callable的类型参数，返回的结果是List<KTypeParameter>,不存在类型参数时返回一个null的list。

个人理解为：泛型的类型参数

```kotlin
class Person(val name: String, val age: Int, var address: String) {

    fun find(name:String): List<String> {
        return listOf("hebei", "beijing")
    }

    fun <A,B> get(a:A):A{
        return a
    }
}
val person = Person("lisi", 44, "北京")

for (c in Person::class.members) {
	println("${c.name}:${  c.typeParameters}")
}
/**
    address:[]
    age:[]
    name:[]
    find:[] 
    get:[A,B]
    equals:[]
    hashCode:[]
    toString:[]
**/
```



## 获取实例对象

## 获取实例方法

## 获取实例属性

## 获取实例属性