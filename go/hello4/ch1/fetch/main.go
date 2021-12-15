package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
)

/*
http.Get 函数是创建 HTTP 请求的函数
resp 结构体中得到访问的请求结果
resp 的 Body 字段包括一个可读的服务器响应流
ioutil.ReadAll 函数从 response 中读取到全部内容
将其结果保存在变量 b 中
resp.Body.Close 关闭 resp 的 Body，防止资源泄漏
Printf 函数将结果 b 写出到标准输出流中
*/
func main() {
	for _, url := range os.Args[1:] {
		resp, err := http.Get(url)
		if err != nil {
			fmt.Fprintf(os.Stderr, "fetch:%v\n", err)
			os.Exit(1)
		}
		b, err := ioutil.ReadAll(resp.Body)
		resp.Body.Close()
		if err != nil {
			fmt.Fprintf(os.Stderr, "fetch:reading %s: %v\n", url, err)
			os.Exit(1)
		}
		fmt.Printf("%s", b)
	}
}
