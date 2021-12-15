package main

import (
	"fmt"
	"math/rand"
)

/*
一个融合了并发、缓冲、推出通知等多重特性的生成器

1. 接受一个无缓冲通道，用于接受通知退出信号
2. 返回一个有缓冲通道，用于生产
3. select 用死循环包裹
*/

// GenerateIntA done 接受通知退出信号
func GenerateIntA(done chan struct{}) chan int {
	ch := make(chan int, 5)

	go func() {
	Lable:
		for {
			select {
			case ch <- rand.Int():
			case <-done:
				break Lable
			}
		}
		close(ch)
	}()
	return ch
}

// GenerateIntB done 接受通知退出信号
func GenerateIntB(done chan struct{}) chan int {
	ch := make(chan int, 10)

	go func() {
	Lable:
		for {
			select {
			case ch <- rand.Int():
			case <-done:
				break Lable
			}
		}
		close(ch)
	}()
	return ch
}

// GenerateInt 通过 select 执行扇入操作
func GenerateInt(done chan struct{}) chan int {
	ch := make(chan int)
	send := make(chan struct{})

	go func() {
	Lable:
		for {
			select {
			case ch <- <-GenerateIntA(send):
			case ch <- <-GenerateIntB(send):
			case <-done:
				send <- struct{}{}
				send <- struct{}{}
				break Lable
			}
		}
		close(ch)
	}()
	return ch
}

func main() {
	// 创建一个作为接受退出信号的 chan
	done := make(chan struct{})

	// 启动生成器
	ch := GenerateInt(done)

	// 获取生成器资源
	for i := 0; i < 10; i++ {
		fmt.Println(<-ch)
	}

	// 通知生产者停止生产
	done <- struct{}{}
	fmt.Println("stop generate")
}
