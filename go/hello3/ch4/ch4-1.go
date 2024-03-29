package ch4

/*
4. 接口
	接口是一个编程规约，也是一组方法签名的集合。Go 的接口是非侵入式的设计，也就是说，
一个具体类型实现接口不需要在语法上显示地声明，只要具体类型的方法集是接口方法集的超集，
就表示该类型实现了接口，编译器在编译时会进行方法集的校验。接口是没有具体实现逻辑的，也
不能定义字段

4.1.1 接口声明
	接口字面量类型
	interface {}

	接口命名类型使用 type 关键字声明
	type InterfaceName interface {}

	接口定义大括号内可以是方法声明的集合，也可以嵌入另一个接口类型匿名字段

	方法声明 = 方法名 + 方法签名

	声明新接口类型的特点
	1. 接口的命名一般以 er 结尾
	2. 接口定义的内部方法声明不需要 fun 引导
	3. 只有方法声明没有方法实现

4.1.2 接口初始化
	实例赋值接口
	如果具体类型实例的方法集是某个接口的方法集的超集，则称该具体类型实现了接口，可
以将该具体类型的实例直接赋值给接口类型的变量，此时编译器会进行静态的类型检查。接口
被初始化后，调用接口的方法就相当于调用接口绑定的具体类型的方法，这就是接口调用的语义

	接口变量赋值接口变量

4.1.3 接口方法调用
	接口方法调用的最终地址是在运行期间决定的，将具体类型变量赋值给接口后，会使用具体
类型的方法指针初始化接口变量，当调用接口变量的方法时，实际上是间接地调用实例的方法。

4.1.4 接口的动态类型和静态类型
	动态类型
	接口绑定的具体实例的类型称为接口的动态类型。接口可以绑定不同类型的实例，所以接口
的动态类型是随着其绑定的不同实例而发生变化的

	静态类型
	接口被定义时，其类型就已经被确定，这个类型叫接口的静态类型。接口的静态类型在其
定义是就被确定，静态类型的本质特征就是接口的方法签名集合。Go 编译器校验接口是否能赋值，
是比较二者的方法集

4.2 接口运算

4.2.1 类型断言
	i.(TypeName) --- i 必须是接口变量
	1. 如果 TypeName 是一个具体类型名，则类型断言用于判断接口变量 i 绑定的实例类
行是否就是具体类型 TypeName
	2. 如果 TypeName 是一个接口类型名，则类型断言用于判断接口变量 i 绑定的实例类型
是否同时实现了 TypeName 接口

	o := i.(TypeName)
	1. TypeName 是具体类型名，此时如果接口 i 绑定的实例类型就是具体类型 TypeName，
则变量 o 的类型就是 TypeName，变量 o 的值就是接口绑定的实例值的副本
	2. TypeName 是接口类型名，如果接口 i 绑定的实例类型满足接口类型 TypeName，则变量
o 的类型就是接口类型 TypeName，o 底层绑定的具体类型实例是 i 绑定的实例的副本

	if o, ok := i.(TypeName); ok {} // 直接使用 i.(TypeName) 可能导致 panic

4.2.2 类型查询
	switch v := i.(type) {}
	接口查询有两层语义，一是查询一个接口变量底层绑定的底层变量的具体类型是什么，二
是查询接口变量绑定的底层变量是否还实现了其他接口
	1. i 必须是接口类型
	具体类型实例的类型是静态的，在类型声明后就不再变化，所以具体类型的变量不存在类
型查询，类型查询一定是对一个接口进行操作。
	2. case 子句后面可以跟非接口类型名，也可以跟接口类型名，匹配是按照 case 子句的
顺序进行的
	a. 如果 case 后面是一个接口类型名，且接口变量 i 绑定的实例类型实现了该接口类型的方
	方，则匹配成功，v 的类型时接口类型，v 底层绑定的实例是 i 绑定具体类型实例的副
	本
	b. 如果 case 后面是一个具体类型名，且接口变量 i 绑定的实例类型和该具体类型相同，则
	匹配成功，此时 v 就是该具体类型变量，v 的值是 i 绑定的实例值的副本
	c. 如果所有的 case 字句都不满足，则执行 default 语句，测试执行的仍然是 v := o
	d. fallthrogh 语句不能在 Type Switch 语句中使用

4.4.1 数据结构
	iface 数据接口
	非空接口初始化的过程就是初始化一个 iface 类型的结构
	type iface struct {
		tab *itab // itab 存放类型即方法指针信息
		data unsafe.Pointer // 数据信息
	}

	itab: 用来存放接口自身类型和绑定的实例类型及实例相关的函数指针
	data: 指向接口绑定的实例的副本，接口的初始化也是一种值拷贝

	type itab struct {
		inter *interfacetype // 接口自身的静态类型
		_type *_type // _type 就是接口存放的具体实例的类型（动态类型）
		hash uint32 // hash 值
		_ [4]byte
		func [1]uintptr
	}

4.4.4 空接口数据结构
	空接口 interface{} 是没有任何方法集的接口。
	空接口只关心存放的具体类型是什么，具体类型的值是什么

	type eface struct {
		_type *_type
		data unsafe.Pointer
	}

	空接口在 Go 语言中真正的意义是支持多态，如下几种使用了空接口（将空接口还原）：
	1. 接口类型断言
	2. 接口类型查询
	3. 反射

 */
