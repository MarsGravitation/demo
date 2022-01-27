package main

import (
	"fmt"
	"time"
)

/*
https://wudaijun.com/2017/10/go-select/

go select 的特性：
	1. 每个 case 都必须是一个通信
	2. 所有 channel 表达式都会被求值
	3. 所有被发送的表达式都会被求值
	4. 如果任意某个通信可以进行，它就执行；其他被忽略
	5. 如果有多个 case 都可以运行，select 会随机公平地选出一个执行。其他不会执行。
	否则执行 default
	6. 如果没有 default 子句，select 将阻塞，知道某个通信可以运行；Go 不会重新对
	channel 或值进行求值
*/
func main() {

}

/*
select closed/nil channel
*/
func testS01() {
	c1 := make(chan int)
	c2 := make(chan int)
	c3 := make(chan int)
	for {
		select {
		case v1, ok := <-c1:
			// 如果 c1 被关闭，ok == false，每次从 c1 读取都会立即返回，将导致死循环
			// 可以通过将 c1 置为 nil，来让 select ignore 调这个 case，继续评估其他 case
			if !ok {
				c1 = nil
			}
			fmt.Println(v1)
		case v2 := <-c2:
			// 同样，如果 c2 被关闭，每次从 c1 读取都会立即返回对应元素类型的零值，导致死循环
			// 解决方案仍然是置 c2 为 nil，但是有可能误判（写入方是写入了一个零值而不是关闭channel）
			fmt.Println(v2)
		case v3 := <-c3:
			// 如果 c3 已经关闭，则 panic
			// 如果 c3 为 nil，则 ignore 该 case
			fmt.Println(v3)
		}
	}
}

/*
实现非阻塞读写

结合特性 5,6，可以通过带 default 语句的 select 实现非阻塞读写，在实践中还是比较
有用的，比如 GS 尝试给玩家推送某条消息，可能并不希望 GS 阻塞在该玩家的 writeChan

注意：for 和 default 基本不会同时出现
*/
func testS02() {
	writeChan := make(chan int)
	msg := 1
	select {
	case writeChan <- msg:
		// do something write successed
		fmt.Println(msg)
	default:
		// drop msg, or log err
	}
}

/*
实现定时任务

结合特性2，每次 select 都会对所有通信表达式求值，因此可通过 time.After
简洁实现定时器功能，并且定时任务可通过 done channel 停止
*/
func testS03() {
	doneC := make(chan int)
	for {
		select {
		// 每次 select 都会执行 time.After(对所有通信表达式求值)
		case <-time.After(time.Second):
		// do something per second
		case <-doneC:
			return
		}
	}
}

/*
1. donec close 了，每次 select 都会执行到 case <- donec，并读出零值（false）
2. 每次执行了 case <- donec1 后，select 再次对 case1 的 timer.After 求值，
返回一个新的下一秒超时的 Timer
3. 再次执行到 case <- donec

因此，case <- timer.After(timer.Second) 不应该解释为每一秒执行一次，而是
其他 case 如果有一秒都没有执行，那么就执行这个 case。
*/
func testS031() {
	donec := make(chan bool, 1)
	close(donec)
	for {
		select {
		case <-time.After(time.Second):
			fmt.Println("timer")
		case <-donec:
		}
	}
}

/*
结合特性 4，如果多个 case 满足读写条件，select 会随机选择一个语句执行

因为随机选择了一个 case 执行
val:0
val:1
2: case <-tick.C
val:3
4: case <-tick.C
*/
func testS04() {
	ch := make(chan int, 1024)
	go func(ch chan int) {
		for {
			val := <-ch
			fmt.Printf("val:%d\n", val)
		}
	}(ch)

	ticker := time.NewTicker(time.Second)
	for i := 0; i < 5; i++ {
		select {
		case ch <- i:
		case <-ticker.C:
			fmt.Printf("%d: case <- tick.C\n", i)

		}
		time.Sleep(500 * time.Millisecond)
	}
	close(ch)
	ticker.Stop()
}
