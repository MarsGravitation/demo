package function

import "fmt"

/*
resp.Body.close 调用了多次，这时为了确保 title 在所有执行路径下（即使函数运行失败）都关闭了网络连接。

defer 机制
只需要在调用普通函数或方法前加上关键字 defer。
当执行到该条语句时，函数和参数表达式得到计算，但直到包含该 defer 语句的函数执行完毕时，defer 后的函数参会被执行，
不论包含 defer 语句的函数是通过 return 正常结束，还是由于 panic 导致的异常结束。

可以在一条函数中执行多条 defer 语句，它们的执行顺序与声明顺序相反

defer 语句经常被用于处理成对的操作，如打开、关闭、连接、断开连接、加锁、释放锁。
通过 defer 机制，不论函数逻辑多复杂，都能保证在任何执行路径下，资源被释放。
释放资源的 defer 应该直接更在请求资源的语句后。

defer resp.Body.Close()
defer f.Close()
defer mu.Unlock()

defer 语句中的函数会在 return 语句更新返回值变量后再执行，又因为在函数中定义的匿名函数
可以访问该函数包括返回值变量在内的所有变量，所以，对匿名函数采用 defer 机制，可以观察函数
的返回值
 */

func double(x int) (result int) {
	defer func() {
		fmt.Printf("double(%d) = %d\n", x, result)
	}()
	return x + x
}

/*
_ = double(4)
double(4) = 8
 */
