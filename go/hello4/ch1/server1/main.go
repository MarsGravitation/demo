package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	// 将发送到 / 路径下的请求和 handler 函数关联起来
	http.HandleFunc("/", handler)
	log.Fatal(http.ListenAndServe("localhost:8080", nil))
}

func handler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "URL.Path = %q\n", r.URL.Path)
}