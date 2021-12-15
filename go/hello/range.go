/*
range 关键字用于 for 循环中迭代数组，切片，通道或集合的元素。
在数组和切片中它返回元素的索引和索引对应的值，在集合中返回 k-v 对
*/
package main

import "fmt"

func main(){
	nums := []int{2, 3, 4}
	sum := 0
	for _, num := range nums {
		sum += num
	}
	
	fmt.Println("sum: ", sum)
	
	// 在数组上使用 range 将传入 index 和值两个变量。
	// 上面那个例子我们我们不需要使用元素的需要，用空白符 _ 省略了
	for i, num := range nums {
		if num == 3 {
			fmt.Println("index: ", i)
		}
	}
	
	// map
	kvs := map[string]string{"a": "apple", "b": "banana"}
	for k, v := range kvs {
		fmt.Printf("%s -> %s\n", k, v)
	}
	
	// 枚举字符串
	for i,c := range "go" {
		fmt.Println(i, c)
	}
}