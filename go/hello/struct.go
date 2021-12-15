/*
定义结构体

struct 定义一个新的数据类型，结构体中有一个或多个成员
type 设定了结构体的名称

type struct_variable_type struct {
	member definition
}

声明

variable_name := structure_varibale_type {value1, value2...value}

variable_name := structure_varibale_type {key1: value1, key2: value2}

访问结构体成员
	结构体.成员名
	
	
结构体指针

定义
var struct_pointer *Books

声明
struct_pointer = &Book1

使用结构体指针访问结构体成员

struct_pointer.title
*/
package main

import "fmt"

type Books struct {
	title string
	author string
	subject string
	book_id int
}

func main() {
	// 创建一个新的结构体
	fmt.Println(Books{"Go 语言", "www.lenovo.com", "Go 语言教程", 1})
	
	// key => value 格式
	fmt.Println(Books{title: "Go 语言", author: "www.runoob.com", subject: "Go 语言教程", book_id: 6495407})
	
	// 忽略的字段为 0 或 空
	fmt.Println(Books{title: "Go 语言", author: "www.runoob.com"})
	
	
	// 声明 Book1 为 Books 类型
	var Book1 Books
	
	Book1.title = "Go 语言"
	Book1.author = "www.lenovo.com"
	Book1.subject = "Go 语言教程"
	Book1.book_id = 1
	
	/* 打印 Book1 信息 */
    fmt.Printf( "Book 1 title : %s\n", Book1.title)
    fmt.Printf( "Book 1 author : %s\n", Book1.author)
    fmt.Printf( "Book 1 subject : %s\n", Book1.subject)
    fmt.Printf( "Book 1 book_id : %d\n", Book1.book_id)
	
	printBook(Book1)
	
	printBook2(&Book1)
}

/*
结构体作为函数参数
*/
func printBook(book Books) {
	fmt.Println(book)
}

func printBook2(book *Books) {
	fmt.Printf( "Book title : %s\n", book.title)
    fmt.Printf( "Book author : %s\n", book.author)
    fmt.Printf( "Book subject : %s\n", book.subject)
    fmt.Printf( "Book book_id : %d\n", book.book_id)
}