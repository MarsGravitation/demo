/*
go test 工具

测试依赖命令 go test

按照约定，对于 go test 来说，所有以 _test.go 为后缀名的源代码文件都是需要测试的一部分，而不会被 go build 命令编译进最终的可执行文件中

在 *test.go 中有三种类型的函数，单元测试函数，基准测试函数和实例函数

测试函数	函数名前缀为 Test	测试程序的逻辑是否正确

当我们执行 go test 命令时，遍历项目中所有的 _test.go 文件中符合上述命名规则的函数，然后生成一个临时 main 包去调用相关的测试函数，然后运行 -> 构建测试结果 -> 清理测试生成的临时文件

测试函数

使用测试函数主要是看我们的代码能不能跑通

格式：
导入 testing 包
函数名以 Test 开头

func TestAdd(t *testing.T) {
	
}

其中的参数 t 用来报告测试失败和附加的日志信息

如果我们已经确定了未通过测试的函数的函数名，我们通过 -run="FuncName" 参数，明确指定仅测试函数名为 FuncName 的方法，它是一个正则表达式，当函数名匹配上时，才会被 go test 运行
*/
package test

import (
	"testing"
	"strings"
	"reflect"
)

func Split(str, sep string)(result []string) {
	i := strings.Index(str, sep)
	for i > -1 {
		result = append(result, str[:i])
		str = str[i + 1:]
		i = strings.Index(str, sep)
	}
	
	result = append(result, str)
	return
}

func TestSplit(t * testing.T) {
	// 运行函数，得到结果
	got := Split("a:b:c", ":")
	// 期望的结果
	want := []string{"a", "b", "c"}
	// slice 不能直接进行比较，借助反射包里的 DeepEquals 比较
	if !reflect.DeepEqual(got, want) {
		t.Errorf("except:%v, got:%v", want, got)
	}
}