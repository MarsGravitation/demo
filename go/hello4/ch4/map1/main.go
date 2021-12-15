package map1

import "fmt"

/*
一个 map 就是一个哈希表的引用。
其中 K 对应的 key 必须是支持 == 比较运算符的数据类型，所以 map 可以通过测试 key 是否相等来判断是否已经存在 。

内置的 make 函数可以创建一个 map
ages := make(map[string]int)

字面值创建 map
ages := map[string]int {
	"alice": 31,
	"charlie": 34,
}

===>
ages := make(map[string]int)
ages["alice"] = 31
ages["charlie"] = 34

创建空的 map
map[string]int{}

Map 中的元素通过 key 对应的下标语法访问：
ages["alice"] = 32
fmt.Println(ages["alice"]) // "32"

使用内置的 delete 函数可以删除元素
delete(ages, "alice")

所有这些操作是安全的，即使这些元素不在 map 也没有关系；
如果一个查找失败将返回 value 类型对应的零值，
例如，即使 map 中不存在 bob 会返回 0

但是 map 中的元素并不是一个变量，不能取址操作
禁止对 map 元素取址的原因是 map 可能随着元素数量的增长而重新分配更大的内存空间，从而可能导致之前的地址无效

遍历 map
for name, age := range ages {
	fmt.Printf("%s\t%d\n", name, age)
}

如果我们一开始就知道 names 的最终大小，因此给 slice 分配一个合适的大小将会更加有效。

names := make([]string, 0, len(ages))

map 的零值是 nil

map 的查找、删除、len、range 都可以工作在 nil 的 map 上
但是向一个 nil 的 map 存入元素将导致一个 panic 异常。
在向 map 存数据前必须先创建 map

age, ok := ages["bob"]
if !ok {
	// bob is not a key in this map
}

和 slice 一样，map 之间也不能进行相等比较；唯一的例外是和 nil 进行比较

有时候我们需要一个 map 或 set 的 key 是 slice 类型，但是 slice 不可比较。
我们可以通过两个步骤绕过这个限制。
第一步，定义一个辅助函数 k，将 slice 转为 map 对应的 String 类型的 key
第二步，创建一个 key 为 String 类型的 map，在每次对 map 操作时先用 k 辅助函数将 slice 转化为 String 类型
 */

var m = make(map[string]int)

func k(list []string) string {
	return fmt.Sprintf("%q", list)
}

func Add(list[] string) {
	m[k(list)]++
}

func Count(list []string) int {
	return m[k(list)]
}
