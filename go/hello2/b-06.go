/*
简单的理解成用来存放 *.go 的文件夹

所有的 *.go 文件的第一行都要添加以下的代码

package packageName

包名的命名规范

同一个文件夹下直接包含的文件只能属于一个 package，同一个 package 不能在多个文件夹下

包名可以不和文件夹的名字一样
报名中不能含有 - 
只有 package main 才会被编译成可执行文件


可见性

如果包内的内容想对外暴露可见，要求首字母大写

包的导入

// 单行导入
//import "code.github.com/changwu/2020-03-17/helloworld"

// 单行导入+自定义包名
 import hw "code.github.com/changwu/2020-03-17/helloworld"

// 单行导入+匿名导入
 import _ "code.github.com/changwu/2020-03-17/helloworld"

import (
	// 多行导入
	//"code.github.com/changwu/2020-03-17/helloworld"
    
	// 多行导入+自定义包名
	//"code.github.com/changwu/2020-03-17/helloworld"
    
	// 多行导入+匿名导入
	//_ "code.github.com/changwu/2020-03-17/helloworld"
)


init 

包内部都会有一个 init()，每次导入时都会先触发被导入包的 init 调用，再出发自己的 init()

init() 函数的执行时机

全局变量 --- init() --- main


不可以主动调用 init 方法
*/
import (
	hw "code.github.com/changwu/2020-03-17/helloworld"
	"fmt"
)

func init(){
	fmt.Println("this is structToJson init()")
}


func main(){
	/**
	  this is hello world init()
	  this is structToJson init()
	 */
	hw.SayHello()
	
补充：

6. 入的是路径还是包？

import "testmodule/foo"

导入时，是按照目录导入。导入目录后，可以使用这个目录下的所有包
出于习惯，包名和目录通常会设置成一样

7. 相对导入和绝对导入

绝对导入：从 $GOPATH/src 或 $GOROOT 或 $GOPATH/pkg/mod 目录下搜索包并导入

相对导入：从当前目录搜索包名开始导入

一般使用绝对引用的方式

8. 包导入路径优先级

如果使用 go modules

你导入的包如果是有域名，都会现在 $GOPATH/pkg/mod 下查找，找不到就联网，找不到就报错

如果你导入的包没有域名，比如 fmt，就只会道 $GOROOT 里查找

https://mp.weixin.qq.com/s?__biz=MzU1NzU1MTM2NA==&mid=2247483769&idx=1&sn=5ce6c75ecccad89f455f5c43712a6d74&chksm=fc355b12cb42d204d97cdc7f90373626699b3b3f2a4545308c90ceec295d274a5467f9357dc6&scene=21#wechat_redirect
}