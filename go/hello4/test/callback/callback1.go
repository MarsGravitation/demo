package main

import (
	"fmt"
	"sort"
)

/*
回调函数和闭包

当函数具备一下两种特性的时候，就可以称之为高阶函数

1. 函数可以作为另一个函数的参数（典型用法是回调函数）
2. 函数可以返回另一个函数，即让另一个函数作为这个函数的返回值（闭包）

一般来说，附带的还具备一个特性：函数可以作为一个值赋值给变量

f := func() {}
f()

由于 Go 中函数不能嵌套命名函数，所以函数返回函数的时候，只能返回匿名函数

https://www.cnblogs.com/f-ck-need-u/p/9878898.html
 */

func add(msg string, a func(a, b int) int) {
	fmt.Println(msg, ":", a(33, 34))
}

// 将函数作为另一个函数的参数
func test() {
	// 函数内部不能嵌套命名函数
	// 只能定义匿名函数
	f := func(a, b int) int {
		return a + b
	}
	add("a + b", f)
}

func added() func(a, b int) int {
	f := func(a, b int) int {
		return a + b
	}
	return f
}

func test02() {
	m := added()
	fmt.Println(m(33, 44))
}

/*
回调函数(sort.SliceStable)

将函数 B 作为另一个函数 A 的参数，可以使得函数 A 的通用性更强，可以随意定义函数 B，
只要满足规则，函数 A 都可以去处理，这比较适合于回调函数

sort 包中有一个很强大的 Slice 排序工具 SliceStable()，它就是将排序函数作为参数的：

func SliceStable(slice interface{}, less func(i, j int) bool)

定义一个名为 slice 的 Slice 结构，使用名为 less 的函数去对这个 slice 排序。这个
less 函数的结构为 less func(i, j int) bool，其中 i 和 j 指定排序依据。Go 中
已经内置好了排序的算法，我们无需自己去定义排序算法，Go 会自动从 Slice 中每次去两个 i
j 索引对应的元素，然后去回调排序函数 less。所以我们只需要传递升序还是降序，根据什么排序
就可以
 */

func test03() {
	s1 := []int{112, 22, 52, 32, 12}
	// 定义排序函数
	less := func(i, j int) bool {
		// 降序排序
		return s1[i] > s1[j]
	}

	sort.SliceStable(s1, less)

	// 也可以将排序函数直接写在 SliceStable 的参数位置
	//sort.SliceStable(s1, func(i, j int) bool {
	//	return s1[i] > s1[j]
	//})
	fmt.Println(s1)
}

func main() {
	test03()
}
