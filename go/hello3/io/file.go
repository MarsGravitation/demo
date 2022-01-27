package io

import "os"

/*
https://www.cnblogs.com/landv/p/13140156.html

func OpenFile(name string, flag int, perm FileMode) (*File, error)

name: 文件路径
flag: 控制文件的打开方式
perm: 控制文件模式

可用的打开方式：
	O_RDONLY: 只读
	O_WRONLY: 只写
	O_RDWR: 可读可写
	O_APPEND: 追加内容
	O_CREATE: 创建文件，如果文件不存在
	O_EXCL: 与创建文件一同使用，文件必须存在
	O_SYNC: 打开一个同步的文件流
	O_TRUNC: 打开时截断常规可写文件

打开模式：
// The defined file mode bits are the most significant bits of the FileMode.
// The nine least-significant bits are the standard Unix rwxrwxrwx permissions.
// The values of these bits should be considered part of the public API and
// may be used in wire protocols or disk representations: they must not be
// changed, although new bits might be added.
const (
    // The single letters are the abbreviations
    // used by the String method's formatting.
    // 文件夹模式
    ModeDir        FileMode = 1 << (32 - 1 - iota) // d: is a directory
    // 追加模式
    ModeAppend                                     // a: append-only
    // 单独使用
    ModeExclusive                                  // l: exclusive use
    // 临时文件
    ModeTemporary                                  // T: temporary file; Plan 9 only
    // 象征性的关联
    ModeSymlink                                    // L: symbolic link
    // 设备文件
    ModeDevice                                     // D: device file
    // 命名管道
    ModeNamedPipe                                  // p: named pipe (FIFO)
    // Unix 主机 socket
    ModeSocket                                     // S: Unix domain socket
    // 设置uid
    ModeSetuid                                     // u: setuid
    // 设置gid
    ModeSetgid                                     // g: setgid
    // UNIX 字符串设备，当设备模式是设置unix
    ModeCharDevice                                 // c: Unix character device, when ModeDevice is set
    // 粘性的
    ModeSticky                                     // t: sticky
    // 非常规文件；对该文件一无所知
    ModeIrregular                                  // ?: non-regular file; nothing else is known about this file

    // bit位遮盖，不变的文件设置为none
    // Mask for the type bits. For regular files, none will be set.
    ModeType = ModeDir | ModeSymlink | ModeNamedPipe | ModeSocket | ModeDevice | ModeCharDevice | ModeIrregular
    // 权限位
    ModePerm FileMode = 0777 // Unix permission bits
)
 */

func testF1() {
	os.OpenFile("", 1, 1)
}
