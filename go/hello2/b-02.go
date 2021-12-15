/*
包名为 main，表示当前 go 将会被编译成二进制可执行文件
当前文件想被编译成二进制的可执行文件，除了 main 包，还得出现 main 函数
如果是我们写一个工具包，那么不用取名 main


变量声明 

var 变量名 变量类型

变量名推荐驼峰命名

同一个作用域下不能声明重复的变量

标准声明

var name string
var age int
var isDone bool

声明变量的同时并赋值

var name string = "cxd"

Go 支持类型推倒
var name = "cxd"

批量声明全局变量

var (
	name string
	age int
	isDone bool
)

短变量声明

name := "cxd"

短变量声明，要求只能在函数中使用
变量一旦声明，必须使用

变量先声明，再使用，一旦声明了，必须使用

匿名变量

func foo()(int, string) {
	return 1, "tom"
}

const (
	n1 = 100
	n2
	n3
)

func main() {
	x, _ := foo()
	_, y := foo()
	fmt.Println(x)
	fmt.Println(y)
	
	fmt.Println(n1)
	fmt.Println(n2)
	fmt.Println(n3)
}

Go 中匿名变量使用 _ 表示，表示占位与忽略值
如果我们确定不会使用函数的返回值，就将其表示成匿名变量
匿名变量不占用命名空间，不会分配内存，所以匿名变量不存在啊重复声明

常量

声明常量
const pi = 3.14
const e = 2.71

批量声明常量
const (
	name = "cxd"
	age = 23
)

声明多个常量同时使用相同值，var 变量则不行

尝试修改变量时，编译器会爆红
*/
package main

import "fmt"

const (
	n1 = 100
	n2
	n3
)

func main() {
	fmt.Println("hello world")
	
	fmt.Println(n1)
	fmt.Println(n2)
	fmt.Println(n3)
}