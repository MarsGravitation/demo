package main

import (
	"fmt"
	"github.com/fsnotify/fsnotify"
	"log"
	"os"
	"path/filepath"
	"time"
)

func main() {
	done := make(chan bool)
	//notify()
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		log.Fatal(err)
	}
	defer watcher.Close()

	notify(watcher)
	//
	//go func() {
	//	for {
	//		select {
	//		case event := <-watcher.Events:
	//			log.Println("event:", event)
	//			if event.Op&fsnotify.Write == fsnotify.Write {
	//				log.Println("modified file:", event.Name)
	//			}
	//		case err := <-watcher.Errors:
	//			log.Println("error:", err)
	//		}
	//	}
	//}()
	//
	//dir, _ := os.Getwd()
	//err = watcher.Add(dir + "/a.txt")
	//if err != nil {
	//	log.Fatal(err)
	//}

	schedule()
	<-done
}

func notify(watcher *fsnotify.Watcher) {
	//watcher, err := fsnotify.NewWatcher()
	//if err != nil {
	//	log.Fatal(err)
	//}
	//defer watcher.Close()

	go func() {
		for {
			select {
			case event := <-watcher.Events:
				log.Println("event:", event)
				if event.Op&fsnotify.Write == fsnotify.Write {
					log.Println("modified file:", event.Name)
				}
			case err := <-watcher.Errors:
				log.Println("error:", err)
			}
		}
	}()

	dir, _ := os.Getwd()
	err := watcher.Add(filepath.Join(dir, "a.txt"))
	if err != nil {
		log.Fatal(err)
	}
}

func schedule() {
	t := time.NewTicker(time.Duration(1) * time.Second)
	defer t.Stop()

	for {
		select {
		case <-t.C:
			fmt.Println("hello world!!!")
			t.Reset(time.Duration(1) * time.Second)
			// fmt.Println("当前时间:" + int(time.Now().UnixNano() / 1e6) + ",执行时间间隔:" + strconv.Itoa(1))
		}
	}
}