package main

import "encoding/json"

type student struct {
	Parameter interface{} `json:"parameter"`
}

func main() {
	student := student{
		Parameter: make([]int, 0),
	}
	bytes, err := json.Marshal(student)
	if err != nil {
		println(err)
	} else {
		println(string(bytes))
	}
}
