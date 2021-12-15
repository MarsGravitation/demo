package append

/*
runes = append(runes, r)

通常我们并不知道 append 调用是否导致了内存的重新分配，
因此我们也不能确认新的 slice 和原始的 slice 是否引用的是相同的底层数组空间。
同样，我们不能确定在原先的 slice 上操作是否影响到新的 slice。
因此，通常是将 append 返回的结果直接赋值给输入的 slice 变量

要正确得使用 slice，需要记住尽管底层数组的元素是间接访问的，但是 slice
对应结构体本身的指针、长度、容量部分是直接访问的。
 */
func appendIn(x []int, y int) []int {
	var z []int
	zlen := len(x) + 1
	if zlen <= cap(x) {
		z = x[:zlen]
	} else {
		zcap := zlen
		if zcap < 2 * len(x) {
			zcap = 2 * len(x)
		}
		z = make([]int, zlen, zcap)
		copy(z, x)
	}
	z[len(x)] = y
	return z
}
