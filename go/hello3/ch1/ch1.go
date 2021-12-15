package ch1

import (
	"fmt"
	"unsafe"
)

/*
1.6.3 切片

slice 是一种变长数组，其数据结构中有指向数组的指针，所以是一种引用类型。

Go 为切片维护三个元素 --- 指向底层数组的指针、切片元素数量和底层数组的容量
 */
type slice struct {
	array unsafe.Pointer
	len int
	cap int
}

/*
1. 切片的创建
由数组创建

2. 通过内置函数 make 创建切片
注意：由 make 创建的切片各元素被默认初始化为切片元素类型的零值
a := make([]int, 10) // len = 10, cap = 10
b := make([]int, 10, 15) // len = 10, cap = 15

注意：直接声明切片类型变量是没有意义的
var a []int
底层的数据结构
array = nil
len = 0
cap = 0

切片支持的操作
len
cap
append
copy
 */
func test() {
	var array = [...]int{0, 1, 2, 3, 4, 5 ,6}
	s1 := array[0:4]
	s2 := array[:4]
	s3 := array[2:]
	fmt.Printf("%v\n", s1) // 0 1 2 3
	fmt.Printf("%v\n", s2) // 0 1 2 3
	fmt.Printf("%v\n", s3) // 2 3 4 5 6

	a := make([]int, 10) // len = 10, cap = 10
	b := make([]int, 10, 15) // len = 10, cap = 15
	fmt.Printf("%v\n", a) // 0 0 0 0 0 0 0 0 O
	fmt.Printf("%v\n", b) // 0 0 0 0 0 0 0 0 O

	a1 := [...]int{0, 1, 2, 3, 4, 5, 6}
	b1 := make([]int, 2, 4)
	c1 := a1[0:3]

	fmt.Println(len(b1)) // 2
	fmt.Println(cap(b1)) // 4
	b1 = append(b1, 1)
	fmt.Println(b1) // 0 0 1
	fmt.Println(len(b1)) // 3
	fmt.Println(cap(b1)) // 4

	b1 = append(b1, c1...)
	fmt.Println(b1) // 0 0 1 0 1 2
	fmt.Println(len(b1)) // 6
	fmt.Println(cap(b1)) // 8，底层数组发生了扩展

	d1 := make([]int, 2 ,2)
	copy(d1, c1) // copy 只会赋值 d 和 c 中长度最小的
	fmt.Println(d1) // 0 1
	fmt.Println(len(d1))
	fmt.Println(cap(d1))

	// 字符串和切片到的相互转换
	str := "hello,世界!" // 通过字符串字面量初始化一个字符串 str
	a2 := []byte(str) // 将字符串转换为 []byte 类型切片
	b2 := []rune(str) // 将字符串转换为 []rune 类型切片
	fmt.Println(a2, b2)
}

/*
1.6.4 map

 */

/*
Person
1.6.5　struct

a. struct 类型字面量
struct {}

b. 自定义 struct 类型
type TypeName struct {}

 */
type Person struct {
	Name string
	Age int
}

type Student struct {
	*Person
	Number int
}

func test02() {
	// 按照类型声明顺序，逐个赋值
	// 不推荐这种初始化方式，一旦 struct 增加字段，则整个初始化语句会报错
	a := Person{"Tom", 21}

	// 推荐这种使用 Field 名字的初始化方式，没有指定的字段则默认初始化为类型的零值
	p := &Person{
		Name: "tata",
		Age: 12,
	}

	s:= Student{
		Person: p,
		Number: 110,
	}

	fmt.Println(a, s)
}

// 2021/12/3
/*
1.4.1 变量
	变量：使用一个名称来绑定一块内存地址，该内存地址中存放的数据类型由定义变量时指定
的类型决定，该内存地址里面存放的内容可以改变。
	1. 显示的完整声明
	var varName dataType [ = value]
	- 如果不指定初始值，则 Go 默认将该变量初始化为类型的零值
	- Go 的变量声明后就会立即为其分配空间

	2. 短类型声明
	varName := value
	- := 声明只能出现在函数内（包括方法内）
	- 此时 Go 编译器自动进行数据类型推断
	Go 支持多个类型变量同时声明并赋值
	a, b := 1, "hello"

	可见性和作用域
	Go 内部使用统一的命名空间对变量进行管理，每一个变量都有一个唯一的名字，包名是这个
	名字的前缀

1.5.5 字符串
	- 字符串是常来那个，而可以通过类似数组的索引访问其字节单元，但是不能修改某个字节的值
	var a = "hello, world"
	b := a[0]
	a[1] = 'a' // error
	- 字符串转换为切片 []bytes(s) 要慎用，尤其是当数据量较大时（每转换一次都需要复制内容）
	a := "hello, world"
	b := []byte(a)
	- 字符串尾部不包含 NULL 字符
	- 字符串类型底层实现是一个二元的数据结构，一个是指针指向字节数组的起点，另一个是长度。
	type stringStruct struct {
		str unsafe.Pointer // 指向底层字节数组的指针
		len int // 字节数组长度
	}
	- 基于字符串创建的切片和原字符串指向相同的底层字符串数组，一样不能修改，对字符
	串的切片操作返回的子串仍然是 string，而非 slice
	a := "hello, world!"
	b := a[0:4]
	- 字符串和切片的转换：字符串可以转换成字节数组，也可以转换成 Unicode 的数组
	b := []byte(a)
	c := []rune(a)
	- 字符串的运算
	c := a + b // 字符串拼接
	len(a) // 内置的 len 函数获取字符串长度

1.5.6 rune 类型
	Go 内置两种字符类型：一种是 byte 的字节类类型（byte 是 uint 的别名），另一种
	是表示 Unicode 编码的字符 rune。rune 在 Go 内部是 int32 类型的别名，占用 4
	个字节。Go 语言默认的字符编码是 UTF-8。

	* pointerType // * 类型名
	[n] elementType // [n] 数组元素类型
	[] elementType // [] 切片元素类型
	map [keyType]valueType // map[键类型]值类型
	chan valueType // chan 通道元素类型

	struct {} // 结构类型使用 struct{}
	interface {} // 接口类型使用 interface{}

1.6.1 指针
	*T // 指针声明
	&t // 变量名前加 & 来获取变量的地址
	- 在赋值语句中，*T 出现在 = 左边表示指针声明，*T 出现在 = 右边表示取指针
	指向的值
	var a = 11
	p := &T
	- 结构体指针方法结构体仍然使用 . 操作符
	p := &andes
	p.name
	- Go 不支持指针的运算
	- 函数中允许返回局部变量的地址

1.6.2 数组
	数组的类型名是 [n]elementType，其中 n 是数组长度，elementType 是数组元素类型。
	数组一般在创建时通过字面量初始化。
	array := [...]float64{7.0}

	数组初始化
	a := [3]int{1, 2, 3} // 指定长度和初始化字面来那个
	a := [...]int{1, 2, 3} // 不指定长度，但是由后面的初始化列表数量来确定长度
	a := [3]int{1:1, 2:2} // 指定总长度，并通过索引值进行初始化，没有初始化元素使用类型默认值
	a := [...]int{1:1, 2:2} // 不指定总长度，通过索引值进行初始化，数组长度由最后一个索引值确定

	数组的特点
	a. 数组创建完长度就固定了，不可以再追加元素
	b. 数组是值类型的，数组赋值或者作为函数参数都是值拷贝
	c. 数组长度是数组类型的组成部分。[10]int 和 [20]int 表示不同的类型
	d. 可以根据数组创建切片

1.6.3 切片
	type slice struct {
		array unsafe.Pointer
		len int
		cap int
	}
	三个元素 --- 指向底层数组的指针、切片的元素数量和底层数组的容量

	切片的创建
	a. 由数组创建
	array[b:e]
	b. make，由 make 创建的切片各元素被默认初始化为切片元素类型的零值
	a := make([]int, 10, 15)
	array=nil
	len=0
	cap=0

	切片的操作
	len(), cap(), append(), copy()

	append() --- 底层数组可能发生扩展

	字符串和切片的相互转换
	a := []byte(str) // 字符串转为 []byte
	b := []rune(str) // 字符串转为 []rune

1.6.4 map
	a. map 创建
	- 使用字面量创建
	ma := map[string]int{"a":1}

	- 使用内置的 make 函数创建
	make(map[int]string, 10)

	- map 支持的操作
	mapName[key] 放在 = 左边是更新，放在 = 右边是访问
	range 遍历 map，但是不保证每次迭代元素的顺序
	delete(mapName, key)
	len() 返回 map 中的键值对数量

	注意：
	map 不是并发安全的
	不要直接修改 map value 内某个元素的值，如果像修改 map 的某个键值，则必须整体赋值

1.6.5 struct
	struct 结构中的类型可以是任意类型；struct 的存储空间时连续的，其字段按照声明
	时的顺序存放
	struct 有两种形式：一种是 struct 类型字面量，另一种是使用 type 声明的自定义 struct 类型

	a. struct 类型字面量
	struct {}
	b. 自定义 struct 类型
	type TypeName struct{}
	使用 struct 字面量的场景不多，更多的时候是通过 type 自定义一个新的类型来实现的。
	type 是自定义类型的关键字，不但支持 struct 类型的创建，还支持任意其他自定义类型的创建
	c. struct 类型变量的初始化
	a := Person{"Tom", 21} // 按照类型声明顺序，逐个赋值，不推荐
	// 推荐这种使用 Field 名字的初始化方式，没有指定的字段则默认初始化为类型的零值
	p := &Person {
		Name: "tata",
		Age: 12,
	}

1.7.4 标签和跳转
	goto 语句用于函数的内部的跳转，需要配合标签一起使用
	goto Lable：跳转到标签名后的语句处执行，特点：
	- 只能在函数内跳转
	- 不能跳过内部变量声明语句，这些变量在 goto 语句的标签语句处又是可见的
	- 只能跳转到同级作用域或者上层作用域内，不能跳转到内部作用域内

	break 用于函数内跳出 for、switch、select
	- 单独使用，用于跳出 break 当前所在的 for、switch、select
	- 和标签一起使用，用于跳出标签所标识的 for、switch、select 语句的执行，可用于跳出多重
	循环，但标签和 break 必须在同一个函数内。

	L1:
	for {
		for {
			break L1 // 跳出 L1 标签所在的 for 循环
			if {
				break // 默认仅跳出离 break 最近的内层循环
			}
		}
	}

	continue 用于跳出 for 循环的本次迭代，调到 for 循环的下次迭代的 post 语句处执行
	- 单独使用，用于跳出 continue 当前所在的 for 循环的本次迭代
	- 和标签一起使用，用于跳出标签所表示的 for 语句的本次迭代，但标签和 continue 必须
	在同一个函数内。

	L1:
	for {
		for {
			continue L1 // 跳出 L1 标签所在的 for 循环 i++ 处执行
			if {
				continue // 默认仅跳出离 break 最近的内层循环 j++ 处执行
			}
		}
	}
 */
