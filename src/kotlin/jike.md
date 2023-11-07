# 1.解构:把一个对象的各个属性进行拆解

	val user = User(12,"lisi")
	val (name,age) = user


class User(var age:Int,var name:String){
	operator componment1() = name
	operator componment2() = age
}
用于map

val map = mapof<String,String>("key" to "k")
for((k,v) in map){
	print("$k--$v")
}



# 2.循环

 for (i in 1..10)

 for (i in 1 unit 10)  相当于重载..符号  1到10

 for(i in 10 downTo 1) 反向遍历

 for(i in 1..10 step 2) 步长2

 repeat(10){} 循环10次

 # 3.集合操作符

 val list = arrayListOf<Char>('a','b')
 val a = list.map{ it - 'a'}
 		.filter{ it > 0 }
 		.find{ it > 1}

# 4 作用域函数

let有闭包参数，run无闭包参数

val result = user.run{"run::${this.javaclass}"}
val result = user.let {user:User -> "let::${user.javaclass}"}

also  apply 都不返回闭包结果，also有闭包参数 apply 没有闭包参数

user.also{
	print("also::${it.javaClass}")
}.apply{
	print("apply::${it.javaClass}")

}.name

多用于链式调用

takeIf 的闭包返回一个判断结果，为false，返回空
takeUnless 刚好相反，闭包的判断结果为true，会返回null

user.takeIf{it.name.length>0}?.also{ print("姓名为${it.name}")} ?:println("姓名为null")

user.takeUnless{it.name.length>0}?.also{println("姓名为null")}?:println("姓名为${user.name}")

with 顶级函数

with(user){
	this.name = "with"
}

contains
elementAt
firstOrNull
lastOrNull
indexOf
singleOrNull

any
all
none
count
reduce 从第一个到最后一个进行累计

filter

map

reversed 反转
sorted 排序


# 5  运算符重载?????

for(i in 1..10)



# 6 反引号 ``

fun `123`(){}
fun ` `(){} 可以用空格定义方法名

只支持kotlin调用的时候

# 7 比较对象

==  	equals
=== 	 ==

kotlin   java

# 8.typealias 类型链接

val a:File = A("")

public typealias A = File

这样a就可以当做File 来执行

# 9 DSL domain special launage 领域专门语言
提高开发效率，减小沟通成本
Anko Kolly build.gradle 内部DSL


# 10 常量 变量 只读

javap *.class  反编译class

-l
-public

val 不能有setter，不能再赋值
val 不等于常量

const val v =0; //常量

# 11 作用域函数

let有闭包参数
run无闭包参数





