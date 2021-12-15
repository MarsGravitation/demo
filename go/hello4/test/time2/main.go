package main

import (
	"fmt"
	"time"
)

func main() {
	test2()
}

func test() {
	// 创建定时器，每个 1s 后，定时器就会给 Channel 发送一个事件（当前时间）
	t := time.NewTimer(time.Second * 1)
	defer t.Stop()

	fmt.Println(time.Now())
	for {
		<-t.C
		fmt.Println(time.Now())
		// 需要重置 Reset 使 t 重新开始计时
		t.Reset(time.Second * 2)
	}
}

func test2() {
	ticker := time.NewTicker(2 * time.Second)
	defer ticker.Stop()

	for  {
		select {
		case <- ticker.C:
			fmt.Println(time.Now())
		}
	}
}

// https://studygolang.com/articles/18468
func test3() {
	//ticker := time.NewTicker(2 * time.Second)
	//ch := make(chan bool)
	//go func(ticker * time.Ticker) {
	//	defer ticker.Stop()
	//	for {
	//		select {
	//		case <-ticker.C:
	//			fmt.Println("Ticker running...")
	//			case stop := <-ch:
	//				if stop {
	//					fmt.Println("Ticker Stop")
	//					return
	//				}
	//		}
	//	}
	//}
}