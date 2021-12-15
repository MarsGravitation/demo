/*
6.1 在window中编译出在linux64和Mac64中可以使用的二进制文件

SET CGO_ENABLED=0   // 禁用cgo
SET GOOS=linux		// 目标平台是linux
SET GOARCH=amd64	// linux为64位
SET CC=arm-linux-gnueabihf-gcc
go build

SET CGO_ENABLED=0   // 禁用cgo
SET GOOS=darwin		// 目标平台是linux
SET GOARCH=amd64	// linux为64位
go build


SET CGO_ENABLED=1
SET GOOS=linux		// 目标平台是linux
SET GOARCH=amd64	// linux为64位
SET CC=arm-linux-gnueabihf-gcc

debug

https://studygolang.com/articles/2914
https://blog.csdn.net/cytzrs/article/details/51251053
*/