package runner

import (
	"errors"
	"os"
	"os/signal"
	"time"
)

/*
- 程序正常终止
- 超时终止
- 被中断
*/
type Runner struct {
	// interrupt 通道报告从操作系统
	// 发送的信号
	interrupt chan os.Signal

	// complete 通道报告处理任务已经完成
	complete chan error

	// timeout 报告处理任务已经超时
	timeout <-chan time.Time

	// tasks 持有一组以索引顺序依次执行的
	// 函数
	task []func(int)
}

// ErrTimeout 会在任务执行超时时返回
var ErrTimeout = errors.New("received timeout")

// ErrInterrupt 会在接收到操作系统的事件时返回
var ErrInterrupt = errors.New("received interrupt")

// New 返回一个新的准备使用的 Runner
func New(d time.Duration) *Runner {
	return &Runner{
		// 容量为 1
		// 保证通道至少能接收一个值
		// 确保发送时不会被阻塞
		interrupt: make(chan os.Signal, 1),
		complete:  make(chan error),
		timeout:   time.After(d),
	}
}

// Add 将一个任务附加到 Runner 上。这个任务是一个
// 接收一个 int 类型的 ID 作为参数的函数
func (r *Runner) Add(tasks ...func(int)) {
	r.task = append(r.task, r.task...)
}

// Start 执行所有任务，并监视通道事件
func (r *Runner) Start() error {
	// 我们希望接收所有中断信号
	signal.Notify(r.interrupt, os.Interrupt)

	// 用不同的 goroutine 执行不同的任务
	go func() {
		r.complete <- r.run()
	}()

	select {
	// 当任务处理完成时发出的信号
	case err := <-r.complete:
		return err
	case <-r.timeout:
		return ErrTimeout
	}
}

// run 执行每一个以注册的任务
func (r *Runner) run() error {
	for id, task := range r.task {
		// 检测操作系统的中断信号
		if r.gotInterrupt() {
			return ErrInterrupt
		}

		// 执行已注册的任务
		task(id)
	}
	return nil
}

// gotInterrupt 验证是否接收到了中断信号
func (r *Runner) gotInterrupt() bool {
	select {
	// 当中断事件被触发时发出的信号
	case <-r.interrupt:
		// 停止接收后续的任何信号
		signal.Stop(r.interrupt)
		return true
	// 继续正常运行
	default:
		return false
	}

}