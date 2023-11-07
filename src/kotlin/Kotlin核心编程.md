# 1. 认识Kotlin

在Java之后，JVM平台，Scala和kotlin算是平台的佼佼者。Scala是大数据领域的明星。kotlin在2017年成为Android平台官方支持语言。

## 1.1 Java发展

Java是最成功语言之一

* 多平台强大的社区支持

* 尊重标准

  Java5引入了泛型，是一个重大进步，java8也很重要。

 * 高阶函数和Lambda 首次突破了只有类是“头等公民”的设计，支持将函数作为参数来传递，同时结合Lambda，改变现有程序设计模式。

 * Stream API 简化日常开发中的集合操作

 * Optional类 为消除null引起的nullpointerException问题

















# 2.基础语法



## 2.1 类型声明

```kotlin
val a:String = "I am String"
```

增强的类型推导

```
val int = 1314	
```

类型推导提高kotlin这种静态语言的开发效率。

函数返回值类型

```
fun sum(x:Int,y:Int):Int = x+y
```

> > Unit是一种类型，void是一个关键字。

## 2.2 var val 

var 变量

val 常量：只能被赋值一次，声明时不能省略类型；**引用不可变，内容可变**，优先使用

```
val book = Book("this is book")
book.name = "this is java"// 内容是可变的
```

## 2.3 高阶函数 Lambda

函数是头等公民，不仅可以像类一样在顶层定义一个函数，还可以在一个函数内部定义一个函数。

```
fun foo(x:Int){
	fun double(y:Int):Int{
		return y*2
	}
	println(double(x))
}

```

还可以将一个函数作为参数来传递。

### 2.3.1 将函数作为参数传递

1.方法作为参数传递，必须像其他参数一样具备具体的类型信息。

2.需要把方法引用当作参数传递给另一个方法

* 函数类型

  ```
  （Int）-> Unit 
  ```

  左边参数，右边返回类型

  必须有括号来包裹参数类型，

  返回类型显式声明

* 各种情况

    ```
    () -> Unit
    
    (Int,String) -> Unit
    //？表示参数可以null
    (errcode:Int,errMsg:String?) -> Unit
    
    //该函数类型的变量也是可选的话
    ((errcode:Int,errMsg:String?) -> Unit)?
    
    //高阶函数支持返回另外一个函数
    //代表传入一个Int参数，返回一个类型为(Int) -> Unit 的函数类型
    (Int) -> ((Int) -> Unit )
    
    ```

案例

    //test:(Country)->Boolean 函数类型
    fun filterCountries(contries:List<Country>,test:(Country)->Boolean):
    	List<Country>{
    	
    	val res = mutableListOf<Country>()
            for( c in countries){
    			if(test(c)){
    				res.add(c)
    			}
            }
            return res
    	}

​    



### 2.3.4 方法和成员引用

kotlin通过冒号实现对于某个类的方法进行引用。

```
countryTest::isBigEuropeanCountry
```

```

class Book(val name: String)

fun main() {
    //使用构造函数的引用来调用属性
    val getBook = ::Book
    println(getBook("this is book").name)
}
```

同样引用成员变量可以：

```
Book::name
```

### 2.3.5 匿名函数

```
fun(country:Country):Boolean{
	return country.population>1000
}

//可以直接通过匿名调用
countryApp.filterCountries(countries,fun(country:Country):Boolean{
	return  country.population>1000
})
```

### 2.3.6 Lambda语法糖

```
countryApp.filterCountries(countries, { country ->
		country.continient == "EU" && country.population > 10000
})
```

lambda语法：

1.使用大括号

2.如果lambda声明了部分参数，返回值类型支持类型推导，那么Lambda就可以省略函数类型声明

3.如果lambda变量声明了函数类型，那么lambda参数部分的类型就可以省略

eg:

```
	//有参数加：
	val sum0: (Int, Int) -> Int = { x: Int, y: Int ->
		x + y
	}
	val sum1 = { x: Int, y: Int ->
		x + y
	}

	val sum2: (Int, Int) -> Int = { x, y ->
		x + y
	}
```

如果lambda表达式返回的不是Unit,那默认最后一行就是返回值类型。

* 单个参数的隐式名称

  ```
  fun foo(i: Int) = {
  	print(i)
  }
  //注意it
  listOf(1, 2, 3).forEach { foo(it) }
  listOf(1, 2, 3).forEach { foo(it).invoke() }
  listOf(1, 2, 3).forEach { foo(it)() }
  ```

  





















# 3.Kotlin核心
# 4.代数数据类型和模式匹配

