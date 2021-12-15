package main

import "fmt"

/*
goto label, continue label, break label

https://blog.csdn.net/qq_27682041/article/details/78765779

goto 语句可以无条件地转移到过程中指定的行
通常与条件语句配合使用。可用来实现条件转移，构成循环，跳出循环体等功能。
在结构化程序设计中一般不主张使用 goto 语句，以免造成程序流程的混乱
goto 对应标签既可以定义在 for 循环前面，也可以定义在 for 循环后面，当跳转到标签
地方时，继续执行

for a := 0; i < 5; a++ {
	fmt.Println(a)
	if a > 3 {
		gogo Loop
	}
}
Loop:
	fmt.Println(a)

break

Loop:
for i := 0; i < 3; i++ {
	fmt.Println(i)
	for a := 0; a < 5; a++ {
		fmt.Println(a)
		if a > 3 {
			break Loop
		}
	}
}

在没有使用 loop 标签的时候 break 只是跳出了第一层 for 循环
使用标签后跳出到指定的标签，break 只能跳出到之前，如果将 Loop 标签放在后面则会报错
break 标签只能用于 for，跳出后不再执行标签对应的 for 循环



https://blog.csdn.net/mofiu/article/details/77318376
 */

/*
break label 的跳转标签必须放在循环语句 for 前面，并且在 break label 跳出
循环不再执行 for 循环里的代码
 */
func test06()  {
	a := 1
	loop:
		for i := 0; i < 3; i++ {
			for a < 10 {
				fmt.Println(a)
				if a == 5 {
					break loop
				}
				a++
			}
		}
		fmt.Println(a)
}

/*
goto label 既可以定义在 for 循环前面，也可以定义在 for 循环后面，当跳转到标签
地方时，继续执行标签下面的代码。确切地说，时调整执行的位置，注意的是标签尽量放在
goto 后面，避免出现死循环
 */
func test07() {
	a := 1
	Loop:
		for a < 10 {
			if a == 5 {
				a++
				goto Loop
			}
			fmt.Println(a)
			a++
		}
		fmt.Println(a)
}

/*
continue label 跳出当前该次的循环圈
在 Go 的 continue 有点像 break 语句。不是强制终止，只是继续循环下一个迭代
放生，在两者之间跳过任何代码
 */
func test08() {
	var a int = 10
	LABEL:
		for a < 20 {
			if a == 15 {
				a = a + 1
				continue LABEL
			}
			fmt.Println(a)
			a++
		}
}

func main() {
	test08()
}