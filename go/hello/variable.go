/*
声明变量
var identifier type

变量声明
第一种，指定变量类型，如果没有初始化，则变量默认为零值

数值类型 0
布尔类型 false
字符串 ""
其它类型 nil

第二种，根据值自行判定变量类型

var v_name = value

如 var d = true

第三种，省略 var，注意 := 左侧如果没有声明新的变量就会产生编译错误

v_name := value

var intVal int
intVal := 1 //这时候会产生编译错误，因为 intVal 已经声明，不需要重新声明

intVal :=1 // := 是一个声明语句

相当于
var intVal int
intVal = 1

值类型和引用类型

所有像 int、float、bool、string 这些基本类型都属于值类型，使用这些类型的变量直接指向内存中只ii

当使用等号将一个变量的值给另一个变量时，实际上是在内存中进行了拷贝

你可以通过 &i 来获取变量 i 的内存地址


*/
更复杂的数据通常会需要使用多个字，这些数据一般使用引用类型保存
一个引用类型的变量 r1 存储的是 r1 的值所在内存地址或内存地址中第一个字所在的位置
package main

var x, y int
var ( // 这种因式分解关键字的写法一般用于声明全局变量
	a int
	b bool
)

var c, d int = 1, 2
var e, f = 123, "hello"

// 这种不带声明格式的只能在函数体中出现
// g, h := 123, "hello"

// import "fmt"
func main() {
	// var a string = "cxd"
	// fmt.Println(a)
	
	// var b,c int = 1, 2
	// fmt.Println(b, c)
	g, h := 123, "hello"
	println(x, y, a, b, c, d, e, f, g, h)
}