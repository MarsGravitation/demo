package main

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"
)

/*
https://blog.csdn.net/guyan0319/article/details/90240731

信号 Signal 是操作系统中用来进程间通讯的一种方式。对于 Linux 系统来说，信号就是
软中断，用来通知进程发生了异步事件。

当信号发送到某个进程中时，操作系统会中断该进程的正常流程，并进入相应的信号处理函数执行
操作，完成后再回到中断的地方继续执行。
 */
func main() {
	// 创建一个 os.Signal channel
	sigs := make(chan os.Signal, 1)
	// 创建一个 bool channel
	done := make(chan bool, 1)

	// 注册要接受的信号，syscall/SIGINT: 接受 ctrl + c，syscall.SIGTERM
	// 程序退出
	// 信号没有信号参数表示接受所有的信号
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGTERM)

	// 此 goroutine 为执行阻塞接受信号。一旦有了它，它就会打印出来
	// 然后通知程序可以完成
	go func() {
		sig := <-sigs
		fmt.Println(sig)
		done <- true
	}()

	// 程序将在此处等待，直到它预期信号
	// 在 done 上发送一个值，然后退出
	fmt.Println("waiting signal")
	<-done
	fmt.Println("exiting")
}