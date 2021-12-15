package main

import (
	"fmt"
	"time"
)

/*
定时器是当你想在未来某一刻执行一次时使用的
打点器则是当你想要在固定的时间间隔重复执行准备的

Tick at 2021-11-26 16:01:56.373143289 +0800 CST m=+0.500654272
Tick at 2021-11-26 16:01:56.873591846 +0800 CST m=+1.001102822
Tick at 2021-11-26 16:01:57.373183396 +0800 CST m=+1.500694329
Ticker stopped
 */
func main() {
	// 打点器和定时器的机制有点相似：一个通道用来发送数据
	// 这里我们在这个通道上使用内置的 range 来迭代值
	// 每个 500ms 发送一次的值
	ticker := time.NewTicker(time.Millisecond * 500)
	go func() {
		for t := range ticker.C {
			fmt.Println("Tick at", t)
		}
		fmt.Println("Ticker exit")
	}()

	time.Sleep(time.Millisecond * 1600)
	// 打点器可以和定时器一样被停止
	// 一旦一个打点停止了，将不能再从它的通道中收到值
	ticker.Stop()
	fmt.Println("Ticker stopped")
}
