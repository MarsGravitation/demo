/*
运算符

& 将给出变量的实际地址
* 指针变量
*/

package main

import "fmt"

func main() {
	var a int = 4
	var b int32
	var c float32
	var ptr *int
	
	  /* 运算符实例 */
   fmt.Printf("第 1 行 - a 变量类型为 = %T\n", a );
   fmt.Printf("第 2 行 - b 变量类型为 = %T\n", b );
   fmt.Printf("第 3 行 - c 变量类型为 = %T\n", c );
   
   ptr = &a
   fmt.Printf("a 的值为  %d\n", a);
   fmt.Printf("*ptr 为 %d\n", *ptr);
}