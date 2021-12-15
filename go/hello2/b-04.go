/*
func xxx() {} 这种被称为 Go 语言中的函数

指定接受者的函数被称为方法，专属于 user 的方法
func (u user) SaveUser(name string, age int) (err error) {}

// 无参数，无返回值
func fun() {
}

// 无参数，又返回值，并且给返回值去了名字叫 ret
func fun()(ret Type) {
}

// 有参数，有返回值，并且没有给返回值取名字
func sum(a int, b int) int{
	return a +ｂ
}

// 连续参数类型相同时，可以简写
func sum(a, b int) {
}

// 有参数，有返回值，切给返回值取了名字
func sum(a int, b int) (ret int) {
	ret = a + b
	return
}

// 有多个返回值
func fun(int, int) {
	return 1, 2
}

// 使用
_,v := fun()

// 可变长度的函数，b 可以接受多个 int，也能接受 int 类型的切片
func fun(a int, b... int) {
}

函数的作用域

{} 限制作用域，局部找不到就找全局的

%T 查看函数的返回值类型

类型: func()
类型: func() int

匿名函数

// 声明匿名函数
// 但是一般我们都是在函数的内部声明匿名函数的

var f = func() {
}

内置函数
当出现 panic 时，程序会崩溃，通过 recover 尝试恢复错误

在 Go 中没有 try catch 异常处理机制

在 Go 的设计哲学中，对待每一种错误都当作是一种具体的值，具体处理，会比较多

revover 必须搭配 defer 使用
defer + recover 必须出现在 panic 前面执行才有意义

defer 语句

Go 语言中的 defer 语句会将其后面跟随的语句进行延迟处理
先被 defer 修饰的函数最后被执行，最后被 defer 修饰的函数先执行
一个函数中可以存在多个 defer
defer 多用于释放资源，文件句柄，数据库连接，socket 连接

defer 的执行时机

Go 的底层，return语句并不是原子操作，分成复制和执行 RET 命令两步

返回值 = x
defer
RET 指令


defer 修饰匿名函数 --- 后面再看看

闭包

闭包大概率是一个函数（多数为匿名函数），这个函数可以起到适配的作用
根据 Go 的作用域规定，内层函数能访问到外岑函数的变量
所以： 闭包 = 函数 + 外部变量的引用

底层的原理就是：
1. 函数可以作为返回值
2. 函数中变量的查找顺序：先在自己的作用域中查找，再从全局范围内查找
*/
package main

import "fmt"

func a() {
	fmt.Println("a")
}

func b() {
	defer func() {
		// revcover 不推荐使用
		err := recover()
		if (err != nil) {
			fmt.Println("尝试恢复错误")
		}
	}()
	panic("error panic \n")
}

func main() {
	a()
	b()
}

/*
1 5 4 3 2
*/
func test() {
	fmt.Println("1")
	defer fmt.Println("2")
	defer fmt.Println("3")
	defer fmt.Println("4")
	fmt.Println("5")
}

func f1(f2 func()) {
	fmt.Println("this is f1 will call f2")
	f2()
	fmt.Println("this is f1 finished call f2")
}

func f2(x int, y int) {
	fmt.Println("this is f2 start")
	fmt.Printf("x: %d y: %d \n", x, y)
	fmt.Println("this is f2 end")
}

func f3(f func(int, int),x, y int) func() {
	fun := func() {
		f(x, y)
	}
	return fun
}

func test02() {
	f1(f3(f2, 6, 6))
}

func cacl(base int) (func(int) int, func(int) int) {
	add := func(i int) int {
		base += i
		return base
	}
	
	sub := func(i int) int {
		base -= i
		return base
	}
	return add, sub
}

func test03() {
	add, sub := cacl(10)
	
	fmt.Println(add(1)) // 11
	fmt.Println(sub(2)) // 9 , 之所以是9, 是因为他先执行的add 将base修改成了11
}