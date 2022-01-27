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
	// 127.0.0.1:59128
	fmt.Printf("addr: %s\n", conn.RemoteAddr().String())
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
	/*
	Dial
	拨号连接到指定网络上的地址。
	已知网络有 tcp、tcp4（IPv4）、tcp6（IPv6）、udp、udp4（IPv4）、udp6（IPv6）
	ip、ip4（IPv4）、ip6（IPv6）、unix、unixgram、unixpacket

	对于 TCP 和 UDP 网络，地址的格式为 host:port
	注意必须是文字 IP 地址，或可以解析为 IP 地址的主机名
	端口必须是文字端口号或服务名称
	当使用 TCP 时，主机解析为多个 IP 地址时，Dial 会一次尝试每个地址，直到成功为止：
		Dial("tcp", "golang.org:http")
		Dial("tcp", "192.0.2.1:http")
		Dial("tcp", "198.51.100.1:80")
		Dial("udp", "[2001:db8::1]:domain")
		Dial("udp", "[fe80::1%lo0]:53")
		Dial("tcp", ":80")

	对于 IP 网络，网络必须是 ip、ip4、ip6，后跟一个冒号和文字协议号或协议名称，
	并且地址的形式为 host
	主机必须是文字 IP 地址或带有区域的文字 IPv6 地址
		Dial("ip4:1", "192.0.2.1")
		Dial("ip6:ipv6-icmp", "2001:db8::1")
		Dial("ip6:58", "fe80::1%lo0")
	对于 TCP、UDP 和 IP 网络，如果主机为空或字面上未指定的 IP 地址，假设是本地系统

	对于 Unix 网络，地址必须是文件系统路径
	 */
	conn, err := net.Dial("tcp", ":8080")
	//net.DialTCP()
	if err != nil {
		return
	}

	for i := 1; i < 100; i++ {
		conn.Write([]byte("hello world\n"))
	}

}

func resolveAddr() {
	// addr: 0.0.0.0:0
	// 解析 TCP 地址
	addr, err := net.ResolveTCPAddr("tcp4", "0.0.0.0:")
	if err != nil {
		return
	}
	fmt.Printf("addr: %+v\n", addr)
}
