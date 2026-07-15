package com.cxd.sd

object TestList {

  def main(args: Array[String]): Unit = {
    // 1. List 默认为不可变集合
    // 2. 创建一个 list - 数据有顺序，可重复
    val list: List[Int] = List(1,2,3,4,5)

    // 7. 空集合 Nil
    var list5 = 1::2::3::4::Nil

    // 4. List 增加数据
    // 4.1 :: 的运算规则从右到左
//    var list1 = 6::list
    // :: 是添加到首位
    val list1 = 7::6::list
    // 4.2 添加到第一个元素位置
    val list2 = list.+:(6)

    // 5. 集合间合并，将一个整体拆成一个一个的个体，称为扁平化
    val list3 = List(8,9)
    val list31 = List(1,2)
    val list4 = list3:::list31

    // 6. 取指定数据
    println(list(1))

    // 3. 遍历
    list4.foreach(println)
  }

}
