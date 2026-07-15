package com.cxd.sd

object TestMap {

  def main(args: Array[String]): Unit = {
    // 1. 创建不可变集合 Map
    val map = Map("a"->1, "b"->2)

    // 3. 访问数据
    for (elem <- map) {
      println(elem._1 + "," + elem._2)
    }

    // 4. 如果 key 不存在，返回 0
    // get 返回 Option，有值 Some，无值 None
    println(map.get("d"))

    // 2. 循环打印
    map.foreach((kv)=>{println(kv)})
  }

}
