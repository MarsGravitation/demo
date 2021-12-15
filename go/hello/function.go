/*
Go 最少有个 main 函数

func function_name([parameter list]) [return_types] {
	函数体
}
*/
package main

import (
	"fmt"
	"math"
)

func main() {
	var a int = 100
	var b int = 200
	var ret int
	ret = max(a, b)
	
	fmt.Printf("最大值是: %d\n", ret)
	
	// 函数作为实参
	getSquareRoot := func(x float64) float64 {
		return math.Sqrt(x)
	}
	
	fmt.Println(getSquareRoot(8))
}

func max(num1, num2 int) int {
	var result int
	if num1 > num2 {
		result = num1
	} else {
		result = num2
	}
	return result

}

/*
函数返回多个值
*/
func swap(x, y string) (string, string) {
	retunr y, x
}

/*
通过引用传递来交换
*/
func swap(x *int, y *int) {
	var temp int
	temp = *x
	*x = *y
	*y = temp
}

/*
闭包

函数 getSquence，返回另外一个函数。该函数的目的是在闭包中传递 i 的变量
*/
func getSquence() func() int {
	i := 0
	return func() int {
		i += 1
		return i
	}
}