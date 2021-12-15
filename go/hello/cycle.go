/*
循环

for init; condition; post {}

for condition {}

for {}

for range 可以对 slice、map、数组、字符串进行迭代循环
for key,value := range oleMap {
	newMap[key] = value
}
*/
package main

import "fmt"

func main() {
	sum := 0
	for i := 0; i <= 10; i++ {
		sum += i
	}
	fmt.Println(sum)
	
	strings := []string{"google", "cxd"}
	for i, s := range strings {
		fmt.Println(i, s)
	}
	
	numbers := [6]int{1, 2, 3, 5}
	for i, x := range numbers {
		fmt.Printf("第 %d 位 x 的值 = %d\n", i, x)
	}
	
	var i, j int
	
	for i = 2; i < 100; i++ {
		for j = 2; j <= (i/j); j++ {
			if (i % j == 0) {
				break;
			}
		}
		if (j > (i/j)) {
			fmt.Printf("%d 是素数\n", i)
		}
	}
	
	var a int = 10
	
	LOOP: for a < 20 {
		if a == 15 {
			a = a + 1
			goto LOOP
		}
		fmt.Printf("a 的值为：%d\n", a)
		a++
	}
}