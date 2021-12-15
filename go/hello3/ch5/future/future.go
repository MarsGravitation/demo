package main

import (
	"fmt"
	"time"
)

/*
5.2.5 future 模式

编程中经常遇到在一个流程中需要调用多个子调用的情况，这些字调用相互之间没有依赖，
如果串行地调用，则耗时会很长，此时可以使用 Go 并发编程中的 future 模式。

基本工作原理：
1. 使用 chan 作为函数参数
2. 启动 goroutine 调用函数
3. 通过 chan 传入参数
4. 做其他可以并行处理的事情
5. 通过 chan 异步获取结果
 */

// 一个查询结构体
type query struct {
	// 参数 Channel
	sql chan string
	// 结果 Channel
	result chan string
}

// execQuery 执行 Query
func execQuery(q query) {
	go func() {
		// 获取输入
		sql := <- q.sql

		// 访问数据库

		// 输出结果通道
		q.result <- "result from " + sql
	}()
}

func main() {
	// 初始化 Query
	q := query{make(chan string, 1), make(chan string, 1)}

	// 执行 query，注意执行的时候无需准备参数
	go execQuery(q)

	// 发送参数
	q.sql <- "select * from table"

	// 做其他事情，通过 sleep 描述
	time.Sleep(1 * time.Second)

	// 获取结果
	fmt.Println(<-q.result)
}