package main

import "fmt"

/*
固定 worker 工作池

在 Go 语言编程中，一样可以轻松地构建固定数目的 goroutine 作为工作线程池。

下面以计算多个整数的和为例来说明这种并发范式。

程序中除了主要的 main goroutine，还开启了如下几类 goroutine：
1. 初始化任务的 goroutine
2. 分发任务的 goroutine
3. 等待所有 worker 结束通知，然后关闭结果通道的 goroutine

main 函数负责拉起上述 goroutine，并从结果通道获取最终的结果。
程序采用三个通道，分别是：
1. 传递 task 任务的通道
2. 传递 task 结果的通道
3. 接受 worker 处理完任务后所发送通知的通道
*/

// NUMBER 工作池的 goroutine 数目
const (
	NUMBER = 10
)

// 工作任务
type task struct {
	begin  int
	end    int
	result chan<- int
}

// 任务处理：计算 begin 到 end 的和
// 执行结果写入结果 chan result
func (t *task) do() {
	sum := 0
	for i := t.begin; i <= t.end; i++ {
		sum += i
	}
	t.result <- sum
}

// InitTask 初始化待处理 task chan
func InitTask(taskchan chan<- task, r chan int, p int) {
	qu := p / 10
	mod := p % 10
	high := qu * 10
	for j := 0; j < qu; j++ {
		b := 10*j + 1
		e := 10 * (j + 1)
		tsk := task{
			begin:  b,
			end:    e,
			result: r,
		}
		taskchan <- tsk
	}

	if mod != 0 {
		tsk := task{
			begin:  high + 1,
			end:    p,
			result: r,
		}
		taskchan <- tsk
	}

	close(taskchan)
}

// DistributeTask 读取 task chan 并分发到 worker goroutine 处理，总的数量是 workers
func DistributeTask(taskchan <-chan task, workers int, done chan struct{}) {
	for i := 0; i < workers; i++ {
		go ProcessTask(taskchan, done)
	}
}

// ProcessTask 工作 goroutine 处理具体工作，并将处理结果发送到结果 chan
func ProcessTask(taskchan <-chan task, done chan struct{})  {
	for t := range taskchan{
		t.do()
	}
	done <- struct{}{}
}

// CloseResult 通过 done channel 同步等待所有工作 goroutine 的结束，然后关闭结果 chan
func CloseResult(done chan struct{}, resultchan chan int, workers int) {
	for i := 0; i < workers; i++ {
		<- done
	}
	close(done)
	close(resultchan)
}

// ProcessResult 读取结果通道，汇总结果
func ProcessResult(resultchan chan int) int {
	sum := 0
	for r := range resultchan {
		sum += r
	}
	return sum
}

/*
1. 构建 task 并发发送到 task 通道中
2. 分别启动 n 个工作线程，不停地从 task 通道中获取任务，然后将结果写入结果通道。
如果任务通道被关闭，则负责向收敛结果的 goroutine 发送通知，告诉其当前 worker
已经完成工作。
3. 收敛结果的 goroutine 接受到所有 task 已经处理完毕的信号后，主动关闭结果通道。
4. main 函数 ProcessResult 读取并统计所有的结果。
 */
func main() {
	workers := NUMBER

	// 工作通道
	taskchan := make(chan task, 10)

	// 结果通道
	resultchan := make(chan int, 10)

	// worker 信号通道
	done := make(chan struct{}, 10)

	// 初始化 task 的 goroutine，计算 100 个自然数之和
	go InitTask(taskchan, resultchan, 100)

	// 分发任务到 NUMBER 个 goroutine 池
	DistributeTask(taskchan, workers, done)

	// 获取各个routine 处理完任务的通知，并关闭结果通道
	go CloseResult(done, resultchan, workers)

	// 通过结果通道获取结果并汇总
	sum := ProcessResult(resultchan)

	fmt.Println("sum = ", sum)

}
