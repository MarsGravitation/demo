package main

import (
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"os"
	"time"
)
/*
goroutine 是一种函数的并发执行方式，而 Channel 是用来在 goroutine 之间进行参数传递
main 函数本身也运行在一个 goroutine 中，而 go function 则表示创建一个新的 goroutine，
并在这个新的 goroutine 中执行这个函数
main 函数中用 make 函数创建了一个传递 string 类型的 channel，对每一个命令行参数，我们
都用 go 这个关键字来创建一个 goroutine，并且让函数在这个新的 goroutine 异步执行 http.Get
方法。这个程序里的 io.Copy 回把相应的 Body 内容拷贝到 ioutil.Discard 输出流中（垃圾桶），
因为我们需要这个方法返回的字节数，但是又不想要其内容。每当请求返回内容时，fetch 函数都会往 ch
这个 channel 里写入一个字符串，由 main 函数里的第二个 for 循环来处理并打印 Channel 里的
这个字符串。当一个 goroutine 尝试在一个 channel 上做 send 或者 receive 操作时，这个 goroutine
会阻塞在调用处，知道另一个 goroutine 往这个 channel 写入或者接受值，这样两个 goroutine 才
会继续执行 channel 操作之后的逻辑

这个例子中，每一个 fetch 函数在执行时都会往 channel 里发送一个值(ch <- expression)，主函数
负责接受这些值(<-ch)。main 来接受所有 fetch 函数传回的字符串，可以避免在 goroutine 异步执行
还没完成时 main 函数提前退出
*/
func main() {
	start := time.Now()
	ch := make(chan string)
	for _, url := range os.Args[1:] {
		// start a goroutine
		go fetch(url, ch)
	}
	for range os.Args[1:] {
		// receive from channel ch
		fmt.Println(<-ch)
	}
	fmt.Printf("%.2fs elapsed\n", time.Since(start).Seconds())
}

func fetch(url string, ch chan<- string) {
	start := time.Now()
	resp, err := http.Get(url)
	if err != nil {
		ch <- fmt.Sprintf("while reading %s: %v", url, err)
		return
	}
	nbytes, err := io.Copy(ioutil.Discard, resp.Body)
	// don't leak resources
	resp.Body.Close()
	if err != nil {
		ch <- fmt.Sprintf("while reding %s: %v", url, err)
		return
	}
	secs := time.Since(start).Seconds()
	ch <- fmt.Sprintf("%.2fs %7d %s", secs, nbytes, url)
}
