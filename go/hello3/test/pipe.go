package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"time"
	//"syscall"
)

/*
https://www.cnblogs.com/walkerwang731/p/10825497.html
https://www.cnblogs.com/CheeseZH/p/5264465.html
 */

var pipeFile = "/tmp/pipe.ipc"

/*
goland 基于 named pipes 实现进程间 IPC 通信

write 是非阻塞的，read 是阻塞的
 */
func main() {
	err := os.Remove(pipeFile)
	//err := syscall.Mkfifo(pipeFile, 0666)
	if err != nil {
		log.Fatal("create named pipe error:", err)
	}
	go write()
	go read()
	for {
		time.Sleep(time.Second * 1000)
	}
}

func read() {
	fmt.Println("open a named pipe file for read.")
	file, _ := os.OpenFile(pipeFile, os.O_RDWR, os.ModeNamedPipe)
	reader := bufio.NewReader(file)

	for {
		line, err := reader.ReadBytes('\n')
		fmt.Println("read...")
		if err == nil {
			fmt.Print("load string: " + string(line))
		}
	}
}

func write() {
	fmt.Println("start schedule writing.")
	f, err := os.OpenFile(pipeFile, os.O_RDWR, 0777)
	if err != nil {
		log.Fatalf("error opening file: %v", err)
	}
	i := 0
	for {
		fmt.Println("write string to named pipe file.")
		f.WriteString(fmt.Sprintf("test write times: %d\n", i))
		i++
		time.Sleep(time.Second)
		if i == 10 {
			break
		}
	}
}
