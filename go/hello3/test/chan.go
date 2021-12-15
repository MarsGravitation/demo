package main

import (
	"fmt"
	"time"
)

/*
panic:
1. 向已经关闭的通道写数据会导致 panic
最佳实践是由写入者关闭通道
2. 重复关闭通道会导致 panic

阻塞：
向未初始化的通道写/读数据都会导致当前 goroutine 的永久阻塞

非阻塞：
1. 读取已经关闭的通道不会引发阻塞，返回通道的值，如果读取完返回零值
*/

// https://blog.csdn.net/lanyang123456/article/details/98378982
func test() {
	ch := make(chan int, 10)
	go func() {
		time.Sleep(time.Duration(10) * time.Second)
		for i := 0; i < 10; i++ {
			ch <- i
		}

		close(ch)
	}()
	//for i := 0; i < 10; i++ {
	//	ch <- i + 1
	//}
	//
	//close(ch)
	//
	//fmt.Println(<-ch)

	// ch 通道已关闭，但通道还可以读取
	for v := range ch {
		fmt.Println(v)
	}

	// 如果继续读取，返回零值
	//fmt.Println(<-ch)
}

/*
https://www.cnblogs.com/-wenli/p/12350181.html
*/

/*
range 能够感知 channel 的关闭，当 Channel 被发送数据的协程关闭时，range
就会结束，接着退出 for 循环。如果不关闭，会导致死锁

使用场景：当协程只从一个 channel 读取数据，然后进行处理，处理后协程退出
*/
func test02() {
	ch := make(chan int, 10)

	go func(inCh chan int) {
		for i := 0; i < 10; i++ {
			ch <- i + 1
		}

		close(inCh)
	}(ch)

	for v := range ch {
		fmt.Println(v)
	}
}

/*
for-select

for-select 可以让函数具有持续多路处理多个 channel 的能力。但 select
没有感知 channel 的关闭，这引出了两个问题：
1. 继续在关闭的通道上读，会读到通道传输数据类型的零值
2. 继续在关闭的通道上写，会 panic

问题 2 使用的原则是，通道只有发送方关闭，接收方不可关闭，即某个写通道只由使用
该 select 的协程关闭，select 就不存在继续在关闭的通道上写数据的问题。

问题 1 可以使用 , ok 来检测通道的关闭，使用情况有两种：
第一种：如果某个通道关闭后，需要退出协程，直接 return 即可
*/

//func test03() {
//
//	go func() {
//		for {
//			select {
//			case x, ok := <- in:
//				if !ok {
//					return
//				}
//				fmt.Printf("Process %d\n", x)
//				processedCnt++
//				case <-t.C:
//					fmt.Printf("working, processedCnd = %d\n", processedCnt)
//			}
//		}
//	}()
//
//}

/*
第二种：如果某个通道关闭了，不再处理该通道，而是继续处理其他 case，退出是
等待所有的可读通道关闭。
select 一个特征：select 不会在 nil 的通道上进行等待。
*/

//func test04() {
//	go func() {
//		select {
//		case x, ok := <-in1:
//			if !ok {
//				in1 = nil
//			}
//			case y, ok := <- in2:
//				if !ok {
//					in2 = nil
//				}
//				case <-t.C:
//					fmt.Println("working, processedCnt = %d\n", processedCnt)
//		}
//
//		if in1 == nil && in2 == nil {
//			return
//		}
//	}()
//}

/*
第三种：使用退出通道退出

使用 ,ok 来推出使用 for-select 协程，解决是当读入数据的通道关闭时，没数据读
时程序的正常结束。如果：
1. 接受的协程要退出了，如果它直接退出，不告知发送协程，发送协程将阻塞
2. 启动了一个工作协程处理数据，如何通知它退出

使用一个专门的通道，发送退出的信号，可以解决这类问题
*/
func test05() {
	ch := make(chan struct{})
	for i := 0; i < 3; i++ {

		go func(chIn <-chan struct{}) {
			defer fmt.Println("goroutine")
			for {
				select {
				case v := <-ch:
					fmt.Println(v)
					return
				}
			}
		}(ch)

	}

	time.Sleep(time.Duration(1) * time.Second)
	close(ch)
	time.Sleep(time.Duration(1) * time.Second)

}

/*
协程包含一个停止通道 stopCh，当 stopCh 被关闭，case <- stopCh 会执行，直
接返回即可

当我启动了 100 个 worker，只要 main 执行关闭 stopCh，每一个 worker 都会
得到信号，进而关闭。如果 main 向 stopCh 发送 100 个数据，这种就低效了
*/
//func worker(stopCh <- chan struct{})  {
//	go func() {
//		defer fmt.Println("worker exit")
//		for {
//			select {
//			case <- stopCh:
//				fmt.Println("Recv stop signal")
//				return
//				case <-t.C:
//					fmt.Println("Working")
//			}
//		}
//	}()
//	return
//}

/*
最假实践
1. 发送协程主动关闭通道，接受协程不关闭通道。技巧：把接收方的通道入参声明为只读
（<-chan），如果接受协程关闭只读协程，编译时就会报错
2. 协程处理 1 个通道，并且是读时，协程优先使用 for-range，因为 range 可以
关闭通道的关闭自动退出协程
3. ,ok 可以处理多个读通道关闭，需要关闭当前使用 for-select 的协程
4. 显示关闭通道 stopCh 可以处理主动通知协程退出的场景
 */
//func main() {
//	test05()
//}
