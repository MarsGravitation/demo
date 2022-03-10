/*
goroutine

原子操作
atomic 操作

channel 信道

指定 channel 的 buffer

channel 的阻塞
注意，channel 默认上是阻塞的，也就是说，如果 channel 满了，就阻塞写，如果 channel 空了，就阻
塞读

select

 */
package main

import "fmt"

func main() {
	ints := make(chan int)
	c := make(chan int, 1)
	fmt.Println(len(ints))
	fmt.Println(len(c))
}
