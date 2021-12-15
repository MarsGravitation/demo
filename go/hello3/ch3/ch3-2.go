package main

import "fmt"

/*
Go 语言的方法非常纯粹，可以看作特殊类型的函数，其显式地将对象实例或指针作为函数
的第一个参数，并且参数名可以自己指定。这个对象实例或指针称为方法的接受者

为命名类型定义方法

类型方法接受者是值类型
func (t TypeName) methodName (paramList) (ReturnList) {
}

类型方法接受者是指针
func (t *TypeName) methodName (paramList) (ReturnList) {
}
*/

type SliceInt []int

func (s SliceInt) Sum() int {
	sum := 0
	for _, i := range s {
		sum += i
	}
	return sum
}

/*
类型方法的特点：
1. 可以为命名类型增加方法（除了接口），非命名类型不能自定义方法
[]int 不能增加方法，因为它是非命名类型。命名接口类型本身就是一个方法的签名集合，
所以不能为其增加具体的实现方法
2. 方法的定义必须和类型的定义在同一个包中
3. 可见性和变量一样
4. 使用 type 定义的自定义类型是一个新类型，新类型不能调用原油类型的方法，但是底层
类型支持的元素可以被新类型继承

3.3 方法调用

3.3.1 一般调用

TypeInstanceName.MethodName(ParamList)

TypeInstanceName: 类型实例名或指向实例的指针变量名
 */

type T struct {
	a int
}

func (t T) Get() int {
	return t.a
}

func (t *T) Set(i int)  {
	t.a = i
}

func test21()  {
	var t = &T{}

	t.Set(2)
	t.Get()
}

/*
3.3.2 方法值

变量 x 的静态类型是 T，M 是类型 T 的一个方法，x.T 被成为方法值。
x.T 是一个函数类型变量，可以赋值给其他变量，并像普通的函数名一样使用。

f := x.M
f(args...)
===>
x.M(args)

 */

func test22() {
	var t = &T{}

	f := t.Set
	// 方法值调用
	f(2)
	f(3)
}

/*
3.3.3 方法表达式

方法表达式相当于提供一种语法将类型方法调用显示地转换为函数调用，接受者必须显式地传递
进去

T.Get (*T).Set被称为方法表达式，方法表达式可以看作函数名，只不过这个函数的首个
参数是接受者的实例或指针。T.Get 的函数签名是 func(t T) int, (*T).Set 的函数
签名是 func(t *T, i int).注意：这里的 T.Get 不能写成 (*T).Get, (*T).Set
也不能写成 T.Set，在方法表达式中编译器不会做自动转换。
 */

func test23() {
	t := T{a:1}

	// 普通方法调用
	t.Get()

	// 方法表达式调用
	T.Get(t)
}

/*
方法集

3.3.4 方法集

命名类型方法接受者有两种类型，一个是值类型，另一个是指针类型，这个和函数是一样的

接受者 Int 类型的方法集合

func (i Int) Print()
func (a Int) Max(b Int) Int

接受者是 *Int 类型的方法集合
func (i *Int) Set(a Int)

类型的方法集：
T 类型的方法集是 S
*T 类型的方法集是 S 和 *S

在直接使用类型实例调用类型的方法时，无论值类型还是指针类型变量，都可以调用类型的
所有方法，原因是编译器在编译期间能够识别出这种调用关系，做了自动的转换
 */

type Int int

func (a Int) Max(b Int) Int {
	if a >= b {
		return a
	} else {
		return b
	}
}

func (i *Int) Set(a Int) {
	*i = a
}

func (i Int) Print() {
	fmt.Printf("value=%d\n", i)
}

func test24() {
	var a Int = 10
	var b Int = 20

	c := a.Max(b)
	c.Print()
	(&c).Print()

	a.Set(20) // 编译器转换为 (&a).Set(20)
	a.Print()
}

/*
3.3.5 值调用和表达式调用的方法集

1. 通过类型字面量显式地进行值调用和表达式调用，编译器不会做自动转换
 */

type Data struct {

}

func (Data) TestValue() {

}

func (*Data) TestPointer() {

}

func test25() {

	// 显式调用
	(*Data)(&struct{}{}).TestValue()
	(*Data)(&struct{}{}).TestValue()

	// method value
	(Data)(struct{}{}).TestValue()
	// method expression
	Data.TestValue(struct{}{})

	Data.TestPointer(struct{}{})
	// (Data)(struct{}{}).TestPointer()
}

/*
函数类型分为两种，一种是函数字面量类型（未命名类型），另一种是函数命名类型

函数字面量类型
语法表达格式是 func(InputTypeList) OutputTypeList，可以看出有名函数和匿名
函数都属于函数字面量类型。有名函数的定义相当于初始化一个字面量类型后将其赋值给一个
函数名变量；匿名函数的定义也是直接初始化一个函数字面量类型，只是没有绑定到一个具体
变量上。从 Go 类型系统的角度来看，有名函数和匿名函数都是函数字面量类型的实例。

函数签名
函数签名就是字面量类型

函数声明
函数声明 = 函数名 + 函数签名

函数签名
func (InputTypeList) OutputTypeList

函数声明
func FuncName(InputTypeList) OutputTypeList
 */

func add(a, b int) int {
	return a + b
}

// add 函数的签名，实际上就是 add 的字面量类型
// func (int, int) int

func main() {

}
