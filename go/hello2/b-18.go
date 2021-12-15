/*
GOROOT
go 命令的位置

GOPATH

go env GOPATH

bin: 存放编译后生成的二进制可执行文件
pkg: 存放编译后生成的 .a 文件
src: 存放项目的源代码，可以是自己写的代码，也可以是你 go get 下载的包


go install myapp，生成的可执行文件会放在 $GOPATH//bin

如果你安装的是一个库，则会生成 .a 文件到 $GOPATH/pkg 下对应的平台目录中，生成 .a 为后缀的文件

GOPATH 存在的问题：
1. 无法指定版本的包，不同版本的包的导入方法一样
2. 别人运行你的程序时，无法保证他下载的包版本是你所期望的版本
3. 在本地，一个包只能留一个版本，意味着你在开发本地的所有项目，都得使用同一个版本的包

go mod

GO111MODULE=off 禁用模块支持，编译时慧聪 GOPATH 和 vendor 文件夹中查找包
GO111MODULE=on 启用模块支持，编译时会忽略GOPATH和vendor文件夹，只根据 go.mod下载依赖。
GO111MODULE=auto，当项目在 $GOPATH/src 外且根目录有 go.mod 文件时，自动开启模块支持

go env -w GO111MODULE="on"

go mod init github.com/cxd/module-main - 初始化，多了 go.mod

go get github.com/sirupsen/logrus

go install - 执行安装，会下载依赖包，并且完善 go.mod 文件，生成 go.sum 文件

go install 把 logrus 所有依赖包安装在 $GOPATH/pkg 下
同时，可以发现 go install 问我们生成的可执行文件 module-main 也在 $GOPATH//bin 下

go.mod 文件

第一行：模块的引用路径
第二行：项目使用的 go 版本
第三行：项目所需的直接依赖包及其版本

module github.com/cxd/module-main

go 1.17

require (
        github.com/sirupsen/logrus v1.8.1 // indirect
        golang.org/x/sys v0.0.0-20191026070338-33540a1f6037 // indirect
)

go.sum

每行都是由 模块路径，模块版本，哈希校验值

github.com/davecgh/go-spew v1.1.1/go.mod h1:J7Y8YcW2NihsgmVo/mv3lAwl/skON4iLHjSsI+c5H38=
github.com/pmezard/go-difflib v1.0.0/go.mod h1:iKH77koFhYxTK1pcRnkKkqfTogsbg7gZNVY4sRDYZ/4=
github.com/sirupsen/logrus v1.8.1 h1:dJKuHgqk1NNQlqoA6BTlM1Wf9DOH3NBjQyu0h9+AZZE=
github.com/sirupsen/logrus v1.8.1/go.mod h1:yWOB1SBYBC5VeMP7gHvWumXLIWorT60ONWic61uBYv0=
github.com/stretchr/testify v1.2.2/go.mod h1:a8OnRcib4nhh0OaRAV+Yts87kKdq0PP7pXfy6kDkUVs=
golang.org/x/sys v0.0.0-20191026070338-33540a1f6037 h1:YyJpGZS1sBuBCzLAR1VEpK193GlqGZbnPFnPV/5Rsb4=
golang.org/x/sys v0.0.0-20191026070338-33540a1f6037/go.mod h1:h1NjWce9XRLGQEsW7wpKNCjG9DtNlClVuFLEZdDNbEs=


https://www.cnblogs.com/wongbingming/p/12941021.html

推荐的 GO 项目结构

个人开发者：
项目.模块

团队开发：
github.com/cxd/project

github
cxd: 用户名
project: 项目名

企业开发者
github
前端/后端/基础架构组
项目
模块
*/