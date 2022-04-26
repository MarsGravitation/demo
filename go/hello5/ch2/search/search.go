package search

import (
	"log"
	"sync"
)

/*
	每个代码文件都以 package 开头，随后跟着包的名字。
	与第三方包不同，从标准库中导入代码时，只需要给出要导入的包名。编译器查找包的时候，
总是会到 GOROOT 和 GOPATH 环境变量引用的位置去查找。

	变量没有定义在任何函数作用域内，所以会被当成包级变量。这个变量使用关键字 var
声明，而且声明为 Matcher 类型的映射（map）。
	在 Go 语言里，标识符要么从包里公开，要么不从包里公开。当代码导入了一个包时，程序
可以直接访问这个包中任意一个公开的标识符。这些标识符以大写字母开头。以小写字母开头的
标识符是不公开的，不能被其他包中的代码直接访问。
	map 是 Go 语言里的一个引用类型，需要使用 make 来构造。
	在 Go 语言中，所有变量都被初始化为其零值。对于引用类型来说，所引用的底层数据结构
会被初始化为对应的零值。但是被声明为其零值的引用类型的变量，会返回 nil 作为其值。

	Go 语言使用关键字 func 声明函数，关键字后面紧跟着函数名、参数以及返回值。

	:= 用于声明一个变量，同时给这个变量赋予初始值。编译器使用函数返回值的类型来确定
每个变量的类型。
*/

// 注册用于搜索的匹配器的映射
var matchers = make(map[string]Matcher)

// Run 执行搜索逻辑
func Run(searchTerm string) {
	// 获取需要搜索的数据源列表
	feeds, err := RetrieveFeeds()
	if err != nil {
		log.Fatal(err)
	}

	// 创建一个无缓冲的通道，接受匹配后的结果
	results := make(chan *Result)

	// 构造一个 waitGroup，以便处理所有的数据源
	var waitGroup sync.WaitGroup

	// 设置需要等待处理
	// 每个数据源的 goroutine 的数量
	waitGroup.Add(len(feeds))

	// 为每个数据源启动一个 goroutine 来查找结果
	for _, feed := range feeds {
		// 获取一个匹配器用于查找
		matcher, exists := matchers[feed.Type]
		if !exists {
			matcher = matchers["default"]
		}

		// 启动一个 goroutine 来执行搜索
		go func(matcher Matcher, feed *Feed) {
			Match(matcher, feed, searchTerm, results)
			waitGroup.Done()
		}(matcher, feed)
	}

	// 启动一个 goroutine 来监控是否所有的工作都做完了
	go func() {
		// 等待所有任务完成
		waitGroup.Wait()

		// 用关闭通道的方式，通知 Display 函数
		// 可以退出程序了
		close(results)
	}()

	// 启动函数，显示返回的结果，并且
	// 在最后一个结果显示完后返回
	Display(results)
}
