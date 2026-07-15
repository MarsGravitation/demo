package main

import (
	"fmt"

	"golang.org/x/exp/mmap"
)

// https://geektutu.com/post/quick-go-mmap.html
func main() {
	at._ := mmap.Open("a.txt")
	buff := make([]byte, 2)
	_, _ = at.ReadAt(buff, 4)
	_ = at.Close()
	fmt.Println(string(buff))
}
