package main

import (
	"fmt"
	"math/rand"
)

func GenerateIntA() chan int {
	ch := make(chan int, 10)
	// 启动一个 goroutine 用于生成随机数，函数返回一个通道用于获取随机数
	go func() {
		for {
			ch <- rand.Int()
		}
	}()
	return ch
}

// GenerateIntB 多个 goroutine 增强型生成器
func GenerateIntB() chan int {
	ch := make(chan int, 10)
	go func() {
		for {
			ch <- rand.Int()
		}
	}()
	return ch
}

func GenerateInt() chan int {
	ch := make(chan int, 20)
	go func() {
		for {
			// 使用 select 的扇入技术增加生成的随机源
			select {
			case ch <- <-GenerateIntA():
				case ch <- <-GenerateIntB():
			}
		}
	}()
	return ch
}

func main() {
	ch := GenerateInt()

	for i := 10; i < 100; i++ {
		fmt.Println(<-ch)
	}
}
