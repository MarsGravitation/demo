package main

import (
	"bytes"
	"crypto/md5"
	"fmt"
	"html/template"
	"io"
	"io/ioutil"
	"log"
	"mime/multipart"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"
)

/*
net/http 包可以很方便的就搭建起来一个可以运行的 Web 服务。同时使用这个包能很简单
地对 Web 的路由，静态文件，模板，cookie 等数据进行设置和操作。
 */

func sayHelloName(w http.ResponseWriter, r *http.Request)  {
	// 解析 URL 传递的参数，对于 POST 则解析响应包的主题
	// 注意：如果没有调用 ParseForm 方法，下面无法获取表单的数据
	r.ParseForm() // 解析参数，默认是不会解析的
	fmt.Println(r.Form)
	fmt.Println("path", r.URL.Path)
	fmt.Println("scheme", r.URL.Scheme)
	// Form type Values map[string][]string
	fmt.Println(r.Form["url_long"])
	for k, v  := range r.Form {
		fmt.Println("key: ", k)
		fmt.Println("value: ", strings.Join(v, " "))
	}
	fmt.Fprintf(w, "Hello World!")

}

func login(w http.ResponseWriter, r *http.Request)  {
	fmt.Println("method: ", r.Method)
	if r.Method == "GET" {
		t, _ := template.ParseFiles("login.gtpl")
		t.Execute(w, nil)
	} else {
		r.ParseForm()
		// 请求的是登录数据
		fmt.Println("username", r.Form["username"])
		fmt.Println("password", r.Form["password"])
	}
}

/*
upload
处理文件上传我们需要调用 r.ParseMultipartForm
maxMemory 如果超过后，那么剩下的部分将存储在系统的临时文件中
我们可以通过 r.FormFile 获取上面的文件句柄，然后实例中使用了 io.Copy 来存储文件

获取其他非文件字段信息的时候不需要调用 r.ParseForm，因为在需要的时候 go 会自动
去调用。而且 ParsMultipartForm 调用一次之后，后面再次调用不会再有效果
 */
func upload(w http.ResponseWriter, r *http.Request)  {
	fmt.Println("method: ", r.Method)
	if r.Method == "GET" {
		curtime := time.Now().Unix()
		h := md5.New()
		io.WriteString(h, strconv.FormatInt(curtime, 10))
		token := fmt.Sprintf("%x", h.Sum(nil))

		t, _ := template.ParseFiles("upload.gtpl")
		t.Execute(w, token)
	} else {
		r.ParseMultipartForm(32 << 20)
		file, header, err := r.FormFile("uploadfile")
		if err != nil {
			fmt.Println(err)
			return
		}
		defer file.Close()
		fmt.Fprintf(w, "%v", header.Header)

		f, err := os.OpenFile(".\\" + header.Filename, os.O_WRONLY|os.O_CREATE, 0666)
		if err != nil {
			fmt.Println(err)
			return
		}
		defer f.Close()
		io.Copy(f, file)
	}
}

/*
客户端通过 multipart.Write 把文件的文本流写入一个缓存中，然后调用 http 把
缓存传到服务器
 */
func postFile(filename string, targetUrl string) error {
	bodyBuf := &bytes.Buffer{}
	bodyWriter := multipart.NewWriter(bodyBuf)

	// 关键的一步操作
	fileWriter, err := bodyWriter.CreateFormFile("uploadfile", filename)
	if err != nil {
		fmt.Println("error writing to buffer")
		return err
	}

	fh, err := os.Open(filename)
	if err != nil {
		fmt.Println("error opening file")
		return err
	}
	defer fh.Close()

	// io copy
	_, err = io.Copy(fileWriter, fh)
	if err != nil {
		return err
	}

	contentType := bodyWriter.FormDataContentType()
	bodyWriter.Close()

	resp, err := http.Post(targetUrl, contentType, bodyBuf)
	if err != nil {
		return err
	}
	defer resp.Body.Close()
	respBody, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}
	fmt.Println(resp.Status)
	fmt.Println(string(respBody))
	return nil
}

/*
Request: 用户请求的信息，用来解析用户的请求信息，包括 POST，GET 等信息
Response: 服务器需要反馈给客户端的信息
Conn: 用户的每次请求链接
Handler: 处理请求和生成返回信息的处理逻辑

http 包执行流程：
1. 创建 Listen Socket，监听指定的端口，等待客户端请求到来
2. Listen Socket 接受客户端的请求，得到 Client Socket，接下来通过 Client Socket
与客户端通信
3. 处理客户端的请求，首先从 Client Socket 读取 Http 请求的协议头，如果是 POST
方法，还可能要读取客户端提交的数据，然后交给相应的 handler 处理请求，handler
处理完毕准备好客户端需要的数据，通过 Client Socket 写给客户端
 */
func main() {
	// 设置访问的路由
	http.HandleFunc("/", sayHelloName)
	http.HandleFunc("/login", login)
	http.HandleFunc("/upload", upload)
	// 设置监听的端口
	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		log.Fatal("ListenAndServer: ", err)
	}
}
