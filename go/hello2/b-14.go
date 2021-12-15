package main

import(
	"fmt"
	"net/http"
	"io/ioutil"
	"net/url"
	"strings"
)

func main() {
	test02()
}

// Get 请求
func test() {
	res, err := http.Get("http://www.baidu.com")
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	
	// 关闭响应体
	defer res.Body.Close()
	
	fmt.Println("res . status code", res.StatusCode)
	fmt.Println("res . status proto", res.Proto)
	data, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	fmt.Println(string(data))
}

// 带参数的 GET 请求
func test02() {
	// 使用 Go 提供的 url 包添加参数
	apiUrl := "http://httpbin.org/anything"
	data := url.Values{}
	data.Set("name", "cxd")
	data.Set("age", "26")
	u, err := url.ParseRequestURI(apiUrl)
	
	fmt.Printf("data: %T\n", data)
	
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	
	u.RawQuery = data.Encode()
	
	// 发送请求
	res, err := http.Get(u.String())
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	
	defer res.Body.Close()
	
	bytes, err2 := ioutil.ReadAll(res.Body)
	if err2 != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	fmt.Println(string(bytes))
}

func test03() {
	// 使用 Go 的 http 包，模拟客户端发送 Post 请求
	url := "http://httpbin.org/anything"
	
	// 表单数据类型
	contentType := "application/x-www-form-urlencoded"
	data := `{"name":"cxd", "age": 26}`
	
	res, err := http.Post(url, contentType, strings.NewReader(data))
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	
	// 关闭 Body
	defer res.Body.Close()
	
	bytes, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	fmt.Println("res: ", string(bytes))
}