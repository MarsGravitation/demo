package main

import (
	"fmt"
	"sync"
)

/*
每一个请求一个 goroutine
就是来一个请求或任务就启动一个 goroutine 去处理，典型的是 Go 中的 HTTP server 服务

计算 100 个自然数的和
 */

// 工作任务
type task struct {
	begin int
	end int
	result chan<- int
}

// 任务执行：计算 begin 到 end 的和
// 执行结果写入结果 chan result
func (t *task)do() {
	sum := 0
	for i := t.begin; i <= t.end; i++ {
		sum += i
	}
	t.result <- sum
}

func InitTask(taskchan chan<- task, r chan int, p int)  {
	// 10
	qu := p / 10
	// 0
	mod := p % 10
	// 100
	high := qu * 10
	for j := 0; j < qu; j++ {
		// 1 11 21
		b := 10 * j + 1
		// 10 20 30
		e := 10 * (j + 1)
		tsk := task{
			begin: b,
			end: e,
			result: r,
		}
		taskchan <- tsk
	}
	if mod != 0 {
		tsk := task{
			begin: high + 1,
			end: p,
			result: r,
		}
		taskchan <- tsk
	}
	close(taskchan)
}

func DistributeTask(taskchan <- chan task, wait *sync.WaitGroup, result chan int) {
	for v := range taskchan {
		wait.Add(1)
		go ProcessTask(v, wait)
	}
	wait.Wait()
	close(result)
}

func ProcessTask(t task, wait *sync.WaitGroup)  {
	t.do()
	wait.Done()
}

func ProcessResult(resultchan chan int) int {
	sum := 0
	for r := range resultchan {
		sum += r
	}
	return sum
}


func main() {
	// 创建任务通道
	taskchan := make(chan task, 10)

	// 创建结果通道
	resultchan := make(chan int, 10)

	// wait 用于同步等待任务的执行
	wait := &sync.WaitGroup{}

	// 初始化 task 的 goroutine，计算 100 个自然数之和
	go InitTask(taskchan, resultchan, 100)

	// 每个 task 启动一个 goroutine 进行处理
	go DistributeTask(taskchan, wait, resultchan)

	// 通过结果通道获取结果并汇总
	sum := ProcessResult(resultchan)

	fmt.Println("sum = ", sum)
}
