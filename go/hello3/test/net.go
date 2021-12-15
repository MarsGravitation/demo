package main

import (
	"bufio"
	"fmt"
	"io"
	"net"
)

func main() {
	listen()
}

/*
listen: %!s(int=52586)conn: &{{0xc00007ac80}}

listen: %!s(int=52614)
conn: 127.0.0.1:52625
*/
func netTest() {
	//  如果地址参数中的端口为空或“0”，如“127.0.0.1:”或“[::1]:0”，将自动选择端口号
	listen, err := net.Listen("tcp", ":")
	if err != nil {
		return
	}
	fmt.Printf("listen: %v\n", listen.Addr().(*net.TCPAddr).Port)
	conn, err := listen.Accept()
	if err != nil {
		return
	}
	fmt.Printf("conn: %v\n", conn.RemoteAddr())
}

func listen() {
	listener, err := net.Listen("tcp", ":8080")
	if err != nil {
		return
	}
	defer listener.Close()
	for {
		conn, err := listener.Accept()
		if err != nil {
			if err == io.EOF {
				fmt.Println("connect close...")
			} else {
				fmt.Println(err)
			}
			return
		}
		go handle(conn)
	}
}

func handle(conn net.Conn) {
	//for {
	//	buf := make([]byte, 1024)
	//	n, err := conn.Read(buf)
	//	if err != nil {
	//		return
	//	}
	//	fmt.Println("receive: ", string(buf[:n]))
	//}
	reader := bufio.NewReader(conn)
	for {
		buf := make([]byte, 1024)
		n, err := reader.Read(buf)
		if err != nil {
			fmt.Println("client: ", err)
			return
		}
		fmt.Println("receive: ", string(buf[:n]))
	}
}

/*
client 不要 sleep，否则不会出现粘包现象

https://www.cnblogs.com/yorkyang/p/7259919.html
 */
func conn() {
	conn, err := net.Dial("tcp", ":8080")
	//net.DialTCP()
	if err != nil {
		return
	}

	for i := 1; i < 100; i++ {
		conn.Write([]byte("hello world\n"))
	}

}
