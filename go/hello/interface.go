/*
定义接口

type interface_name interface {
}

定义结构体
tyoe struct_name struce {
}

实现接口方法
func (struct_name_variable struct_name) method_name1() [return_type] {
}

func (struct_name_variable struct_name) method_namen() [return_type] {
}
*/
package main

import (
	"fmt"
)

type Phone interface {
	call()
}

type NokiaPhone struct {
}

func (nokiaPhone NokiaPhone) call() {
	fmt.Println("I am Nokia, I can call you!")
}

type IPhone struct {
}

// 该 method 属于 IPhone 类型对象中的方法
func (iPhone IPhone) call() {
	fmt.Println("I am iPhone, I can call you!")
}

func main() {
	var phone Phone
	
	phone = new(NokiaPhone)
	phone.call()
	
	phone = new(IPhone)
	phone.call()
}
