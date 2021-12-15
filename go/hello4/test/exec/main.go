package main

import (
	"fmt"
	"os"
	"os/exec"
	"runtime"
)

func SystemCommandAsync(command string, cmd string) {
	var interpreter string
	var arg string
	switch runtime.GOOS {
	case "linux":
		interpreter = "bash"
		arg = "-c"
	case "windows":
		interpreter = "cmd"
		arg = "/c"
	}

	c := exec.Command(interpreter, arg, command)
	output, err := c.Output()
	if err != nil {
		fmt.Printf("Execute Shell:%s failed with error:%s", command, err.Error())
		return
	}
	fmt.Printf("Execute Shell:%s finished with output:\n%s", command, string(output))

}

func Command2(filename string, cmdstr string) error {
	StdoutPipeIn, StdoutPipeOut, err := os.Pipe()
	if err != nil {
		fmt.Println("Create Pipe error:%s", err.Error())
		return err
	}
	defer StdoutPipeIn.Close()
	defer StdoutPipeOut.Close()

	StderrPipeIn, StderrPipeOut, err := os.Pipe()
	if err != nil {
		fmt.Println("Create Pipe error:%s", err.Error())
		return err
	}
	defer StderrPipeIn.Close()
	defer StderrPipeOut.Close()

	SystemCommandAsync("./a.sh", "ls")

	return err
}

func main() {
	SystemCommandAsync("ping www.baidu.com", "")
}