/*
切片

切片是对数组的抽象

Go 数组的长度是布克改变的，在特定场景中这样的集合就不太适用了，Go 中提供了一种灵活，功能强悍的内置类型切片（“动态数组”），与数组相比切片的长度是不固定的，可以追加元素，再追加时可能使切片的容量增大

定义切片

var identifier []type

切片不需要说明长度
或使用 make() 函数创建切片

var slice1 []type = make([]type, len)

slice1 := make([]type, len)

也可以指定容量
make([]T, length, capacity)

len 是数组的长度并且也是切片的初始长度

切片初始化

s :=[] int {1, 2, 3}
直接初始化切片，[] 标识是切片类型， {} 初始化值，cap = len = 3

s := arr[:]
初始化切片 s，是数组 arr 的饮用

s : arr[startIndex:endIndex]

将 arr 中从下标 startIndex 到 endIndex - 1 的元素创建为一个新的切片

s := arr[startIndex:]
默认 endIndex 时表示一直到 arr 的最后一个元素

s := arr[:endIndex]
默认 startIndex 时将表示从 arr 的第一个元素开始

s1 := s[startIndex:endIndex]
通过切片 s 初始化切片 s1

s := make([]int, len, cap)
通过内置函数 make() 初始化切片 s，[]int 标识为其元素类型为 int 的切片

len() 和 cap()
*/
package main

import "fmt"

func main() {
	var numbers = make([]int, 3 ,5)
 
	printSlice(numbers)
	
	test02()
}

func printSlice(x []int) {
	fmt.Printf("len=%d cap=%d slice=%v\n",len(x),cap(x),x)
}

func test() {
	// 创建切片
	numbers := []int{0, 1, 2, 3, 4, 5, 6, 7, 8}
	printSlice(numbers)
	
	// 打印原始切片
	fmt.Println("numbers == ", numbers)
	
	// 1 - 4
	fmt.Println("numbers[1:4] == ", numbers[1:4])
	
	// 默认下限 0
	fmt.Println("numbers[:3] == ", numbers[:3])
	
	// 默认上线 len(s)
	fmt.Println("numbers[4:] == ", numbers[4:])
	
	numbers2 := make([]int, 0, 5)
	printSlice(numbers2)
	
	number2 := numbers[:2]
	printSlice(number2)
	
	number3 := numbers[2:5]
	printSlice(number3)
}

/*
如果向增加切片的容量，我们必须创建一个新的更大的切片并把原分片的内容都拷贝过来
*/

func test02() {
	var numbers []int
	printSlice(numbers)
	
	// 允许追加空切片
	numbers = append(numbers, 0)
	printSlice(numbers)
	
	// 向切片添加一个元素
	numbers = append(numbers, 1)
	printSlice(numbers)
	
	// 创建切片是之前切片的两倍容量
	numbers1 := make([]int, len(numbers), (cap(numbers)) * 2)
	
	// 拷贝
	copy(numbers1, numbers)
	printSlice(numbers1)
}