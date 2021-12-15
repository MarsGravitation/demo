package slice

/*
Slice 代表变长的序列，徐磊中每个元素都有相同的类型。
一个 slice 类型一般写作 []T，其中 T 代表 slice 中元素的类型

一个 slice 是一个轻量级的数据结构，提供了访问数组子序列元素的功能，而且 slice 的底层确实引用一个数组对象。
一个 slice 由三个部分构成：指针、长度和容量。
指针指向第一个 slice 元素对应的底层数组元素的地址，要注意的是 slice 的第一个元素并不一定是数组的第一个元素。
长度对应 slice 中元素的数目； len
长度不能超过容量，容量一般是从 slice 的开始位置到底层数据的结尾位置。cap

多个 slice 之间可以共享底层的数据，并且引用的数组部分可能重叠。

数组这样定义
months := [...]string{1: "January", ... , 12: "December"}

第 0 个元素被自动初始化为空字符串

slice 的切片操作 s[i:j]，用于创建一个新的 slice，引用 s 的从第 i 个元素开始
到 j - 1 个元素的子序列。新的 slice 将只有 j - i 个元素。如果 i 位置的索引被省略的话将使用 0 代替，
如果 j 位置的索引被省略的话将使用 len(s) 代替。

Q2 := months[4:7] // len = 3, cap = 9
summer := months[6:9] // len = 6, cap = 9

如果切片操作超出 cap(s) 的上限将导致一个 panic 异常，但是超出 len(s) 意味着扩展 slice

字符串的切片操作和 []byte 字节类型切片的切片操作都是类似的。
都写作 x[m:n]，并且都是返回一个原始字节序列的子序列，底层都是共享之前的底层数组，因此这种操作都是常量时间复杂度。

因为 slice 值包含指向第一个 slice 元素的指针，因此向函数传递 slice 将允许在函数内部修改底层数组的元素。
换句话说，复制一个 slice 只是对底层的数组创建了一个新的 slice 别名。

slice 之间不能比较

len(s) == 0 来判断 slice 是否为空

make([]T, len)
make([]T, len, cap)
在底层，make 创建了一个匿名的数组变量，然后返回一个 slice
只有通过返回的 slice 才能引用底层匿名的数组变量。

4.2.1 append 函数

 */

func reverse(s []int) {
	for i, j := 0, len(s) - 1; i < j; i, j = i + 1, j - 1 {
		s[i], s[j] = s[j], s[i]
	}
}
