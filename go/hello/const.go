/*
常量

常量是一个简单值的标识符，在程序运行时，不会被修改的量
常量中的数据类型只可以是布尔型，数字型和字符型

const identifier [type] = value

编译器可以根据变量的值来推断其类型

常量还可以用作枚举

常量表达式中，函数必须是内置函数，否则编译不过
*/

package main

import "fmt"
import "unsafe"

const(
	d = "abc"
	e = len(d)
	f = unsafe.Sizeof(d)
)

func main() {
	const LENGTH int = 10
	const WIDTH int = 5
	var area int
	const a, b, c = 1, false, "str"
	
	area = LENGTH * WIDTH
	fmt.Printf("面积为： %d", area)
	println()
	println(a, b, c)
	println(e, d, f)
}