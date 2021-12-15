package main

import (
	"fmt"
	"reflect"
)

type Student struct {
	Name string "学生姓名"
	Age int `a:"111"b:"333"`
}

/*
reflect 通过 reflect.TypeOf() 返回一个 Type 的接口变量，通过接口抽象出来的方法访问
具体类型的信息

	1. 通用方法
	Name() --- 返回包含包名的类型名字，对于未命名类型返回的是空
	Kind() --- 返回该类型的底层基础类型

	对于 reflect.TypeOf(a)，传进去的实参 a 有两种类型，一种是接口变量，另一种是具体类
型变量。如果 a 是具体类型变量，则 reflect.TypeOf() 返回的是具体的类型信息；如果 a 是一个
接口变量，则函数的返回结果又分为两种情况：如果 a 绑定了具体类型实例，则返回的是接口的
动态类型，也就是 a 绑定的具体实例类型的信息，如果 a 没有绑定具体类型实例，则返回的是
接口自身的静态类型信息。

 */
func main() {
	s := Student{}
	rt := reflect.TypeOf(s)
	fieldName, ok := rt.FieldByName("Name")

	if ok {
		// 取 tag 数据
		fmt.Println(fieldName.Tag)
	}
	fieldAge, ok2 := rt.FieldByName("Age")
	if ok2 {
		// 可以像 JSON 一样，取 tag 里的数据，多个 tag 之间无逗号，tag 不需要引号
		fmt.Println(fieldAge.Tag.Get("a"))
		fmt.Println(fieldAge.Tag.Get("b"))
	}

	fmt.Println("type_Name:", rt.Name())
	fmt.Println("type_NumField:", rt.NumField())
	fmt.Println("type_PkgPath:", rt.PkgPath())
	fmt.Println("type_String:", rt.String())

	fmt.Println("type.Kind.String:", rt.Kind().String())
	fmt.Println("type.String()=", rt.String())

	// 获取结构类型的字段名称
	for i := 0; i < rt.NumField(); i++ {
		fmt.Printf("type.Field[%d].Name:=%v \n", i, rt.Field(i).Name)
	}

	sc := make([]int, 10)
	sc = append(sc, 1, 2, 3)
	sct := reflect.TypeOf(sc)

	// 获取 slice 元素的 type
	scet := sct.Elem()

	fmt.Println("slice element type.Kind()=", scet.Kind())
	fmt.Printf("slice element type.Kind()=%d\n", scet.Kind())
	fmt.Println("slice element type.String()=", scet.String())

	fmt.Println("slice element type.Name()=", scet.Name())
	fmt.Println("slice type.NumMethod()=", scet.NumMethod())
	fmt.Println("slice type.PkgPath()=", scet.PkgPath())
	fmt.Println("slice type.PkgPAth()=", sct.PkgPath())
}
