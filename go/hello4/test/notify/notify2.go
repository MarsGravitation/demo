package main

import (
	"github.com/fsnotify/fsnotify"
	"log"
	"os"
	"time"
)

func main() {
	done := make(chan bool)
	go notify2()
	<-done
}

func notify2() error {
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		return err
	}
	defer watcher.Close()

	path, err := os.Getwd()
	if err != nil {
		return err
	}
	watcher.Add(path + "/a.txt")
	for {
		select {
		case event := <-watcher.Events:
			log.Println("event:", event)
		case err1 := <-watcher.Errors:
			log.Println("error: ", err1)
		}
		time.Sleep(1 * time.Second)
	}
	return nil
}
