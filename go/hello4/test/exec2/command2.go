package main

import (
	"bufio"
	"context"
	"fmt"
	"io"
	"os/exec"
	"syscall"
	"time"
)

func main() {
	for i:=0; i < 1; i++ {
		cmd()
		time.Sleep(time.Duration(1) * time.Second)
	}

}

func cmd() {
	ctx, cancel := context.WithCancel(context.Background())
	cmd := exec.CommandContext(ctx, "bash", "-c", "pwd")

	stdout, _ := cmd.StdoutPipe()
	stderr, _ := cmd.StderrPipe()

	go func() {
		r := bufio.NewReader(stdout)
		cat(r, 1)
	}()

	go func() {
		e := bufio.NewReader(stderr)
		cat(e, 2)
	}()

	err := cmd.Start()
	if err != nil {
		panic(err)
	}

	done := make(chan error)

	go func() {
		//time.Sleep(time.Duration(60) * time.Second)
		done <- cmd.Wait()
	}()

	after := time.After(time.Duration(5) * time.Second)
	select {
	case <-after:
		cmd.Process.Signal(syscall.SIGINT)
		cancel()
	case <-done:
		fmt.Println("当前任务执行完成")
	}
}

func cat(reader *bufio.Reader, f int) {
	buf := make([]byte, 1024)

	var read int
	var err error
	for {
		fmt.Println(f)
		read, err = reader.Read(buf)

		if read == 0 || err == io.EOF {
			break
		}
		fmt.Printf("read : %d, err: %v\n", read, err)
		fmt.Printf("result: %s\n", string(buf[:read]))
	}
}