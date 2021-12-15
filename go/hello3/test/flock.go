// +build linux
package main

import (
	"fmt"
	"os"
	"sync"
	"time"
)

/*
http://c.biancheng.net/view/5730.html

同时启动 10 个 goroutine，但在程序运行过程中，只有一个 goroutine 能获得文件锁。
其他的 goroutine 在获取不到 flock 后，会抛出异常的信息。这样即可达到同一文件在
指定的周期内只允许一个进程访问的效果。

syscall.Flock(int(f.Fd()), syscall.LOCK_EX|syscall.LOCK_NB)

flock 属于建议锁，不具备强制性。一个进程使用 flock 将文件锁住，另一个进程可以直接
操作正在被锁的文件，修改文件中的数据，原因在于 flock 只是用于检测文件是否被加锁，针对
文件已经被加锁，另一个进程写入数据的情况，内核不会阻止这个进程的写入操作，也就是建议性
的内核处理策略。

LOCK_SH：共享锁
LOCK_EX：排它锁
LOCK_UN：释放锁

进程使用 flock 尝试锁文件时，如果文件已经被其他进程锁住，进程会被阻塞知道锁被释放掉，
或者在调用 flock 的时候，采用 LOCK_NB 参数。在尝试锁住该文件的时候，发现已经被其他
服务锁住，会返回错误，错误码为 EWOULDBLOCK。

可调用 LOCK_UN 参数来释放锁，也可以通过关闭 fd 的方式来释放文件锁（flock 的第一个
参数是 fd），意味着 flock 会随着进程的关闭而被自动释放掉。
 */

type FileLock struct {
	dir string
	f *os.File
}

func New(dir string) *FileLock {
	return &FileLock{
		dir: dir,
	}
}

// Lock 加锁
func (l *FileLock) Lock() error {
	f, err := os.Open(l.dir)
	if err != nil {
		return err
	}
	l.f = f
	//err = syscall.Flock(int(f.Fd()), syscall.LOCK_EX|syscall.LOCK_NB)
	if err != nil {
		return fmt.Errorf("connot flock directory %s - %s", l.dir, err)
	}
	return err
}

func (l *FileLock) Unlock() error {
	defer l.f.Close()
	//return syscall.Flock(int(l.f.Fd()), syscall.LOCK_UN)
	return nil
}

/*
output: 9
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
connot flock directory /home/workspace/go/test/flock - resource temporarily unavailable
 */
func main() {
	path, _ := os.Getwd()
	lockedFile := path

	wg := sync.WaitGroup{}

	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(num int) {
			flock := New(lockedFile)
			err := flock.Lock()
			if err != nil {
				wg.Done()
				fmt.Println(err.Error())
				return
			}
			fmt.Printf("output: %d\n", num)
			wg.Done()
		}(i)
	}
	wg.Wait()
	time.Sleep(2 * time.Second)
}
