package main

import (
	"fmt"
	"time"
)

/*
Timer 1 expired
Timer 2 stopped

表示 goroutine 没有退出，当 主 goroutine 退出时，其他的也会退出
 */
func main() {
	// 定时器表示在未来某一个时刻的独立时间
	// 你告诉定时器需要等待的时间，然后它将提供一个用于通知的通道
	// 这里的定时器将等待 2s
	timer1 := time.NewTimer(2 * time.Second)

	// <-timer1.C 直到这个定时器的通道 c 明确的发送了定时器
	// 失效的值之前，将一直阻塞
	<-timer1.C
	fmt.Println("Timer 1 expired")

	// 如果你需要的仅仅是单纯的等待，你需要使用 time.Sleep
	// 定时器是有用原因之一就是你可以在定时器失效之前，需要这个定时器
	timer2 := time.NewTimer(time.Second)
	go func() {
		<-timer2.C
		fmt.Println("Timer 2 expired")
	}()
	// 第一个定时器将在程序开始后 2s 失效，但是第二个在它没失效之前就停止了
	stop := timer2.Stop()
	if stop {
		fmt.Println("Timer 2 stopped")
	}
}
