
# 1.命令解析器
shell就是命令解释器

命令解析器的作用：对用户输入到终端的命令进行解析，调用对应的执行程序。
用户在终端输入命令, 由shell命令解释器对命令进行解析(按照$PATH环境变量搜索命令), 解析成内核能够识别的指令, 然后由内核执行命令, 最后由终端显示命令执行的结果给用户.
>注意：shell在寻找命令的时候是按照$PATH环境变量去查找的，如果找到了就执行对应的命令，若找不到就报错, 执行echo $PATH可以查看PATH环境变量的值.

```cpp
	常用的命令解析器：
shell -- Bourne Shell
	/bin/sh
bash -- Bourne Again Shell
	/bin/bash
	当前系统所使用的shell
echo $SHELL
	当前系统下有哪些shell
cat /etc/shells

```
# 2.	linux下的目录结构
 
![系统目录结构.drawio.png](img%2F%E7%B3%BB%E7%BB%9F%E7%9B%AE%E5%BD%95%E7%BB%93%E6%9E%84.drawio.png)
##  2.1	linux下主要目录介绍

```cpp
 /bin: binary，二进制文件，可执行程序，shell命令
 如: ls , rm , mv, cp等常用命令
 
 /sbin: s是Super User的意思，这里存放的是系统管理员使用的系统管理程序。
 如ifconfig, halt, shutdown, reboot等系统命令
 
 /dev: device，在linux下一切皆文件
 硬盘, 显卡, 显示器
 字符设备文件、块设备文件
 如: 在input目录下执行: sudo cat mouse0, 移动鼠标会显示有输入.
 
 /lib: linux运行的时候需要加载的一些动态库
 如: libc.so、libpthread.so等
 
 /mnt: 手动的挂载目录, 如U盘等
 
 /media: 外设的自动挂载目录, 如光驱等。
 
 /root: linux的超级用户root的家目录
 
 /usr: unix system resource--类似于WINDOWS的programe files目录
 
 include目录里存放头文件, 如: stdio.h、stdlib.h、string.h、pthread.h
 games目录下的小游戏-如: sl小火车游戏
 

/etc: 存放配置文件
 
 /etc/passwd
 man 5 passwd可以查看passwd文件的格式信息 

 /etc/group
 man 5 group可以查看group文件的格式信息 

 /etc/profile
 系统的配置文件, 修改该文件会影响这个系统下面的所有的用户
 
 /opt: 安装第三方应用程序
 比如安装oracle数据库可以在这个目录下
 
 /home: linux操作系统所有用户的家目录
 用户家目录：(宿主目录或者主目录）	/home/cts
 
 /tmp: 存放临时文件
 新建在这个目录下的文件会在系统重启后自动清除
```


# 3.文件和目录操作相关的命令

```python
#tree命令  以树状形式查看指定目录内容
tree  --  树形结构显示当前目录下的文件信息
tree 目录  -- 树形结构显示指定目录下的文件信息	

#使用该命令需要安装软件tree
sudo apt-get update    
sudo apt-get install tree

# ls命令  查看指定目录下的文件信息
文件类型 （第1个字符）
	- ： 普通文件
	d ： 目录
	l ：  符号链接，相当于windows中的快捷方式
	s ： 套接字
	p ： 管道
	b ： 块设备
	c ： 字符设备

# cd 命令 切换目录(change directory), 命令使用方式: cd + 路径
# pwd命令  查看用户当前所处的工作目录,  
# which命令 显示命令所在的目录
# touch命令
如果文件不存在, 创建新文件, 
如果文件存在, 更新文件的最后修改时间。命令使用方式：touch 文件名

# mkdir命令 创建新目录 
创建方式：mkdir目录名 ,如果创建多级目录需要添加参数 -p

# rmdir命令 删除空目录，只能删除空目录，使用方式：rmdir 目录名
# rm命令
 	删除文件： rm 文件名
 	删除目录： rm  -r 目录名
 	参数：
 	-r：递归删除目录，删除目录必须添加此参数
 	-i：提示用户是否删除文件或目录
 	-f：强制删除
 	注意事项：
使用rm命令删除的文件或目录不会放入回收站中，数据不易恢复。

# cp 命令 复制文件 cp 源目录或文件 目标目录或文件
# mv命令  改名或者移动文件 mv file1 file2
# cat命令
 	将文件内容一次性输出到终端。
 	使用方式： cat 文件名
 	缺点：终端显示的内容有限，如果文件太长无法全部显示。
 	可用于文件重定向: cat file1>file2, 相当于cp file1 file2

# more命令
 文件内容分页显示到终端，但是只能一直向下浏览，不能回退。
 使用方式：more + 文件名

# less命令
 文件内容分页显示到终端，可以自由上下浏览。
 使用方式：less 文件名

# head命令
 	从文件头部开始查看前n行的内容
 	使用方式：head -n[行数] 文件名
 	head -20 hello.txt  如果没有指定行数，默认显示前10行内容

# tail命令
 	从文件尾部向上查看最后n行的内容
 	使用方式：tail -n[行数] 文件名 如果没有指定行数，默认显示最后10行内容
 	一个比较重要的应用：显示日志 : tail -f test.log
	一个终端tail -f test.log , 另一个终端: echo “hello world” >>test.log

# 软链接  软连接类似于windows下的快捷方式，如何创建软连接
	ln -s 文件名 快捷方式的名字
	例如：ln -s aa aa.soft
 
 	目录也可以创建软连接
	例如：ln -s tmp tmp.link
 
 
#wc 显示文件行数, 字节数, 单词数
	wc -l file显示文件的总行数
	wc -c file显示文件的总字节数
	wc -w file显示文件的总单词数
	wc file 显示文件的总行数, 单词数和总字节数
# whoami 显示当前登陆的用户名
 



#  用户权限、用户、用户组
# 修改文件权限chmod
linux是通过权限对文件进行控制的, 通过使用chmod命令可以修改文件相关的权限.
1. 文字设定法
  
 命令：chmod [who] [+|-|=] [mode] 文件名
 

* 操作对象【who】
 	u -- 用户（user）
 	g -- 同组用户（group）
 	o -- 其他用户（other）
 	a -- 所用用户（all）【默认】
* 操作符【+-=】
 	\+   添加权限
 	\-   取消权限
 	\=  赋予给定权限并取消其他权限
* 权限【mode】
 	r -- 读
 	w -- 写
 	x -- 执行
>示例：给文件file.txt的所有者和所属组添加读写权限
 chmod ug+wr file.txt
2. 数字设定法
 
命令：chmod [+|-|=][mode] 文件名
 

* 操作符【+-=】
	+ -- 添加权限
	- -- 取消权限
	= -- 赋予给定权限并取消其他权限 (默认为=)
* 	数字表示的含义
	0 -- 没有权限(-)
	1 -- 执行权限（x）
	2 -- 写权限（w）
	4 -- 读权限（r)
>例：给file.txt文件设置 rw-rw-r--
 chmod 664 file.txt
   注意点: 使用数字设定法, 一定要使用3位的8进制数: 如:066
```python

# 4.2 修改文件所有者和所属组
1. 修改文件所有者chown
	用法：chown 文件所有者 文件名
	sudo chown mytest file.txt
2. 修改文件所有者和所属组chown

用法：chown 文件所有者:文件所属组 文件名
sudo chown mytest:mytest file.txt
sudo chown mytest.mytest file.txt

注意:普通用户需要使用管理员用户权限执行该命令
注意: 若系统没有其他用户, 可以使用sudo adduser 用户名 创建一个新用户.

#4.3 修改文件所属组
chgrp命令
 
使用方法：chgrp 用户组 文件或目录名
示例：修改文件所属组为mytest
 

sudo chgrp mytest file.txt
 
普通用户需要使用管理员权限执行该命令。
 

# 5.find命令
# 1.按文件名查询：
使用参数 -name
命令：find  路径  -name   "文件名"
示例：find /home -name "*.c"
## 2.按文件类型查询：
使用参数 -type
命令：find 路径 -type 类型
## 3.类型	
普通文件类型用f表示而不是 -
 	d -> 目录
 	l -> 符号链接
 	b -> 块设备文件
 	c -> 字符设备文件
 	s -> socket文件
 	p -> 管道文件
## 4.查找指定目录下的普通文件：
find 路径 -type f
 	按文件大小查询：使用参数 -size
 	命令：find  路径  -size  范围
## 5.范围
	大于：+表示 --  +100k
	小于：-表示  --  -100k
	等于: 不需要添加符号 --  100k
	大小
	M 必须大写（10M）
	k 必须小写（20k）
	c 表示字节数
例子: 查询目录为家目录
	等于100k的文件:  find ~/ -size 100k
	大于100k的文件:  find ~/ -size +100k
	大于50k, 小于100k的文件:  find ~/ -size +50k -size -100k
### 1.按文件日期
创建日期：-ctime -n/+ n
	-n: n天以内
	+n: n天以外
修改日期：-mtime -n/+n
访问日期：-atime -n/+n
### 2.	按深度
-maxdepth n(层数）
	搜索n层以下的目录, 搜索的层数不超过n层
-mindepth n（层数）
	搜搜n层以上的目录,搜索的层数不能小于n层
### 3.	高级查找
例：查找指定目录下所有目录，并列出目录中文件详细信息
	find ./ -type d -exec shell命令 {} \;
find ./ -type d -exec ls -l {} \;
	find ./ -type d -ok shell命令 {} \;
find ./ -type d -ok ls -l {} \;
注意: {}中间不能有空格
ok比较安全, 特别是在执行rm删除文件的时候.
	find ./ -type d | xargs shell命令
find ./ -type d | xargs ls -l
# 6.grep命令

	grep -r（有目录） “查找的内容” 搜索的路径
-r参数, 若是目录, 则可以递归搜索
-n参数可以显示该查找内容所在的行号
-i参数可以忽略大小写进行查找
-v参数不显示含有某字符串


>	搜索当前目录下包含hello world字符串的文件
 	grep -r -n "hello world" ./     ------显示行号
 	grep -r -i -n "HELLO world" ./  -------忽略大小小查找
# 7.find和grep命令结合使用
先使用find命令查找文件, 然后使用grep命令查找哪些文件包含某个字符串
 
find . -name "*.c" | xargs grep -n "main"
 

# 10	Linux中常用的压缩工具
##	gzip和bzip2
不能压缩目录，只能一个一个文件进行压缩，压缩之后会使原文件消失
	gzip *    压缩当前目录下所有的文件, 但是目录不能压缩
	gunzip *  解压当前目录下所有的.gz文件
	bzip2 *   压缩当前目录下所有的文件, 但是目录不能压缩
	bunzip2 * 解压当前目录下所有的. bz2文件
##		tar工具
相关参数说明
	z：用gzip来压缩/解压缩文件
	j：用bzip2来压缩/解压缩文件
	c：create, 创建新的压缩文件, 与x互斥使用
	x：从压缩文件中释放文件, 与c互斥使用
	v：详细报告tar处理的文件信息
	f：指定压缩文件的名字
	t:  查看压缩包中有哪些文件
##		压缩：
	tar cvf 压缩包名字.tar 原材料[要打包压缩的文件或目录]
	tar zcvf 压缩包名字.tar.gz 原材料[要打包压缩的文件或目录]
	tar jcvf 压缩包名字.tar.bz2 原材料[要打包压缩的文件或目录]
##	解压缩：
	tar  xvf   已有的压缩包（test.tar.gz）
	tar  zxvf  已有的压缩包（test.tar.gz）
	tar  jxvf  已有的压缩包（test.tar.bz2）
	解压到指定目录：添加参数 -C（大写）
	tar zxvf test.tar.gz -C 解压目录（./mytest）
查看压缩包中有哪些文件
	tar -tvf test.tar
	rar工具
使用前需要安装 rar 工具
	sudo apt-get install rar
压缩：
	命令： rar a -r 要压缩的文件(含文件或者目录)
	压缩目录需要使用参数：-r
rar a -r my aa bb dir  ----将aa bb dir压缩到my.rar文件中
	打包的生成的新文件不需要指定后缀
解压缩：
	命令：rar x xxx.rar 压缩目录
rar x my.rar  ----将my.rar解压到当前目录
	解压到指定目录, 直接指定解压目录即可
	rar x xxx.rar目录
rar x my.rar TAR  -----将my.rar解压到TAR目录下
				注意：若解压目录不存在则会报错
# zip工具
压缩：zip -r 压缩包名 要压缩的文件(含文件或目录)
压缩目录需要使用参数-r
使用该命令不需要指定压缩包后缀
zip -r xxx file dir ---生成xxx.zip文件
解压缩：unzip压缩包名
解压缩到指定目录：添加参数 –d 解压目录
unzip xxx.zip -d /home/itcast/test/day1
注意：解压目录若不存在则会创建．

# 软件的安装和卸载

```python
软件安装：sudo apt-get install 软件名
软件卸载：sudo apt-get remove 软件名
更新软件列表：sudo apt-get update
清理安装包：sudo apt-get clean
清理的是缓存路径：/var/cache/apt/archives
 

 软件包安装
在Ubuntu系统下必须有deb格式的安装包

 
# 软件安装
 
sudo dpkg -i xxx.deb
 
# 软件卸载

sudo dpkg -r 软件名
```
