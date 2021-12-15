package exec3

import (
	"fmt"
	"os/exec"
	"time"
)

func main() {
	cmd := exec.Command("/bin/bash", "-c", "watch top > top.log")
	// 设置创建新的进程组
	//cmd.SysProcAttr = &syscall.SysProcAttr{
	//	Setpgid: true,
	//}
	time.AfterFunc(1 * time.Second, kill(cmd))
	err := cmd.Run()
	fmt.Printf("pid = %d err = %s\n", cmd.Process.Pid, err)
}

/*
USER    PID  PPID  PGID   SESS JOBC STAT   TT       TIME COMMAND
king 27655 91597 27655      0    1 S+   s012    0:01.10 go run main.go
king 27672 27655 27655      0    1 S+   s012    0:00.03 ..../exe/main
king 27673 27672 27655      0    1 S+   s012    0:00.00 /bin/bash -c watch top >top.log
king 27674 27673 27655      0    1 S+   s012    0:00.01 watch top

go run 产生了一个子进程 27672，command 是 go 执行的临时目录
27672 产生了 27673（watch top > top.log）进程
27673 产生了 27674（watch top）进程

为什么这些子进程没有关闭？
他们的进程组 ID 为 27655，但是我们传递的 cmd id 是 27673，因此程序并没有 kill，导致 cmd.Run() 一直在执行

Linux 中，进程组的第一个进程被称为进程组 Leader，同时这个进程组 ID 就是这个进程的 ID，
从这个进程中创建的其他进程，都会继承这个进程的进程组和会话信息；从上面可以看出 go run main.go
程序 PID 和 PGID 同位 27655，那么这个进程就是进程组 Leader，我们不能 kill 这个进程组

那么我们给要执行的进程，新建一个进程组。

在 linux 中，
int setpgid(pid_t pid, pid_t pgid)

如果将 pid 和 pgid 同时设置成 0，也就是 setpgid(0, 0)，则会使用当前进程为进程组 leader 并创建
新的进程组。

在 go 中，可以通过 cmd.SysProcAttr 来设置


root       6056   3010  4 18:02 pts/0    00:00:00 go run main.go
root       6086   6056  0 18:02 pts/0    00:00:00 /tmp/go-build328018790/b001/exe/main
root       6091   6086  0 18:02 pts/0    00:00:00 /bin/bash -c watch top >top.log
root       6092   6091  0 18:02 pts/0    00:00:00 watch top

pid=6091 err=signal: killed
 */
func kill(cmd *exec.Cmd) func() {
	return func() {
		if cmd != nil {
			// 孤儿进程
			cmd.Process.Kill()
			//syscall.Kill(-cmd.Process.Pid, syscall.SIGKILL)
		}
	}
}