package main

import (
	"encoding/json"
	"sync"
)

type Student struct {
	Name   string
	Age    int32
	Remark [1024]byte
}

var buf, _ = json.Marshal(Student{Name: "cxd", Age: 28})

func unmarsh() {
	stu := &Student{}
	json.Unmarshal(buf, stu)
}

// 对象重用机制
// sync.Pool 是可伸缩的，同时也是并发安全的，其大小仅受限于内存的大小。
// sync.Pool 用于存储那些被分配了但是没有被使用，而未来可能会使用
// 的值。这样就可以不用再次经过内存分配，可直接复用已有对象，减轻 GC
// 压力，从而提升系统的性能

// sync.Pool 的大小是可伸缩的，高负载时会动态扩容，存放在池中的对象
// 如果不活跃了会被自动清理

var studentPool = sync.Pool{
	New: func() interface{} {
		return new(Student)
	},
}

// Get & Put

// Get 用于从对象池中获取对象，因为返回值是 interface{}，因此需要类型转换
// stu := studentPool.Get().(*Student)

// Put 则是在对象使用完毕后，返回对象池
// studentPool.Put(stu)
