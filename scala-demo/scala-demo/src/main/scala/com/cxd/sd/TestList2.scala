package com.cxd.sd

object TestList2 {

  def main(args: Array[String]): Unit = {
    val list:List[Int] = List(1,2,3,4,5,6,7,8,9)
    val nestedList: List[List[Int]] = List(List(1, 2, 3), List(4,
      5, 6), List(7, 8, 9))
    val wordList: List[String] = List("hello world", "hello atguigu", "hello scala")

    // 1. 过滤
    println(list.filter(x => x % 2 == 0))

    // 2. 转换
    println(list.map(x => x + 1))

    // 3. 扁平化 = 1 -> n
    println(nestedList.flatten)

    // 4. 扁平化 + 映射 = map + flatten
    println(wordList.flatMap(x => x.split(" ")))

    // 5. 分组
    println(list.groupBy(x => x % 2))

    // reduce
    val i:Int = list.reduce((x, y) => x - y)
    println(i)

    // 底层就是 reduceLeft

  }

}
