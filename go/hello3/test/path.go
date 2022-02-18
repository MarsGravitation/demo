package main

import (
	"archive/zip"
	"fmt"
	"io"
	"io/fs"
	"os"
	"path/filepath"
	"strings"
	"time"
)

/*
https://mp.weixin.qq.com/s?__biz=MjM5MzU5NDYwNA==&mid=2247483686&idx=1&sn=dd9fae8d6c4954f595dd3b6c1f96179c&chksm=a695eca591e265b3213e32d18d05733c464e3e03b4e23ffdf46f9a42855bc6c55720ddc4d76e&scene=178&cur_album_id=2213476421212176389#rd

go path/filepath 提供文件路径使用操作

Walk(root string, walkFn WalkFunc) error

walk 会遍历 root 下的所有文件（包括 root）并对每一个目录和文件都调用 walkFunc 方法。在访
问文件和目录时发生的错误都会通过 error 参数传递给 WalkFunc 方法。文件时按照词法顺序进行
遍历的，这个通常输出更漂亮，但是也会导致处理非常大的目录时效率会降低。另外，Walk 函数
不会遍历符号链接。

type WalkFunc func(path string, info os.FileInfo, err error) error

WalkFunc是一个方法类型，Walk 函数在遍历文件或者目录时调用。调用时将参数传递给 path，
将 Walk 函数中的 root 作为前缀。将 root + 文件名或者目录名作为 path 传递给 WalkFunc 函数。
例如在 dir 目录下遍历到 a 文件，则 path = dir/a；info 是 path 所指文件的文件信息。如果在
遍历过程中出现了问题，传入参数 err 会描述这个问题。WalkFunc 函数可以处理这个问题，Walk
将不会再深入该目录。如果函数会返回一个错误，Walk 函数会终止运行；只有一个例外，我们也
通常用这个来跳过某些目录。当 WalkFunc 的返回值是 filepath.SkipDir 时，Walk 将会跳过这个目
录，照常执行下一个文件。

*/
func main() {
	// 将一个目录或者文件压缩为 zip 包
	oldFileName := "root.log"

	currentTime := time.Now()

	// 获取 s
	mSecond := fmt.Sprintf("%03d", currentTime.Nanosecond()/1e6)

	// zip 文件名
	zipFileName := strings.Split(oldFileName, ".")[0] + "_" + currentTime.Format("20060102150405") +
		mSecond + ".zip"

	// 压缩文件
	zipFile(oldFileName, zipFileName)
}

func zipFile(source, target string) error {
	// 创建目标 zip 文件
	zipfile, err := os.Create(target)

	if err != nil {
		fmt.Println(err)
		return err
	}
	defer zipfile.Close()

	// 创建一个写 zip 的 writer
	archive := zip.NewWriter(zipfile)
	defer archive.Close()

	return filepath.Walk(source, func(path string, info fs.FileInfo, err error) error {
		if err != nil {
			return err
		}

		// 将文件或者目录信息转换为 zip 格式的文件信息
		header, err := zip.FileInfoHeader(info)
		if err != nil {
			return err
		}

		if !info.IsDir() {
			// 确定采用的压缩算法
			header.Method = zip.Deflate
		}

		header.SetModTime(time.Unix(info.ModTime().Unix(), 0))

		// 文件或者目录名
		header.Name = path

		// 创建在 zip 内的文件或者目录
		writer, err := archive.CreateHeader(header)
		if err != nil {
			return err
		}

		// 如果是目录，只需创建无需其他操作
		if info.IsDir() {
			return nil
		}

		file, err := os.Open(path)
		if err != nil {
			return err
		}
		defer file.Close()

		// 将待压缩文件拷贝给 zip 内文件
		_, err = io.Copy(writer, file)

		return err
	})
}