package com.cxd.sd

object TestArray {

  def main(args: Array[String]): Unit = {
    // 1. 数组定义
    val arr01 = new Array[Int](4)

    // 2. 数组赋值
    // 2.1 修改某个元素的值
    arr01(3) = 10
    // 2.2 采用方法的形式给数组赋值
    // index, value
    arr01.update(1, 2)

    // 3. 遍历数组
    // 3.1 查看数组
    println(arr01.mkString(","))

    // 3.2 普通遍历
    for (i <- arr01) println(i)

    // 3.3 简化遍历
    def printx(elem:Int): Unit = {
      println(elem)
    }
    arr01.foreach(printx)

    // 4. 增加元素 - 不可变数组，其实是产生新的数组

    // 加号在后面，添加到末尾，加号在前面添加到首位
    val ints: Array[Int] = arr01 :+ 5
    println(ints.mkString(","))
  }

}
