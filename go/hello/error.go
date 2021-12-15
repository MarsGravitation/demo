/*
Go 错误处理

Go 通过内置的错误接口提供了非常简单的错误处理机制

type error interface {
	Error() string
}

我们可以在编码中通过实现 error 接口类型来生成错误信息
函数通常在最后的返回值中返回错误信息
使用 errors.New 可返回一个错误信息

func Sqrt(f float64) (float64, error) {
	if f < 0 {
		return 0, error.New("math: square root of negative numbermath: square root of negative number")
	}
}

result, err := Sqrt(-1)
if err != nil {
	fmt.Println(err)
}
*/
package main

import (
	"fmt"
)

// 定义一个 DivideError 结构
type DivideError struct {
	dividee int
	divider int
}

// 实现 error 接口
func (de *DivideError) Error() string {
	strFormat := `
	Cannot proceed, the divider is zore.
	dividee: %d
	divier: 0
	`
	return fmt.Sprintf(strFormat, de.dividee)
}

// 定义 int 类型除法运算函数
func Divide(varDividee int, varDivider int) (result int, errorMsg string) {
	if (varDivider == 0) {
		dData := DivideError {
			dividee: varDividee,
			divider: varDivider,
		}
		errorMsg = dData.Error()
		return
	} else {
		return varDividee / varDivider, ""
	}
}

func main() {
	// 正常情况
	if result, errorMsg := Divide(100, 10); errorMsg == "" {
		fmt.Println("100/10 = ", result)
	}
	if _, errorMsg := Divide(100, 0); errorMsg != "" {
		fmt.Println("errorMsg is: ", errorMsg)
	}
}