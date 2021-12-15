package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

/*
Go 为开发数据库驱动定义了一些标准接口
 */

/*
sql.Register
database/sql 注册数据库驱动，当第三方开发者开发数据库驱动时，都会实现 init 函数，
在 init 里面调用这个完成本驱动的注册

init 函数的初始化过程，包在引入的时候会自动调用包的 init 函数以完成对包的初始化。
因此，我们引入数据库驱动包之后会自动去调用 init 函数，然后再 init 函数里面注册
这个数据库驱动，这样我们就可以使用了数据库驱动了
func init() {
    sql.Register("sqlite3", &SQLiteDriver{})
}

第三方数据库驱动都是通过调用这个函数来注册自己的数据库驱动名称以及相应的 driver
实现。在 database/sql 内部通过一个 map 来存储用户定义的相应驱动

var drivers = make(map[string]driver.Driver)

drivers[name] = driver

driver.Driver

type Driver interface {
    Open(name string) (Conn, error)
}

返回的 Conn 只能用来进行一次 goroutine 操作

它会解析 name 参数来获取相关数据库的连接信息，解析完成后，它将使用此信息来初始化
一个 Conn 并返回它

driver.Conn

只能应用在一个 goroutine 里面

type Conn interface {
    Prepare(query string) (Stmt, error)
    Close() error
    Begin() (Tx, error)
}

type Conn interface {
    Prepare(query string) (Stmt, error)
    Close() error
    Begin() (Tx, error)
}

驱动实现了 conn pool，所以不需要再去实现缓存 conn 之类的

driver.Stmt

type Stmt interface {
    Close() error
    NumInput() int
    Exec(args []Value) (Result, error)
    Query(args []Value) (Rows, error)
}

driver.Tx

type Tx interface {
    Commit() error
    Rollback() error
}
 */

func checkErr(err error)  {
	if err != nil {
		panic(err)
	}
}

func main() {
	db, err := sql.Open("mysql", "root:123456@tcp(192.168.102.128)/test")
	checkErr(err)

	// 插入数据
	stmt, err := db.Prepare("insert into user(name, age) values (?, ?)")
	checkErr(err)

	res, err := stmt.Exec("cxj", 24)
	checkErr(err)

	id, err := res.LastInsertId()
	checkErr(err)

	fmt.Println(id)

	// 更新数据
	stmt, err = db.Prepare("update user set name = ? where id = ?")
	checkErr(err)

	res, err = stmt.Exec("cxj2", id)
	checkErr(err)

	affect, err := res.RowsAffected()
	checkErr(err)

	fmt.Println(affect)

	// 查询数据
	rows, err := db.Query("select * from user")
	checkErr(err)

	for rows.Next() {
		var id1 int
		var username string
		var age int
		err := rows.Scan(&id1, &username, &age)
		checkErr(err)

		fmt.Println(id1)
		fmt.Println(username)
		fmt.Println(age)
	}

	// 删除数据
	stmt, err = db.Prepare("delete from user where id = ?")
	checkErr(err)

	res, err = stmt.Exec(id)
	checkErr(err)

	affect, err = res.RowsAffected()
	checkErr(err)

	fmt.Println(affect)

	db.Close()
}
