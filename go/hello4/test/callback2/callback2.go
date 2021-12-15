/*
1. 定义
回调函数就是一个通过指针调用的函数。如果你把函数的指针（地址）作为参数传递给另外一个函数，
当这个指针被用来调用其所指向的函数时，我们就说这是回调函数。回调函数不是由该函数的实现方
直接调用，而是在特定的事件或条件发生时由另外的一方调用的，用于对该事件或条件进行响应

2. 机制
定义一个回调函数
提供函数实现的一方在初始化时，将回调函数的函数指针注册给调用者
当特定的事件或条件发生时，调用者使用函数指针调用回调函数对事件进行处理


https://studygolang.com/articles/10488
 */
package main

import (
	"fmt"
	"strconv"
)

// Callback 提供一个接口，让外部去实现
type Callback func(x, y int) int

// 回调函数的具体实现
func add04(x, y int) int {
	return x + y
}

// 中间函数（系统函数），包含了回调函数
func test04(x, y int, callback Callback) int {
	return callback(x, y)
}

// 调用函数 test04 时，调用真正地实现函数 add
func test05() {
	x, y := 1, 2
	fmt.Println(test04(x, y, add04))
}

type Callback1 func(msg string)

func errLog(msg string) {
	fmt.Println("Convert error: ", msg)
}

// 将字符串转换为 int64，如果转换失败调用 Callback
func stringToInt(s string, callback1 Callback1) int64 {
	if value, err := strconv.ParseInt(s, 0, 0); err != nil {
		callback1(err.Error())
		return 0
	} else {
		return value
	}
}

func main() {
	fmt.Println(stringToInt("18", errLog))
	fmt.Println(stringToInt("hh", errLog))
}