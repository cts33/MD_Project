apply also返回自身对象

let with run 都是单纯一个作用域，返回的是lambda表达式

## let


## with

`with` 可以理解为“*对于这个对象，执行以下操作。*” this关键字代表自身对象。

一个对象的一组函数调用 使用with

```
with(menuView) {         
	this.setMenuData(tabList!!)          
}
```

还可以返回一个属性或者函数，然后被执行

```
val name = with(view){ this:MenuView->
	"name"
}

println(name)
```

## run


## apply

## also


