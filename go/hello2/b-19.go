package main

import (
	"fmt"
	"database/sql"
	_ "github.com/to-sql-driver/mysql"
)

type user struct {
	id int
	name string
	age int
}

func main() {
	db, err := sql.Open("mysql", "root:123456@tcp(127.0.0.1:3306)/test")
	if err != nil {
		panic(err.Error)
	}
	defer db.Close()
	
	rows, err := db.Query("select * from user")
	if err != nil {
		panic(err.Error())
	}
	
	// columns, err := rows.Columns()
	// if err != nil {
	//		panic(err.Error())
	// }
	
	defer rows.Close()
	
	for rows.Next() {
		var u user
		err = rows.Scan(&u.id, &u.name, &u.age)
		if err != nil {
			fmt.Printf("error: %v\n", err)
			return
		}
		fmt.Printf("user: %v\n", u)
	}
	

}