/*
定义 Map

var map_variable map[key_Data_type]value_data_type

map_variable := make(map[key_Data_type]value_data_type)

如果不出实话 map，那么就会创建一个 nil map。nil map 不能村饭键值对
*/
package main

import "fmt"

func main() {
	var ccm map[string]string
	ccm = make(map[string]string)
	
	ccm [ "France" ] = "巴黎"
    ccm [ "Italy" ] = "罗马"
    ccm [ "Japan" ] = "东京"
    ccm [ "India " ] = "新德里"

	for c := range ccm {
		fmt.Println(c, "首都是", ccm[c])
	}
	
	// 查看元素是否存在
	capital, ok := ccm ["American"]
	if ok {
		fmt.Println("American 的首都是：", capital)
	} else {
		fmt.Println("American 的首都不存在")
	}
	
	ccm2 := map[string]string{"France": "Paris", "Italy": "Rome", "Japan": "Tokyo", "India": "New delhi"}
	
	delete(ccm2, "France")
	
	for country := range ccm2 {
		fmt.Println(country, "首都是", ccm2 [ country ])
	}
	
	// 语言类型转换
	mean := float32(1)
	fmt.Printf("mean 的值为: %f\n",mean)
}