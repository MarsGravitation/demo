/*
自定义类型和类型别名

自定义类型：
自定义类型指的是我们可以通过 Go 原生数据类型如 string，整形等去定义我们自己的数据类型

type myString string

myString 就是我们自定义的类型，它具有 String 的特性

类型别名
type rune = int32
type byte = uint8

自定义类型和类型别名的区别

自定义类型的类型数据是 main.自定义类型
类型别名经过编译就退化成原生类型

结构体

type 自定义类型名 struct {
	字段名 字段类型
}

同一个包中，自定义类型名不能重复
同一个结构体中，字段名不能重复

type person struct {
	name string
	aget int
}

实例化

方式1
var p person
p.name = "cxd"
p.age = 26

方式2

通过 new 创建指针类型的结构体，得到的是指向结构体的指针
var p = new(person)

p.name = "cxd"
(*p).age = 26

语法糖
1. 指针.属性名
2. (*pointer).属性名
3. (*pointer) 取出指针的值
4. 直接打印结构体指针的值，编译器为我们加上了 & 符号

方式3：使用键值对的方式

p := person{
	name: "cxd"
	age: 26
}

方式4：我们对一个结构体变量进行取地址，就会得到这个结构体变量的指针
p := &person{
	name: "cxd"
	age: 26
}

方式5：对方式4的简写
p := &persion{
	"cxd"
	26
}

顺序必须一致

匿名结构体

结构体的内存布局

为结构体分配内存时，默认会给它分配一块连续的内存

type test struct {
	a int8
	b int8
	c int8
	d int8
}

n := test{1,2,3,4}

构造函数

为构造体创建构造函数，一般构造体的构造函数会返回构造体指针，防止构造体过大而带来的内存开销问题

一般结构体的指针命名规范

func newPersion(name string, age int) {
	return &persion{
		name: name
		age: age
	}
}

p:=newPersion("cxd", 26)

方法接受者

在 Go 中存在方法接受者机制

方法的接受者就像类内部的成员函数

func (自定义接受者名 接受者类型) 方法名 (参数列表) (返回值列表) {
}

自定义接受者名就好比是 Java 中的 this

值类型接受者

值类型的接受者传递的实际上是接受者的一份拷贝，所作出的修改不会影响到 main 中的原始状态

结构体与 JSON 的序列化

json.Marshal(结构体)
json.Marshal(结构体指针)

如果我们想使用 json 的序列化和反序列化，需要将结构体指针的属性首字母开头大写：表示对外开放

入参位置可以是任意类型，意味着我们可以将结构体的值赋值给它，也可以讲结构体指针赋值给它

结构体标签

type Persion struct {
	Name string `tag:"name"`
	aget int 'myTage:"age"'
}

结构体标签主要用在反射的领域

*/
package main

import "fmt"

func main() {

}

func test() {
	type myString string
	type myInt = int
	
	var a myString
	var b myInt

	fmt.Printf("type of a %T",a)//type of a main.myString
	fmt.Printf("type of b %T",b)//type of b int
}

type student struct {
	name string
	age int
}

func test02() {
	m := make(map[string]*student)
	
	stus := []string{
		{name: "小王子", age: 18},
		{name: "娜扎", age: 23},
		{name: "大王八", age: 9000},
	}
	
	for i:=0;i<3;i++ {
		m[stus[i].name] = &stus[i]
	}
	
	// 输出结果如下
	// 小王子 => 小王子
	// 娜扎 => 娜扎
	// 大王八 => 大王八

	for k,v := range m {
		fmt.Println(k, "=>", v.name)
	}
	
	for _, stu := range stus {
		// k = name
		// v = 结构体指针本身
		m[stu.name] = &stu
		fmt.Printf("value = %#v \n",*m[stu.name])
	}
}

func newStudent(name string, age int) (*student) {
	return &student{
		name: name,
		age: age,
	}
}

// 值类型接受者
func (stu student) saySomething(word string) {
	fmt.Print(word)
}

// 指针类型接受者
func (stu *student) saySomething(word string) {
	fmt.Print(word)
}

type Person struct {
	Name string
}

type Student struct {
	Age int
	*Person
}

func test03() {
	stu := &Student{Age:23, Person:&Person{Name:"cxd"}}
	
	// 序列化
	data, err := json,Marshal(stu)
	if err != nil {
		fmt.Println("error:%v\n", err)
		return
	}
	fmt.Printf("json: %s \n",data)//json: {"Age":23,"Name":"jerry"} 
	
	// 反序列化
	str:="{\"Age\":23,\"Name\":\"jerry\"}"
	var Stu Student
	err2 := json.Unmarshal([]byte(str), &Stu)
	if err2 != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
    //obj: main.Student{Age:23, Person:(*main.Person)(0xc000088360)}
	fmt.Printf("obj: %#v \n",Stu)
}

type Person struct {
	Name string `tag:"name"` 
	Age int `json:"json-age"` 
	address string `json:"add"` // 能被序列化或者反序列话
}

func test04() {
	p := &Person {
		Name: "tom",
		Age: 23,
		address: "shangdong"
	}
	
	data, err := json.Marshal(p)
	if err != nil {
		fmt.Printf("error: %v\n", err)
		return
	}
	
	// 序列化反射时，得到的结果是 key，其实是可以通过 tag 指定的，默认使用字段名称当作 key
	fmt.Printf("%s",data) // {"Name":"tom","json-age":23}
}