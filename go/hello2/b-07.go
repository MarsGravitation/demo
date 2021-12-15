/*
在 Go 中，接口也是一种类型，一种抽象的类型
接口中一般会定义一组方法，用来拓展某个对象的能力
Go 语言中的接口是一些方法的集合，它指定了对象的行为

type 接口类型名 interface {
	方法名1(参数列表1) 返回值列表1
}

一般接口名的命名规范：在末尾 + er
当接口名和方法名的首字母都大写时，这个发那个发可以被接口所在包之外的包访问
参数列表，返回值列表中的变量名同样可以省略不写

只要结构体实现了接口的所有方法，就算做实现了接口

接口可以使用父类引用指向子类对象

type Animal interface {
	say()
}

type Cat struct {}
type Dog struct {}

func (c Cat) say() {
	fmt.Println("喵~~~")
}

func (d Dog) say() {
	fmt.Println("汪~~~")
}


值接收者&指针接收者

Go 会将指针 p 处理成 *p，再去求值

var a Animal
a = &Dog {}
a.say() // 自动处理成 *p

空接口

在 Go 语言中，所有其他数据类型都实现了空接口
如果我们想让入参可以接受任意类型的变量，那么我们可以用空接口来实现

空接口作为 map 的值
var studentInfo = make(map[string]interface{})
*/