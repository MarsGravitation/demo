package io

import (
	"bufio"
	"bytes"
	"crypto/sha1"
	"encoding/hex"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"net"
	"os"
	"path/filepath"
	"strings"
)

/*
公众号奇伢云存储

Reader 和 ReaderAt

type Reader interface {
	Read(p []byte) (n int, err error)
}

type ReaderAt interface {
	ReadAt(p []byte, off int 64) (n int, err error)
}

Reader 传入参数只有一个 p（用来装读到的数据 buffer），返回参数 n（读了多少
数据），err（返回的错误码）

ReaderAt p（装读到的数据 buffer），off（读取的偏移位置），n（读到了多少数据），
err（错误码）

Read
1. 读成功了，数据完全填充 buffer，读取了用户预期的数据大小，这时候 n == len(p),
err == nil，p 里都是用户有用的数据
2. 读失败了，err != nil
3. 读到 EOF 了，err == EOF，n 表示读到的有用数据大小，p 部分被填充
4. 允许数据还没全部准备好，返回部分数据，err == nil；也就是说，Read 接口允许：
没有读满预期的 buffer，也不是 EOF 的情况，err == nil

ReadAt
不允许第四种情况的出现

文件 IO 姿势

Go 读写文件的方式
1. 标准库封转：操作对象 File
2. 系统调用：操作对象 fd

读写数据要素
读文件，就是把磁盘上的文件的特定位置的数据读取到内存的 buffer
写文件，就是把内存 buffer 的数据写到磁盘的文件的特定位置

标准库封装

打开文件
func OpenFile(name string, flag int, perm FileMode) (*File, error)

Open 文件之后，获取到一个句柄，也就是 File 接口，之后对文件的读写都是基于 File

文件当前偏移量默认设置为 0

文件写操作

1. 写一个 buffer 到文件，使用文件当前偏移量
func (f *File) Write(b []byte) (n int, err error)
注意：该写操作会导致文件偏移量的增加

2. 从指定文件偏移，写入 buffer 到文件
func (f *File) WriteAt(b []byte, off int64) (n int, err error)
注意：该写操作不会更新文件偏移量

文件读操作


浅谈 Go IO 的知识框架

1. IO 接口描述

Reader
Writer

接口
基础类型：Reader、Writer、Closer、ReaderAt、WriterAt、Seeker、ByteReader、
ByteWriter、RuneReader、StringWriter

组合类型
ReaderClose、WriterCloser、WriteSeeker

进阶类型：

IO 通用的函数
Copy: 把一个 Reader 读出来，写到 Writer 里去，直到其中一方出错为止（读端出现 EOF）
Copy: 和 Copy 一样，多了一个结束条件：数据拷贝不会超过 N 个
CopyBuffer: 让用户指定使用多大的 Buffer 内存

io/ioutil 工具库
ReadFile: 给一个路径，把文件一把读到内存（不需要 open，close，read，write）
WriterFile：给一个路径，把内存一把写入文件
ReadDir: 读一个目录
ReadAll: 给一个 Reader 流，全部读到内存，这里把 Reader 全部读到内存，注意内存


 */

/*
处理字节数组的库
bytes.Reader: []byte -> reader
bytes.Buffer: []byte -> Reader, Writer
内存块可以作为读写的数据流
 */
func test() {
	buffer := make([]byte, 1024)
	reader := bytes.NewReader(buffer)
	n, err := io.Copy(ioutil.Discard, reader)
	fmt.Printf("n=%v,err=%v\n", n, err)
}

/*
strings.Reader: 把字符串转换成 Reader
 */
func test02() {
	data := "hello world"
	reader := strings.NewReader(data)
	n, err := io.Copy(ioutil.Discard, reader)
	fmt.Printf("n=%v,err=%v\n", n, err)

}

/*
网络：net
 */
func handleConn(conn net.Conn) {
	defer conn.Close()
	buf := make([]byte, 4096)
	conn.Read(buf)
	conn.Write([]byte("pong: "))
	conn.Write(buf)
}

func test03() {
	// net.Listen 创建一个监听套接字，在 Go 里封装成 net.Listener 类型
	server, err := net.Listen("tcp", ":9999")
	if err != nil {
		log.Fatalf("err:%v", err)
	}
	for {
		// Accept 返回一个 net.Conn，代表一条网络连接
		// net.Conn 既是 Reader，又是 Writer，拿到之后各自处理即可
		c, err := server.Accept()
		if err != nil {
			log.Fatalf("err:%v", err)
		}
		go handleConn(c)
	}
}

/*
Go 对网络 fd 的封装
1. 创建还是用 socket 的调用创建的 fd，创建出来就会立马设置 nonblock 模式（
因为 Go 的网络 fd 天然要使用 IO 多路复用的方式来走）
2. 把 socket fd 丢到 epoll 池，监听事件
3. 封转好读写事件到来的函数回调
 */
func test04() {
	// net.Dail 传入服务端地址和网络类型协议，即可返回一条和服务端通信的网络连接
	// net.Conn 既是读端，也是写端
	conn, err := net.Dial("tcp", ":9999")
	if err != nil {
		panic(err)
	}
	conn.Write([]byte("hello world\n"))
	io.Copy(os.Stdout, conn)
}

/*
os.OpenFile: 获取到文件操作句柄 File
File 实现了 Read、Write 等接口

File 基于文件 fd 的封装

个人感觉：有关于 fd（文件、socket） 的都需要关闭，流不需要关闭
 */
func test05() {
	fd, err := os.OpenFile("test.data", os.O_RDWR, 0)
	if fd != nil {
		panic(err)
	}
	io.Copy(ioutil.Discard, fd)
}

/*
缓存 io：bufio

缓冲 IO 是在底层 IO 之上实现的一层 buffer

bufio.NewWriter: 创建一个带 buffer 的 writer，默认 4096
bufio.NewWriterSize: 创建一个带 buffer 的 writer 可以手动指定 buffer size
 */
func test06() {

}

/*
https://github.com/pibigstar/go-demo/blob/master/base/file/read_file.go
 */

/*
读取文件

推荐使用 io.Copy
 */
func copy(path string) (fileMd5 string, err error) {
	f, err := os.Open(path)
	if err != nil {
		return fileMd5, err
	}
	defer f.Close()

	md5hash := sha1.New()
	if _, err := io.Copy(md5hash, f); err != nil {
		return fileMd5, err
	}
	fileMd5 = hex.EncodeToString(md5hash.Sum(nil))
	return fileMd5, nil
}

/*
ReadAll
ioutil.ReadAll
文件不能过大，这个全部读取到内存中
 */
func ReadAll(path string) (fileMd5 string, err error) {
	f, err := os.Open(path)
	if err != nil {
		return fileMd5, err
	}
	defer f.Close()

	buf := make([]byte, 1024)
	reader := bufio.NewReader(f)
	md5hash := sha1.New()
	for {
		n, err := reader.Read(buf)
		if err != nil {
			// 遇到任何错误立即返回，并忽略 EOF 错误信息
			if err == io.EOF {
				goto stop
			}
			return fileMd5, err
		}
		md5hash.Write(buf[:n])
	}
	stop:
		fileMd5 = hex.EncodeToString(md5hash.Sum(nil))
		return fileMd5, nil
}

/*
ReadFile
使用 ioutil 读取文件
 */
func ReadFile(fileName string)  {
	data, err := ioutil.ReadFile(fileName)
	check(err)
	fmt.Println(string(data))
}

// ReadAllDir
// 读取文件夹
func ReadAllDir(path string) {
	files, err := ioutil.ReadDir(path)
	check(err)
	for _, file := range files {
		fmt.Println(file.Name())
	}
}

// WriteFile
// 覆盖原先的内容
func WriteFile(fileName, data string)  {
	err := ioutil.WriteFile(fileName, []byte(data), os.ModePerm)
	check(err)
}

// AppendToFile 追加内容到文件末尾
func AppendToFile(fileName, data string)  {
	file, err := os.OpenFile(fileName, os.O_WRONLY|os.O_APPEND, os.ModePerm)
	defer file.Close()
	check(err)
	file.Write([]byte(data))
}

// CreateFile 创建文件并返回文件指针
func CreateFile(fileName string) {
	// 如果源文件已存在，会清空该文件的内容
	// 如果多级目录，某一个目录不存在，则会返回 PathError
	file, err := os.Create(fileName)
	defer file.Close()
	check(err)
}

// MkAllDir 创建多层文件夹
func MkAllDir(dirs string) {
	// 如果不存在，才创建
	if !IsExist(dirs) {
		err := os.MkdirAll(dirs, os.ModePerm)
		check(err)
		os.RemoveAll(strings.Split(dirs, "/")[0])
	}
}

// DeleteFile 删除文件
func DeleteFile(fileName string) {
	err := os.Remove(fileName)
	check(err)
}

func IsExist(filePath string) bool {
	_, err := os.Stat(filePath)
	if err != nil {
		if os.IsExist(err) {
			return true
		}
		return false
	}
	return true
}

// FileAbs 返回该文件的绝对路径
func FileAbs(path string) string {
	if absPath, err := filepath.Abs(path); err == nil {
		return absPath
	}
	return ""
}

func check(err error) {
	if err != nil {
		panic(err)
	}
}

func main() {

}
