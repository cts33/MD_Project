Python的优点很多，简单为大家列出几点。

简单明确，跟其他很多语言相比，Python更容易上手。
能用更少的代码做更多的事情，提升开发效率。
开放源代码，拥有强大的社区和生态圈。
能够做的事情非常多，有极强的适应性。
能够在Windows、macOS、Linux等各种系统上运行。
Python最主要的缺点是执行效率低，但是当我们更看重产品的开发效率而不是执行效率的时候，Python就是很好的选择。


Python的应用领域
目前Python在Web服务器应用开发、云基础设施开发、网络数据采集（爬虫）、数据分析、量化交易、机器学习、深度学习、自动化测试、自动化运维等领域都有用武之地。

整型（int）：Python中可以处理任意大小的整数，而且支持二进制（如0b100，换算成十进制是4）、八进制（如0o100，换算成十进制是64）、十进制（100）和十六进制（0x100，换算成十进制是256）的表示法。
浮点型（float）：浮点数也就是小数，之所以称为浮点数，是因为按照科学记数法表示时，一个浮点数的小数点位置是可变的，浮点数除了数学写法（如123.456）之外还支持科学计数法（如1.23456e2）。
字符串型（str）：字符串是以单引号或双引号括起来的任意文本，比如'hello'和"hello"。
布尔型（bool）：布尔值只有True、False两种值，要么是True，要么是False。

在Python中可以使用type函数对变量的类型进行检查
```python

a =20
print(type(a))    # <class 'int'>
```
int()：将一个数值或字符串转换成整数，可以指定进制。
float()：将一个字符串转换成浮点数。
str()：将指定的对象转换成字符串形式，可以指定编码。
chr()：将整数转换成该编码对应的字符串（一个字符）。
ord()：将字符串（一个字符）转换成对应的编码（整数）。

列表 类似于list集合
元组（tuple）类似数组
元组和列表的不同之处在于，元组是不可变类型，这就意味着元组类型的变量一旦定义，其中的元素不能再添加或删除，而且元素的值也不能进行修改

```python
# 定义一个三元组
t1 = (30, 10, 55)
# 定义一个四元组
t2 = ('骆昊', 40, True, '四川成都')
```
字符串

```python
 a = 'hello'
```
集合set
```python

set1 = {1,2,4}
```

字典
字典可以嵌套

```python
person = {'name': '王大锤', 'age': 55, 'weight': 60, 'office': '科华北路62号'}

# 全局变量
def test1():
    global a
    a = a + 1
    print(a)


#返回值  ，可以有多个返回值，默认以 元组返回，也可以是列表，集合,
def test2():
    return 1,2
    # return {1,2}

# 可变参数
def test3(*args):
    print(args)

# 关键字参数
def test4(**kwargs):
    print(kwargs)

test4(name='tom',age=20)

# 元组、字典拆包

def getNums():
    return 1,2
n1,n2 = getNums()

dict = {'name':"tom",'age':29}

# 获取的是字典的key
a,b = dict

v1 = dict[a]
v2 = dict[b]

#交换变量

a,b = b,a

# 引用  id()  内存地址
a=1
b=a
# a，b引用相同
print(id(a))
print(id(b))

# 可变类型和不可变类型

a =1
b=a
#a重新赋值，内存地址会改变
a=2

aa = [1,2,3,4]
aa.append(5)
# 此时aa的内存地址是不变的
print(id(aa))


# 文件读写 及序列化
# r w x:写入，如果文件存在，发生异常；a:追加  b:二进制模式；+ ：更新模式；t:文本模式（默认）



file = open('致橡树.txt', 'r', encoding='utf-8')
print(file.read())
file.close()


file = open('致橡树.txt', 'a', encoding='utf-8')
file.write('\n标题：《致橡树》')
file.write('\n作者：舒婷')
file.write('\n时间：1977年3月')
file.close()

file = None
try:
    file = open("demo.md",'r',encoding='utf-8')
    print(file.read())
except FileNotFoundError:
    print("error")
finally:
    file.close()






```









