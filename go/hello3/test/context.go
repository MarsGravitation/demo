package main

import (
	"context"
	"fmt"
	"runtime"
	"sync"
	"time"
)

// type Context interface {
//     Deadline() (deadline time.Time, ok bool)
//     Done() <-chan struct{}
//     Err() error
//     Value(key interface{}) interface{}
// }

// Deadline 方法需要返回当前 Context 被取消的时间，也就是完成工作的截止时间

// Dine 方法需要返回一个 channel，这个 channel 会在当前工作完成或者上下文被取消之后
// 关闭，多次调用 Done 方法会返回同一个 Channel

// Err 方法会返回当前 Context 结束的原因，他只会在 Done 返回的 Channel 被关闭时才会
// 返回非空的值
// - 如果当前 context 被取消就会返回 canceled 错误
// - 如果当前 context 超时就回返回 DeadlineExeceded 错误

// Value 方法会从 context 中返回键对应的值，对同一个上下文来说，多次调用 Value
// 并传入相同的 Key 会返回相同的结果，该方法仅用于传递跨 API 和进程键跟请求与的数据

var wg sync.WaitGroup

func worker(ctx context.Context) {
	go worker2(ctx)
LOOP:
	for {
		fmt.Println("worker")
		time.Sleep(time.Second)
		select {
		// 等待上级通知
		case <-ctx.Done():
			break LOOP
		default:
		}
	}
	wg.Done()
}

func worker2(ctx context.Context) {
LOOP:
	for {
		fmt.Println("worker2")
		time.Sleep(time.Second)
		select {
		// 等待上级通知
		case <-ctx.Done():
			break LOOP
		default:
		}
	}
}

// 输出结果：
// worker
// worker2
// worker2
// worker
// worker
// worker2
// over
func test() {
	ctx, cancle := context.WithCancel(context.Background())
	wg.Add(1)
	go worker(ctx)
	time.Sleep(time.Second * 3)
	// 通知子 goroutine 结束
	cancle()
	wg.Wait()
	fmt.Println("over")
}

func gen(ctx context.Context) <-chan int {
	dst := make(chan int)
	n := 1
	go func() {
		for {
			select {
			case <-ctx.Done():
				// return结束改 goroutine，防止泄露
				return
			case dst <- n:
				n++
			}
		}
	}()
	return dst
}

func ctxCancel() {
	ctx, cancel := context.WithCancel(context.Background())
	// 当我们取完需要的整数后调用 cancel
	defer cancel()

	for n := range gen(ctx) {
		fmt.Println(n)
		if n == 5 {
			break
		}
	}
}

func ctxDeadline() {
	d := time.Now().Add(time.Second)
	ctx, cancel := context.WithDeadline(context.Background(), d)

	// 尽管 ctx 会过期，但在任何情况下调用它的 cancel 函数都是很好的实践。
	// 如果不这样做，可能会使上下文及其父类存活的时间超过必要的时间
	defer cancel()

	select {
	case <-time.After(2 * time.Second):
		fmt.Println("overslept")
	case <-ctx.Done():
		fmt.Println(ctx.Err())
	}
}

func worker3(ctx context.Context) {
LOOP:
	for {
		fmt.Println("db connection ...")
		// 假设正常连接数据库耗时 10 毫秒
		time.Sleep(time.Millisecond * 10)
		select {
		case <-ctx.Done(): // 50 ms 后自动调用
			break LOOP
		default:
		}
	}
	fmt.Println("worker done!")
	wg.Done()
}

func ctxTimeout() {
	// 设置一个 50 毫秒的超时
	ctx, cancel := context.WithTimeout(context.Background(), time.Millisecond*50)
	wg.Add(1)
	go worker3(ctx)
	time.Sleep(time.Second * 5)
	// 通知子 goroutine 结束
	cancel()
	wg.Wait()
	fmt.Println("over")
}

func ctxTimeout2() {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()

	start := time.Now().Second()
	result, err := worker4(ctx)
	if err != nil {
		fmt.Printf("timeout: %s\n", err.Error())
	} else {
		fmt.Printf("result: %d\n", result)
	}
	end := time.Now().Second()
	fmt.Printf("take time: %d\n", end-start)
}

func worker4(ctx context.Context) (int, error) {
	respChan := make(chan int, 1)

	// var wg sync.WaitGroup
	// wg.Add(1)
	// defer wg.Wait()

	go func() {
		time.Sleep(time.Second * 5)
		respChan <- 1
		// wg.Done()
	}()

	select {
	case <-ctx.Done():
		// fmt.Printf("timeout: %s\n", ctx.Err())
		return 0, ctx.Err()
	case result := <-respChan:
		// fmt.Printf("result: %d\n", result)
		return result, nil
	}
}

func main() {
	// test()
	// ctxCancel()
	// ctxDeadline()
	// ctxTimeout()
	ctxTimeout2()
}

// https://geektutu.com/post/quick-go-context.html

// WaitGroup 和 信道 channel 是常见的 2 种并发控制的方式

func doTask(n int) {
	time.Sleep(time.Duration(n))
	fmt.Printf("Task %d Done\n", n)
	wg.Done()
}

// 如果并发启动了多个子协程，需要等待所有的子协程完成任务，WAitGroup 非常
// 适合于这类场景
func wait() {
	for i := 0; i < 3; i++ {
		wg.Add(1)
		go doTask(i + 1)
	}
	wg.Wait()
	fmt.Println("All Task Done")
}

var stop chan bool

// select + chan 机制
func reqTask(name string) {
	for {
		select {
		case <-stop:
			fmt.Println("stop", name)
			return
		default:
			fmt.Println(name, "send request")
			time.Sleep(time.Second)
		}
	}
}

func selectFor() {
	stop = make(chan bool)
	go reqTask("worker1")
	time.Sleep(3 * time.Second)
	stop <- true
	time.Sleep(3 * time.Second)
}

func reqTaskContext(ctx context.Context, name string) {
	for {
		select {
		case <-ctx.Done():
			fmt.Println("stop", name)
			return
		default:
			fmt.Println(name, "send request")
			time.Sleep(time.Second)
		}
	}
}

// 子协程中使用 select 调用 <-ctx.Done() 判断是否需要退出
// 主协程中，调用 cancel() 函数通知子协程退出

// 控制多个协程：为每个子协程传递相同的上下文，调用 cancel 函数所有子协程都会退出
func ctxCancel2() {
	ctx, cancel := context.WithCancel(context.Background())
	go reqTaskContext(ctx, "worker1")
	go reqTaskContext(ctx, "worker2")
	time.Sleep(3 * time.Second)
	cancel()
	time.Sleep(3 * time.Second)
}

type Option struct {
	Intervel time.Duration
}

func reqTaskValue(ctx context.Context, name string) {
	for {
		select {
		case <-ctx.Done():
			fmt.Println("stop", name)
			return
		default:
			fmt.Println(name, "send request")
			op := ctx.Value("options").(*Option)
			time.Sleep(op.Intervel * time.Second)
		}
	}
}

// 子协程获取传递的值
func ctxValue() {
	ctx, cancel := context.WithCancel(context.Background())
	vCtx := context.WithValue(ctx, "options", &Option{1})

	go reqTaskValue(vCtx, "worker1")
	go reqTaskValue(vCtx, "worker2")

	time.Sleep(3 * time.Second)
	cancel()
	time.Sleep(3 * time.Second)
}

// timeout 具有超时通知机制
// 测试 default

// no timeout
// end

// 不能使用 default

// 应该使用 goroutine + channel + select 实现超时
func ctxTimeout3() {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	go func(ctx context.Context) {
		for {
			select {
			case <-ctx.Done():
				fmt.Println("timeout done")
				return
			default:
				time.Sleep(2 * time.Second)
				fmt.Println("no timeout")
				return
			}
		}
	}(ctx)

	time.Sleep(5 * time.Second)
	fmt.Println("end")

	// fmt.Println("cancel")
	// time.Sleep(3 * time.Second)
}

// withDeadline 控制子协程的最迟退出时间

// 超时返回时的陷阱
func doBadthing(done chan bool) {
	time.Sleep(time.Second)
	done <- true
}

func timeout(f func(chan bool)) error {
	done := make(chan bool)
	go f(done)
	select {
	case <-done:
		fmt.Println("done")
		return nil
	case <-time.After(time.Millisecond):
		return fmt.Errorf("timeout")
	}
}

// 典型的超时例子 --- 错误
// time.After 启动了一个异步的定时器，返回一个 channel，当超过指定的时间后，
// 该 channel 将会接收到信号
// 启动了子协程执行函数 f，函数执行结束后，将向 channel done 发送结束信号
// 使用 select 阻塞等待 done 或 time.After 的信息，若超时，则返回错误，若没有超时，
// 则返回 nil

// timgout 调用 1000 次，理论上会启动 1000 个子协程
// 打印当前程序的协程个数
// sleep 2s，确定函数执行完毕

// 结果：1001

// 说明即使是函数执行完毕，协程也没有正常退出
// done 是一个无缓冲区的 channel，如果没有超时，doBadthing 会向 done 发送信号
// select 会接收 done 的信号，因此 doBadthing 能够正常退出，子协程也能够正常退出

// 但是，当超时发生时，select 接收到 time.After 的超时信号就返回了，done 没有了
// 接收反，而 doBadthing 在执行 1s 后向 done 发送信号，由于没有接受者且无缓存区，发送
// 会一直阻塞，导致协程不能退出
func testBadTimeout() {
	for i := 0; i < 1000; i++ {
		timeout(doBadthing)
	}
	time.Sleep(time.Second * 2)
	fmt.Println(runtime.NumGoroutine())
}

// 创建有缓冲区的 channel
func timeoutWithBuffer(f func(chan bool)) error {
	done := make(chan bool, 1)
	go f(done)
	select {
	case <-done:
		fmt.Println("done")
		return nil
	case <-time.After(time.Millisecond):
		return fmt.Errorf("timeout")
	}
}

// 结果：1
func testBufferTimtout() {
	for i := 0; i < 1000; i++ {
		timeoutWithBuffer(doBadthing)
	}
	time.Sleep(time.Second * 2)
	fmt.Println(runtime.NumGoroutine())
}

// 使用 select 尝试发送
// 使用 select 尝试向信道 done 发送信号，如果发送失败，则说明缺少
// 接收者，即超时了，那么直接退出即可
func doGoodthing(done chan bool) {
	time.Sleep(time.Second)
	select {
	case done <- true:
	default:
		return
	}
}
