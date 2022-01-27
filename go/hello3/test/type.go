package main

import "fmt"

func main() {
	received := make(map[string]chan []byte)
	var data = make([]byte, 1)
	copy(data, "a")
	go func() {
		received["any"] <- data
	}()

	fmt.Printf("%T", <- received["any"])
}
