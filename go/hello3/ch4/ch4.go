package ch4

import (
	"fmt"
	"io"
	"log"
	"os"
)

/*
编译器会在编译时进行方法集的校验

4.1.1 接口声明

接口字面量类型的声明语法

interface {}

接口命名类型声明
type InterfaceName interface {}

方法声明

函数签名是函数的字面量类型，函数签名不包括函数名，函数声明是带上函数名的函数签名
方法也一样

声明新接口类型的特点
1. 接口的命名一般以 er 结尾
2. 接口定义的内部方法声明不需要 func 引导
3. 在接口定义中，只有方法声明没有方法实现

4.1.2 接口初始化
单纯的声明一个接口变量没有任何意义，接口只有在被初始化为具体的类型时才有意义
接口绑定具体类型的实例的过程称为接口初始化。接口变量支持两种直接初始化方法
实例赋值接口
具体类型实例的方法集是某个接口的方法集的超集
接口变量赋值接口变量

4.1.3 接口方法调用

4.1.4 接口的动态类型和静态类型
动态类型
接口绑定的具体实例的类型称为接口的动态类型。接口可以绑定不同类型的实例，所以接口
的动态类型是随着其绑定的不同类型实例而发生变化的

静态类型
接口被定义时，其类型就已经被确定，这个类型叫接口的静态类型。接口的静态类型在其定义
时就被确定，静态类型的本质特征就是接口的方法签名集合。

Go 编译器校验接口是否能赋值，是比较二者的方法集

4.2.1 类型断言
i.(TypeName)
i 必须是接口变量，如果是具体类型变量，编译器会报错，TypeName 可以是接口类型名，
也可以是具体类型名

1. 如果 TypeName 是一个具体类型名，则类型断言用于判断接口变量 i 绑定的实例类型
是否就是具体类型 TypeName
2. 如果 TypeName 是一个接口类型名，则类型断言用于判断接口变量 i 绑定的实例类型
是否同时实现了 TypeName 接口

接口断言的两种语法表现：
o := i.(TypeName)
1. TypeName 是具体类型名，此时如果接口 i 绑定的实例类型就是具体类型 TypeName,
则变量 o 的类型就是 TypeName，变量 o 的值就是接口绑定的实例值的副本
2. TypeName 是接口类型名，如果接口 i 绑定的实例类型满足接口类型 TypeName，
则变量 o 的类型就是接口类型 TypeName，o 底层绑定的具体类型实例就是 i 绑定的
实例的副本

comma, ok
前两点一致
2. 如果上述两个不满足，则 ok 为 false，变量 o 是 TypeName 类型的零值
 */

type R interface {
	Read(p []byte) (n int, err error)
}

type W interface {
	Writer(p []byte) (n int, err error)
}

type RW interface {
	// R 嵌入接口类型匿名字段
	R
	W
}

type Inter interface {
	Ping()
	Pang()
}

type Anter interface {
	Inter
	String()
}

type St struct {
	Name string
}

func (St) Ping() {
	println("ping")
}

func (*St) Pang() {
	println("pang")
}

func test() {
	st := &St{"andes"}
	var i interface{} = st

	// 判断 i 绑定的实例是否实现了接口类型 Inter
	o := i.(Inter)
	o.Ping()
	o.Pang()

	// 如下语句会引发 panic，因为 i 没有实现接口 Anter
	//p := i.(Anter)
	//p.String()
	if p, ok := i.(Anter);  ok {
		// i 没有实现接口 Anter，所以程序不会执行到这里
		p.String()
	}

	// 判断 i 绑定的实例是否就是具体类型 St
	s := i.(*St)
	fmt.Printf("%s", s.Name)
}

/*
类型查询

switch v := i.(type) {
case type1:
	xxx
case type2:
	xxx
default:
	xxx
}

语义分析：
1. 查询一个接口变量底层绑定的底层变量的具体类型是什么
2. 查询接口变量绑定的底层变量是否还实现了其他接口

a. i 必须是接口类型
具体类型实例的类型是静态的，在类型声明后就不再变化，所以具体类型的变量不存在
类型查询，类型查询一定是对一个接口变量进行操作
b. case 后面也可以跟非接口类型名，也可以跟接口类型名，匹配是按照 case 子句
的顺序进行的

推荐使用
switch v := i.(type) {
}
 */
func test02() {
	//var i io.Reader
	//switch v := i.(type) {
	//case nil:
	//	fmt.Printf("%T\n", v)
	//default:
	//	fmt.Printf("default")
	//}

	f, err := os.OpenFile("notes.txt", os.O_RDWR|os.O_CREATE, 0755)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()

	var i io.Reader = f
	switch v := i.(type) {
	case io.ReadWriter:
		// i 的绑定的实例是 *os.File 类型，实现了 io.ReadWriter 接口，匹配成功
		v.Write([]byte("io.ReadWriter\n"))
	case *os.File:
		// 上一个匹配成功，就算这个匹配，也不会走
		v.Write([]byte("*os.File\n"))
		v.Sync()
	default:
		return

	}
}
