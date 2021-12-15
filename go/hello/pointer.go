/*
声明指针

var var_name *var-type

* 用于指定变量时作为一个指针

var ip *int
var fp *float32
*/

package main

import "fmt"

func main() {
	test2()
}

func test() {
	var a int = 20
	// 声明指针变量
	var ip *int
	
	// 为指针变量赋值
	ip = &a
	
	fmt.Printf("a 的地址：%x\n", &a)
	
	fmt.Printf("ip 变量储存的指针地址: %x\n", ip )
	
	// * 号获取指针锁指向的内容
	fmt.Printf("*ip 的值：%d\n", *ip)
}

const MAX int = 3

func test2() {
	a := []int{10, 100, 200}
	var i int
	var ptr [MAX]*int
	
	for i = 0; i < MAX; i++ {
		// 整数地址赋值给指针数组
		ptr[i] = &a[i]
	}
	
	for i = 0; i < MAX; i++ {
		fmt.Printf("a[%d] = %d\n", i, *ptr[i])
	}
}

/*
指向指针的指针

声明

var ptr **int
*/
func test3() {
	var a int 
	var ptr *ptr
	var pptr **int
	
	a = 3000
	
	ptr = &a
	
	pptr = &ptr
	
	fmt.Printf("变量 a = %d\n", a )
    fmt.Printf("指针变量 *ptr = %d\n", *ptr )
    fmt.Printf("指向指针的指针变量 **pptr = %d\n", **pptr)
}

/*
指针作为函数参数
*/
func swap(x *int, y *int) {
	var temp = *x
	*x = *y
	*y = temp
}