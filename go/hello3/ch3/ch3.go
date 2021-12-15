package main

import (
	"fmt"
)

/*
Go 语言的类型系统可以分为命名类型、非命名类型、底层类型、动态类型和静态类型等

3.1.1 命名类型和未命名类型

命名类型
类型可以通过标识符来表示，这种类型称为命名类型。Go 语言的基本类型中有 20 个预声
明简单类型都是命名类型，用户自定义类型也是命名类型

未命名类型
一个类型由预声明类型、关键字和操作符组合而成，这个类型称为未命名类型。未命名类
型又称为类型字面量
Go 语言的基本类型中的复合类型；array，slice，map，channel，pointer，function，
struct，interface 都属于类型字面量，也都是未命名类型

*int, []int, [2]int, map[k]v 都是未命名类型

命名类型：
1. 预声明类型（简单类型）：bool、int...
2. 自定义类型：type new_type old_type

未命名类型：
1. array、slice...（又叫类型字面量）
复合类型、类型字面量、未命名类型都是同一个概念

3.1.2 底层类型
1. 预声明类型、类型字面量的底层类型是它们自身
2. 自定义类型中的 newtype 的底层类型是逐层递归向下查找的，直到查到的 oldtype
是预声明类型或类型字面量为止

3.1.3 类型相同和类型赋值
1. 两个命名类型相同的条件是两个类型声明的语句完全相同
2. 命名类型和未命名类型永远不相同
3. 两个未命名类型相同的条件是它们的类型声明字面量的结构相同，且内部元素的类型相同
4. 通过类型别名语句声明的两个类型相同

类型别名语法 type T1 = T2

 */

// Person 使用 type 声明的都是命名类型
type Person struct {
	name string
	age int
}

func test()  {
	// 使用 struct 字面量声明的是未命名类型
	a := struct {
		name string
		age int
	}{"andes", 18}
	fmt.Printf("%T\n", a) // struct {name string; age int}
	fmt.Printf("%v\n", a) // {andes 18}

	b := Person{"tom", 21}
	fmt.Printf("%T\n", b) // main.Person
	fmt.Printf("%v\n", b) // {tom 21}
}

type Map map[string]string

func (m Map) print() {
	for _, key := range m {
		fmt.Println(key)
	}
}

type iMap Map

// 只要底层类型是 slice、map 等支持 range 的类型字面量，新类型仍然可以使用 range 迭代

func (m iMap) Print() {
	for _, key := range m {
		fmt.Println(key)
	}
}

type slice []int
func (s slice) Print() {
	for _, v := range s {
		fmt.Println(v)
	}
}

func test02()  {
	mp := make(map[string]string, 10)
	mp["hi"] = "tata"

	// mp 与 ma 有相同的底层类型 map[string]string，并且 mp 是未命名类型
	// 所以 mp 可以直接赋值给 ma
	var ma Map = mp

	// im 与 ma 虽然有相同的底层类型，但是它们没有一个是未命名类型
	// 编译报错
	//var im iMap = ma
	// 强制进行类型转换
	var im iMap = (iMap) (ma)
	ma.print()
	im.Print()

	// Map 实现了 Print()，所以其可以赋值给接口类型变量
	var i interface{
		print()
	} = ma

	i.print()

	s1 := []int{1, 2, 3}
	var s2 slice
	s2 = s1
	s2.Print()
}

/*
3.1.4 类型强制转换

var a T = (T)(b)

注意：
1. 数值类型和 string 类型之间的想换换换可能造成值部分丢失；其他的转换仅是类型的
转换，不会造成值的改变。string 和数字之间的转换可使用标准库 strconv
2. Go 语言没有语言机制支持指针和 integer 之间的直接转换，可以使用标准库中的 unsafe
包进行处理
 */
func test03()  {
	// 字符串和字节切片之间的转换
	s := "hello,世界!"
	var a []byte
	a = []byte(s)

	var b string
	b = string(a)

	var c []rune
	c = []rune(s)
	fmt.Printf("%T\n", a) // []unit8 byte 是 int8 的别名
	fmt.Printf("%T\n", b) // string
	fmt.Printf("%T\n", c) // int32 rune 是 int32 的别名
}

/*
INT
3.2.1 自定义类型

type newtype oldtype
newtype 继承了底层类型的操作集合（比如 range 迭代访问）
除此之外，newtype 和 oldtype 是两个完全不同的类型，newtype 不会继承 oldetype
的方法。无论 oldtype 是什么类型，使用 type 声明的新类型都是一种命名类型，也就是说，
自定义类型都是命名类型
 */
type INT int // INT 是一个使用预声明类型的自定义类型
type Map1 map[string]string // Map1 是一个使用类型字面量声明的自定义类型
type myMap Map1 // myMap 是一个自定义类型 Map1 声明的自定义类型
// INT、Map1、myMap 都是命名类型

/*
使用 type 自定义的结构类型属于命名类型
struct xxx struct {}

errorString 是一个自定义结构类型，也是命名类型
type errorString struct {}

结构字面量属于未命名类型
struct {}

struct{} 是未命名类型空结构
var s = struct{}{}

struct 初始化
1. 按照字段顺序进行初始化
a := Person{"andes", 18}
不推荐
2. 指定字段名进行初始化
a := Person{name:"andes", age: 18}
推荐
3. 使用 new 创建内置函数，字段默认初始化为其类型的零值，返回值是指向结构的指针
p := new(Person)
name = "", age = 0
不常用，一般使用 struct 都不会将所有字段初始化为零值
4. 一次初始化一个字段
p := Person{}
p.name = "andes"
p.age = 18
不常用
5. 使用构造函数进行初始化


匿名字段
在定义 struct 时，如果字段只给出字段类型，没有给出字段名，则称这样的字段为
匿名字段。被匿名嵌入的字段必须是命名类型或命名类型的指针，类型字面量不能作为匿名
字段使用。匿名字段的字段名默认就是类型名，如果匿名字段是指针类型，则默认的字段名就
是指针指向的类型名。但一个结构体里面不能同时存在某一类型及其指针的匿名字段，原因是
二者的字段名相同。如果嵌入的字段来自其他包，则需要加上包名，并且必须是其他包可以导出
的类型

type File struct {
	*file
}

自定义接口类型
接口字面量是未命名类型，但自定义接口类型是命名类型

自定义接口类型，属于命名类型
type Reader interface {
	Read(p []byte) (n int, err error)
}
 */
// interface{} 是接口字面量类型标识，所以 i 是未命名类型变量
var i interface{}

func main() {

}
