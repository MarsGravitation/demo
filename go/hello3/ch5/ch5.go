package main

import (
	"fmt"
	"math/rand"
	"net/http"
	"runtime"
	"sync"
	"time"
)

/*
Go 语言的并发执行体称为 goroutine。
Go 语言通过 go 关键字来启动一个 goroutine。注意：go 关键字后面必须跟一个函数，
不能是语句或其他东西，函数的返回值被忽略。

*/

func test() {
	// 通过匿名函数形式启动 goroutine
	go func() {
		sum := 0
		for i := 0; i < 10000; i++ {
			sum += i
		}
		println(sum)
		time.Sleep(1 * time.Second)
	}()

	// NumGoroutine 可以返回当前程序的 goroutine 数目
	println("NumGoroutine = ", runtime.NumGoroutine())

	// main goroutine 故意 sleep 5s，防止其提前退出
	time.Sleep(5 * time.Second)
}

func sum() {
	sum := 0
	for i := 0; i < 10000; i++ {
		sum += i
	}
	println(sum)
	time.Sleep(1 * time.Second)
}

func test2() {
	// 通过 go + 有名函数形式启动 goroutine
	go sum()
	// NumGoroutine 可以返回当前程序的 goroutine 数目
	println("NumGoroutine = ", runtime.NumGoroutine())

	// main goroutine 故意 sleep 5s，防止其提前退出
	time.Sleep(5 * time.Second)
}

/*
goroutine 特性：
1. go 的执行是非阻塞的，不会等待
2. go 后面的函数的返回值会被忽略
3. 调度器不能保证多个 goroutine 的执行次序
4. 没有父子 goroutine，所有的 goroutine 是平等地被调度和执行的
5. Go 程序在执行时会单独为 main 函数创建一个 goroutine，遇到其他 go 关键字时
再去创建其他的 goroutine
6. Go 没有暴露 goroutine id 给用户，所以不能在一个 goroutine 里面显示地操作
另一个 goroutine，不过 runtime 包提供了一些函数访问和设置 goroutine 的相关信息
*/

/*
5.1.3 chan

chan 是 Go 语言里面的一个关键字
goroutine 是 Go 语言里面的并发执行体，通道是 goroutine 之间通信和同步的重要
组件。Go 的哲学是不要通过共享内存来通信，而是通过通信来共享内存，通道是 Go 通过
通信来共享内存的载体。

通道是有类型的，可以简单地把它理解为有类型的管道。
声明通道 chan dataType
声明一个通道变量没有任何意义，其值是 nil

make(chan dataType)
make(chan dataType, 10)

len 代表没有被读取的元素数，cap 代表整个通道的容量。
无缓存通道既可用于通信，也可以用于同步，有缓存通道主要用于通信

panic
1. 向已经关闭的通道写数据会导致 panic
最佳实践是由写入者关闭通道，能最大程度地避免向已经关闭的通道写数据而导致的 panic
2. 重复关闭通道会导致 panic

阻塞
1. 向未初始化的通道写数据或读数据都会导致当前 goroutine 的永久阻塞
2. 向缓冲区已满的通道写数据会导致 goroutine 阻塞
3. 通道中没数据，读取该通道会导致 goroutine 阻塞

非阻塞
1. 读取已关闭的通读不会引发阻塞，而是立即返回通道元素类型的零值，可以使用 comman, ok
判断通道是否已经关闭
2. 向有缓冲且没有满的通道读/写不会引发阻塞
*/

func test03() {
	c := make(chan struct{})
	go func(i chan struct{}) {
		sum := 0
		for i := 0; i < 10000; i++ {
			sum += i
		}
		println(sum)
		// 写通道
		c <- struct{}{}
	}(c)

	println("NumGoroutine=", runtime.NumGoroutine())

	// 读通道 c，通过通道进行同步等待
	<-c
}

/*
goroutine 运行结束后退出，写到缓冲通道中的数据不会消失，他可以缓冲和适配两个
goroutine 处理速率不一致的情况，缓冲通道和消息队列类似，有消峰和增大吞吐量的功能。
*/
func test04() {
	c := make(chan struct{})
	ci := make(chan int, 100)
	go func(i chan struct{}, j chan int) {
		for i := 0; i < 10; i++ {
			ci <- i
		}
		close(ci)
		// 写通道
		c <- struct{}{}
	}(c, ci)

	println("NumGoroutine=", runtime.NumGoroutine())

	// 读通道 c，通过通道进行同步等待
	<-c

	// 此时 ci 通道 经关闭，匿名函数启动的 goroutine 已经退出
	println("NumGoroutine=", runtime.NumGoroutine())

	// 但通道 ci 还可以继续读取
	for v := range ci {
		println(v)
	}
}

/*
WaitGroup

WaitGroup 用来等待多个 goroutine 完成，main goroutine 调用 Add 设置需要
等待 goroutine 的数目，每一个 goroutine 结束时调用 Done，Wait 被 main 用来
等待所有 goroutine 完成
*/

var wg sync.WaitGroup
var urls = []string{
	"http://www.baidu.com",
}

func test05() {
	for _, url := range urls {
		// 每一个 URL 启动一个 goroutine ，同事给 wg 加一
		wg.Add(1)

		go func(url string) {
			// 当前 goroutine 结束后给 wg 技术减一
			defer wg.Done()

			// 发送 HTTP GET 请求并打印 HTTP 返回码
			resp, err := http.Get(url)
			if err == nil {
				println(resp.Status)
			}
		}(url)
	}

	// 等待所有请求结束
	wg.Wait()
}

/*
Go 语言借用多路复用的概念，提供了 select 关键字，用于多路监听多个通道。当监听
的通道没有状态是可读或可写的，select 是阻塞的；只要监听的通道中有一个状态是可读
或可写的，则 select 就不会阻塞，而是进入处理就绪通道的分支流程。如果监听的通道
有多个可读或可写的状态，则 select 随机选取一个处理。
*/

func test06() {
	ch := make(chan int, 1)
	go func(chan int) {
		for {
			select {
			// 0 1　的写入是随机的
			case ch <- 0:
			case ch <- 1:

			}
		}
	}(ch)

	for i := 0; i < 10; i++ {
		println(<-ch)
	}
}

/*
5.1.6 扇入和扇出

扇入是指将多路通道聚合到一条通道中处理，Go 语言最简单的扇入就是使用 select 聚合
多条通道服务；扇出是指将一条通道发散到多条通道中处理，在 Go 语言里面具体实现就是
使用 go 关键字启动多个 goroutine 并发处理。

当生产者速度慢时，需要使用扇入技术聚合多个生产者满足消费者；当消费者速度很慢时，需要
使用扇出技术。

5.1.7
读取已经关闭的通道不会引起阻塞，也不会导致 panic，而是立即返回该通道存储类型的零值。
关闭 select 监听的某个通道能使 select 立即感知这种通知，然后进行相应的处理，这就
是所谓的退出通知机制。

下游的消费者不需要随机数时，显示地通知生产者停止生产。
*/

// GenerateIntA 是一个随机数生成器
func GenerateIntA(done chan struct{}) chan int {
	ch := make(chan int)
	go func() {
	Lable:
		for {
			select {
			case ch <- rand.Int():
			case <-done:
				break Lable
			}
		}

		println("close ch")
		// 收到通知后关闭通道 ch
		close(ch)
	}()
	return ch
}

/*
5577006791947779410
8674665223082153551
6129484611666145821
0

读取已经关闭的通道不会引发阻塞，而是立即返回通道元素类型的零值
 */
func test07() {
	done := make(chan struct{})
	ch := GenerateIntA(done)

	fmt.Println(<-ch)
	fmt.Println(<-ch)

	// 发送通知，告诉生产者停止生产
	close(done)

	fmt.Println(<-ch)
	fmt.Println(<-ch)

	// 此时生产者已经退出
	println("NumGoroutine=", runtime.NumGoroutine())
}

func main() {
	test07()
}
