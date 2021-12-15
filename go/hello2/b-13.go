/*
并发编程

1.1 简介

Java 对多线程的封装类是 Thread，每一个 Thread 的实例都会通过 JVM 调用 C++ 的方法，和系统内核的线程做唯一的绑定，因此，Java 中的多线程是真实的多线程，可以利用多核系统的特性

Java 的多线程中线程栈空间初始大小为 2M，所以 Java 的多线程很吃内存

对于 Go 来说，通过 goroutine 实现了多个函数之间的并发执行，我们不在自己去写进程，线程，协程，一个 goroutine 就够了

goroutine 绑定着用户空间的线程，通常只需要 2k 的内存就可以，所以 Go 可以实现的高并发很容易就到达十万级别

1.2 goroutine 的使用
只需要在 func 之前，添加上 go 关键字就行

1.3 goroutine 的调度


二、channel
2.01 Channel 简介
在 Java 中，JMM 规范要求，如果两条线程之间想通信的话，需要借助于主存中的变量区完成通信

在 Go 中，两个并发执行的函数可以通过 channel 进行数据交互

Go 的并发模型是 CSP(communicating sequential processor) 即，通过通信来共享内存，而不是通过内存来实现通信

在 Go 中，通过 channel 去实现各个 goroutine 之间的通信，channel 是一个特殊类型，遵循先进先出的规则，保证收发信息的顺序，每一个 channel 都有它具体的类型，声明通道时，需要指定这个类型

2.02 channel 的类型
var ch1 chan int

2.03 创建通道
通道声明后，还得为他分配空间才能使用

可以通过 make 函数设置缓存区的大小

ch4 := make(chan int)

2.04 channel 的操作
// 创建
ch := make(chan int, 15)
// 将值写入通道
ch <- 10
// 取出存储 value
value := <- ch
// 取出并忽略
<- ch
// 关闭通道，只有在通知接收方所有的数据都发送完毕的时候，才需要关闭通道
// 通道是可以强制回收的，所以不一定非得关闭通道
close(ch)

2.05有缓冲的 channel
当缓冲通道达到满的状态的时候，就会表现出阻塞了，因为这时再也不能承载更多的数据了，必须把数据拿走，才可以流入数据

在声明一个信道的时候，我们给 make 以第二个参数来指明它的容量（默认为 0，即无缓冲）：
// 写入 2 个元素都不会阻塞当前 goroutine，存储个数达到 2 的时候会阻塞
// 如果被阻塞后，还有其他的 goroutine 去消费这个 Channel，就会爆出死锁
var ch chan int = make(chan int, 2)

ch := make(chan int, 3)
ch <- 1
ch <- 2
ch <- 3

// 信道 ch 会阻塞 main 线，报死锁
// 缓冲信道会在满容量的时候加锁
ch <- 4

// 缓冲信道是先进先出的，我们可以把缓冲信道看成一个线程安全的队列
ch := make(chan int, 3)
ch <- 1
ch <- 2 
ch <- 3

fmt.Println(<-ch) // 1
fmt.Println(<-ch) // 2
fmt.Println(<-ch) // 3

// 缓冲区大小是 15 的通道
ch7 :- make(chan []int, 15)

// 向有缓冲区的通道中，只存入，当超出缓冲区大小的容量时，就会报错，死锁
ch := make(chan int, 3)
for i:=3; i < 8; i++ {
	ch <- i
}


2.06 无缓冲区的 channel

// 没有缓冲区的通道
ch6 := make(chan []int)

当向没有缓冲区的 channel 写入数据，同时也没有其他的 goroutine 去尝试获取锁，就会报死锁

因为我们使用 ch := make(chan int) 创建的是无缓冲的通道，无缓冲的通道只有在有人接受值的时候才能发送值。

// deadlock
ch := make(chan int)
ch <- 10

// 找个 goroutine 去消费它
func hello(ch <- chann int) {
	var  value = <- ch
	fmt.Println("value: ",value)
}

ch := make(chan int)
go hello(ch)
ch <- 10

*/
package main

import (
	"fmt"
 	"time"
	"sync"
)

func hello() {
	fmt.Println("Hello Goroutine!")
}

// 只会输出 main goroutine done!
// 因为 main goroutine 执行结束之后，就会结束，子任务也被退出
func main() {
	test02()
}

func test() {
	go hello()
	fmt.Println("main  goroutine done!")
	
	// 方式一：sleep 1s
	time.Sleep(time.Second)
}

var wait = &sync.WaitGroup{}

// 方式二：使用 sync.waitGroup 来实现同步
func test02() {
	// 加一
	wait.Add(1)
	go hello02()
	
	// 当计数为 0 时，不在 wait
	wait.Wait()
	fmt.Println("main  goroutine done!")
}

func hello02() {
	// 减少一次计数
	defer wait.Done();
	fmt.Println("Hello Goroutine!")
}