/*
if 语句

if 布尔表达式 {
	true 执行
}


*/

package main

import "fmt"

func main() {
	var a int = 100
	if a < 20 {
		fmt.Printf("a 小于 20\n")
	} else {
		fmt.Printf("a 不小于 20\n")
	}
	fmt.Printf("a 的值为：%d\n", a)
}