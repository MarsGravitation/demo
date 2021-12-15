/*
声明数组

数组声明需要指定元素类型及元素个数
var variable_name [SIZE] variable_type

var balance [10] float32

初始化数组

var balance = [5]float32{1000.0, 2.0, 3,4, 7.0, 50.0}

balance := [5]float32{1000.0, 2.0, 3,4, 7.0, 50.0}

[...] 代替数组长度，编译器会根据元素个数自行推断数组的长度
var balance = [...]float32{1000.0, 2.0, 3,4, 7.0, 50.0}
balance := [...]float32{1000.0, 2.0, 3,4, 7.0, 50.0}

设置了数组的长度，还可以通过下标来初始化元素
balance := [5]float32{1:2,.0, 3:7.0}

{} 中的元素不能大于 [] 中的数字
如果忽略 [] 中的数字不设置数组大小，Go 语言会根据元素的个数来设置数组的大小

balance[4] = 50.0

访问数组元素

数组元素可以通过索引来读取。

var salary float32 = balance[9]
*/
package main

import "fmt"

func main() {
	var n [10]int
	var i, j int
	
	for i = 0; i < 10; i++ {
		n[i] = i + 100
	}
	
	for j = 0; j < 10; j++ {
		fmt.Printf("Element[%d] = %d\n", j, n[j])
	}
	
	test()
}

func test() {
	var i, j, k int
	balance := [5]float32{1000.0, 2.0, 3.4, 7.0, 50.0}
	
	for i = 0; i < 5; i++ {
		fmt.Printf("balance[%d] = %f\n", i, balance[i])
	}
	
	balance2 := [...]float32{1000.0, 2.0, 3.4, 7.0, 50.0}
	for j = 0; j < 5; j++ {
		fmt.Printf("balance2[%d] = %f\n", j, balance2[j] )
    }
	
	balance3 := [5]float32{1:2.0, 3:7.0}
	for k = 0; k < 5; k++ {
		fmt.Printf("balance3[%d] = %f\n", k, balance3[k] )
    }
}

/*
向函数传递数组

形参设定数组大小

void myFunction(param [10]int)
{
}

形参未设定数组大小
void myFunction(param []int)
*/

func getAverage(arr []int, size int) float32
{
	var i int
	var avg, sum float32
	
	for i = 0; i < size; ++i {
		sum += arr[i]
	}
	
	avg = sum / size
	return avg
}