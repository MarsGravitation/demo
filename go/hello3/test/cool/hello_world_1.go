/*
https://coolshell.cn/articles/8460.html

Hello World

运行

package

fmt 输出格式

变量和常量
注意：go 是静态类型的语言

// 声明初始化一个变量
var x int = 100
var str string = "hello world"
// 声明初始化多个变量
var i, j, k int = 1, 2, 3

// 不指明类型，通过初始化值来推导
var b = true // bool

x := 100

常量：
const s string = "hello world"

数组
var a [5]int

数组的切片操作
a := [5]int{1, 2, 3, 4, 5}

b := a[2:4]

Golang 的切片是共享内存的，也就是说，没有数据的复制，只是记录从哪切到
信息

分支循环语句
if 语句
注意：if 语句没有圆括号，而必须要有花括号

switch
注意：switch 语句没有 break，还可以使用逗号 case 多个值

for 语句

关于分号
从上面的代码我们可以看到代码里没有分号。其实，和 C 一样，Go 的正式的语法使用分号来终止语
句。和 C 不同的是，这些分号由词法分析器在扫描源代码过程中使用简单的规则自动插入分号，因
此输入源代码多数时候就不需要分号了

注意：无论任何时候，你都不应该将一个控制结构（if、for、switch、select）的左大括号
放在下一行。如果这样做，将会在大括号的前方插入一个分号，这可能会导致不想要的结果

map

指针

内存分配
new 是一个分配内存的内建函数，但不同于其他语言中同名的 new 所作的工作，它只是将内存清
零，而不是初始化内存。new(T) 为一个类型为 T 的新项目分配了值为零的存储空间并返回其地址，
也就是类型为 *T 的值。用 Go 的术语来说，就是它返回了一个指向新分配的类型为 T 的零值的指
针。

make(T, args) 函数的目的与 new(T) 不同。它仅用于创建切片、map和chan（消息管道），并返
回类型 T（不是 *T）的一个被初始化了的（不是零）实例。这种差别的出现是由于类型实
质上是对在使用前必须进行初始化的数据结构的引用。例如，切片是一个具有三项内容的描述符，
包括指向数据（在一个数组内部）的指针、长度以及容量，在这三项内容被初始化之前，切片值为
nil。对于切片、映射和信道。make 初始化了其内部的数据结构并准备了将要使用的值。

函数

结构体

结构体方法
如果你想让一个方法可以被别的报访问的话，你需要把这个方法的第一个字母大写。这是一种约定。

接口和多态

错误处理 - Error 接口

错误处理 - Defer

错误处理 - Panic/Recover
 */

package main

import (
	"fmt"
	"math"
)

// 接口
type shape interface {
	area() float64 // 计算面积
	perimeter() float64 // 计算周长
}

// 长方形
type rect struct {
	width, height float64
}

func (r *rect) area() float64 {
	return r.width * r.height
}

func (r *rect) perimeter() float64 {
	return 2 * (r.width + r.height)
}

// 圆形
type circle struct {
	radius float64
}

func (c *circle) area() float64 {
	return math.Pi * c.radius * c.radius
}

func (c *circle) perimeter() float64 {
	return 2 * math.Pi * c.radius
}

func main() {
	interfaceTest()
}

// 接口的使用
func interfaceTest() {
	r := rect{width: 2.9, height: 4.8}
	c := circle{radius: 4.3}

	s := []shape{&r, &c}
	for _, sh := range s {
		fmt.Println(sh)
		fmt.Println(sh.area())
		fmt.Println(sh.perimeter())
	}
}