package main

import "github.com/gin-gonic/gin"

type User struct {
	ID uint64
	Name string
}

/*
Gin 路由采用的是 http router
 */
func main() {
	users := []User{
		{ID: 1, Name: "cxd"},
		{ID: 2, Name: "cxj"},
	}
	// 实例化一个默认的 gin 实例
	r := gin.Default()
	r.GET("/", func(c *gin.Context) {
		//c.JSON(200, gin.H{
		//	"Blog": "www.baidu.com",
		//	"wechat": "cxd",
		//})
		c.JSON(200, users)
	})
	r.POST("/users", func(context *gin.Context) {
		// 创建用户

		// 接受表单数据
		context.PostForm("name")
	})
	r.DELETE("/user/123", func(context *gin.Context) {
		// 删除用户
	})
	r.PUT("/user/123", func(context *gin.Context) {
		// 更新用户
	})

	// 路由参数
	r.GET("/route/:id", func(c *gin.Context) {
		id := c.Param("id")

		// Gin 获取查询参数
		c.Query("name")
		c.String(200, "The user id is %s", id)
	})
	r.Run(":8080")
}
