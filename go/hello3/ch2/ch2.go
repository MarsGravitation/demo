package main

import (
	"fmt"
	"io"
	"os"
)

/*
2.1.1 函数定义

func funcName(param-list) (result-list) {
	function-body
}

2.1.3 实参到形参的传递
Go 函数实参到形参的传递永远是值拷贝，又是函数调用后实参指向的值发生了变化，那是
因为参数传递的是指针值的拷贝，实参是一个指针变量，传递给形参的是这个指针变量的副本，
二者指向同一地址，本质上参数传递仍然是值拷贝
 */

func chvalue(a int) int {
	a = a + 1
	return a
}

func chpointer(a *int) {
	*a = *a + 1
	return
}

func test()  {
	a := 10
	chvalue(a) // 实参传递给形参是值拷贝
	fmt.Println(a)

	chpointer(&a) // 实参传递给形参仍然是值拷贝，只不过复制的是 a 的地址值
	fmt.Println(a)
}

/*
2.2.1 函数签名
函数类型又叫函数签名，一个函数的类型就是函数定义首行去掉函数名、参数名和 {

两个函数类型相同的条件是：拥有相同的形参列表和返回值列表（列表元素的次序、个数
和类型都相同）

add sub 函数类型完全一样
 */
func add(a, b int) int {
	return a + b
}

func sub(x int, y int) (c int) {
	c = x - y
	return
}

type Op func(int, int) int // 定义一个函数类型，输入的是两个 int，返回值是一个 int

/*
可以使用 type 定义函数类型，函数类型变量可以作为函数的参数或返回值

do 第一个参数是函数类型 Op，函数类型变量可以直接用来进行函数调用
*/
func do(f Op,a, b int) int {
	return f(a, b)
}

/*
函数类型和 map、slice、chan 一样，实际函数类型变量和函数名都可以当作指针变量，该
指针指向函数代码的开始位置。通常说函数类型变量是一种引用类型，未初始化的函数类型的
变量默认值是 nil

Go 中函数是第一公民。有名函数的函数名可以看作函数类型的常量，可以直接使用函数名调用函数，
也可以直接赋值给函数类型变量
 */
func test02() {
	fmt.Printf("%T\n", add) // func(int, int) int

	// 函数名 add 可以当作相同函数类型形参，不需要强制类型转换
	do(add, 1, 2)
}

/*
2.2.2 匿名函数
匿名函数可以看作函数字面量，所有直接使用函数类型变量的地方都可以由匿名函数代替。
匿名函数可以直接赋值给函数变量，可以当作实参，也可以作为返回值，还可以直接被调用
 */

// 匿名函数直接赋值函数变量
var sum = func(a, b int) int {
	return a + b
}

/*
2.3 defer
defer 可以注册多个延迟调用，这些调用以先进后出的顺序在函数返回前被执行。有点类似
java 的 finally 子句。defer 常用于保证一些资源最终一定能够得到回收和释放

defer 后面必须是函数或方法的调用，不能是语句
 */
func test03() {
	// 先进后出
	defer func() {
		println("first")
	}()

	defer func() {
		println("second")
	}()
	println("function body")

	/*
	function body
	second
	first
	 */
}

/*
defer 函数的实参在注册时通过值拷贝传递进去
后续语句 a++ 并不会影响 defer 语句最后的输出结果
 */
func test04() int {
	a := 0
	defer func(i int) {
		println("defer i = ", i)
	}(a)

	a++
	return a
	/*
	defer i = 0
	 */
}

/*
defer 语句必须先注册后才能执行，如果 defer 位于 return 之后，则 defer 因为
没有注册，不会执行
 */
func test05() {
	defer func() {
		println("first")
	}()

	a := 0
	println(a)
	return

	defer func() {
		println("second")
	}()
	/*
	0
	first
	 */

}

/*
defer 的好处是可以在一定程度上避免资源泄漏，特别是在很多 return 语句，有多个资源
需要关闭的场景中，很容易漏掉资源的关闭操作

打开资源无报错后直接调用 defer 关闭资源

defer 语句的位置不当，有可能导致 panic，一般 defer 语句放在错误检查语句之后
defer 副作用：defer 会推迟资源的释放，defer 尽量不要放到循环语句里面，将大函数
内部的 defer 语句单独拆分成一个小函数是一种很好的实践方式
 */
func test06() {
	src, err := os.Open("")
	if err != nil {
		return
	}
	defer src.Close()

	dst, err := os.Create("")
	if err != nil {
		return
	}
	defer dst.Close()

	w, err := io.Copy(dst, src)
	println(w)
	return

}

/*
2.5.1
	panic(i interface{})
	recover() interface{}

	引发 panic 有两种情况，一种是程序主动调用 panic 函数，另一种是程序产生运行时错误，
由运行时检测并抛出。
	发生 panic 后，程序会从调用 panic 的函数位置或发生 panic 的地方立即返回，逐层向上执
行函数的 defer 语句，然后逐层打印函数调用堆栈，直到被 recover 捕获或运行到最外层函数而
退出。
	panic 不但可以在函数正常流程中抛出，在 defer 逻辑里也可以再次调用 panic 或抛出 panic。
defer 里面的 panic 能够被后续执行的 defer 捕获。
	recover 用来捕获 panic，阻止 panic 继续向上传递。recover() 和 defer 一起使用，但是 recover
只有在 defer 后面的函数体内被直接调用才能捕获 panic 终止异常，否则返回 nil，异常继续向外传递。

	defer recover() // 捕获失败
	defer func() {
		recover() // 捕获成功
	}()
 */

/*
2.1 函数定义
	函数声明关键字 func、函数名、参数列表、返回列表、函数体
	func funcName(param-list) (result-list) {}

2.1.3 实参到形参的传递
	Go 函数实参到形参的传递永远是值拷贝，有时函数调用后实参指向的值发生了变化，
	那是因为参数传递的是指针值的拷贝，实参是一个指针变量，传递给形参的是这个指针
	变量的副本，二者指向同一地址，本质上参数传递仍然是值拷贝

2.1.4 不定参数
	a. 所有的不定参数类型必须是相同的
	b. 不定参数必须是函数的最后一个参数
	c. 不定参数名在函数体内相当于切片，对切片的操作同样适合对不定参数的操作
	d. 切片可以作为参数传递给不定参数，切片名后要加上 ...
	e. 形参为不定参数的函数和形参为切片的函数类型不相同

2.2.1 函数签名
	函数类型又叫函数签名，一个函数的类型就是函数定义行去掉函数名、参数名和 {
	func(int, int) int
	两个函数类型相同的条件是：拥有相同的形参列表和返回值列表
 */
func main() {

}