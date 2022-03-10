package main

import "fmt"

func main() {
	name := "updater"
	cmd := `ps ux | awk '/` + name + `/ && !/awk/ {print $2}'`
	cmd2 := "ps ux | awk '/" + name + "/ && !/awk/ {print $2}'"
	fmt.Println(cmd)
	fmt.Println(cmd2)

	pid := "1"
	pwdx := `pwdx ` + pid + `|awk '{print $2}'`
	fmt.Println(pwdx)

}
