package com.cxd.sd

object TestTuple {

  def main(args: Array[String]): Unit = {
    // 1. 声明元组的方式
    val tuple:(Int, String, Boolean) = (40, "bobo", true)

    // 2. 访问元组
    // 2.1 通过元素的顺序进行访问，_x
    println(tuple._1)

    // 2.2 索引
    println(tuple.productElement(0))

    // 2.3 通过迭代器访问数据
    for (elem <- tuple.productIterator) {
      println(elem)
    }

    // 3. Map 中的键值对其实就是元组
    val map = Map("a"->1)
    map.foreach(tuple=>{println(tuple._1 + "=" + tuple._2)})
  }

}
