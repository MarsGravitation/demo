package main

import "fmt"

/*
通道可以分为两个方向，一个是读，一个是写，假如一个函数的输入参数和输出参数都是相同
的 chan 类型，则该函数可以调用自己，最终形成一个调用链。
 */

func chain(in chan int) chan int {
	out := make(chan int)
	go func() {
		for v := range in {
			out <- 1 + v
		}
		close(out)
	}()
	return out
}

func main() {
	in := make(chan int)

	// 初始化输入参数
	go func() {
		for i := 0; i < 10; i++ {
			in <- i
		}
		close(in)
	}()

	// 连续调用 3 次 chan，相当于把 in 中的每个元素都加 3
	out := chain(chain(chain(in)))
	for v := range out {
		fmt.Println(v)
	}
}
