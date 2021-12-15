package main

import (
	"fmt"
	"io/ioutil"
	"regexp"
)

func getProcessList() (processList []string, err error) {
	fileinfo,err := ioutil.ReadDir("/proc")
	if err != nil {
		return processList, err
	}

	for _,fi := range fileinfo {
		if fi.IsDir() {
			pattern := `^[\d]*$`
			reg := regexp.MustCompile(pattern)
			ret := reg.FindAllString(fi.Name(), -1)
			if len(ret) > 0{
				fmt.Println(fi)
				// 判断目录名是否为数字，如果为数字（pid）则加入 processList
				processList = append(processList, fi.Name())
			}
		}
	}

	return processList, err
}

func main() {
	list, err := getProcessList()
	if err != nil {
		return
	}
	for _, name := range list {
		fmt.Println(name)
	}
}