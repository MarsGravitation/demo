package main

import "fmt"

/*
声明语句定义了程序的各种实体对象以及部分或全部的属性。
Go 语言主要有四种类型的声明语句：var、const、type、func
分别对应变量、常量、类型和函数实体对象的声明。

一个 Go 语言编写的程序对应一个或多个以 .go 为文件后缀名的源文件。
每个源文件以包的声明语句开始，说明该源文件是属于哪个包。声明语句之后
是 import 语句导入依赖的其他包，然后是包一级的类型、变量、常量、函数
的声明语句，包一级的各种类型的声明语句的顺序无关紧要（函数内部的名字则
必须先声明之后才能使用）
 */

const boilingF = 212.0

/*
常量 boilingF 是在包一级范围声明语句声明的，然后 f 和 c 两个变量是在
main 函数内部声明的声明语句声明的。在包一级声明的名字可在整个包对应的源
文件中访问，而不仅仅在其声明语句所在的源文件中访问。相比之下，局部声明的
名字就只能在函数内部很小的范围被访问。

一个函数的声明由一个函数名字、参数列表（由函数的调用者提供参数变量的具体值）、
一个可选的返回值列表和包含函数定义的函数体组成。如果函数没有返回值，那么返回
值列表是省略的。执行函数从函数的第一个语句开始，依次顺序执行知道遇到 return
返回语句，如果没有返回语句则是执行到函数末尾，然后返回到函数调用者。

 */
func main() {
	var f = boilingF
	var c = (f - 32) * 5 / 9
	fmt.Printf("boiling point = %g or %g\n", f, c)
}
