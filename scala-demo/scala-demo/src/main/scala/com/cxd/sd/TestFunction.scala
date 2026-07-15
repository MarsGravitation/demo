package com.cxd.sd

import scala.collection.mutable.ArrayBuffer

object TestFunction {
  def main(args: Array[String]): Unit = {
    // 函数标准写法
    def f( s : String): String = {
      return s + " 金莲"
    }
    println(f("Hello"))

    // 1. return 可以省略，使用函数体的最后一行代码作为返回值
    def f1( s : String) : String = {
      s + " 金莲"
    }

    // 2. 函数体只有一行代码，可以省略花括号
    def f2(s:String):String = s + " 金莲"

    // 3. 返回值类型如果能够推断出来，那么可以省略
    def f3(s:String) = s + "金莲"

    // 4. 如果有 return，则不能省略返回值类型

    // 5. 如果函数明确声明 unit，那么即使函数体中使用 return 关键字也不起作用
    def f5() : Unit = {
      return "大郎"
    }
    // ()
    println(f5())

    // 6. 如果期望是无返回值类型，可以省略等号
    def f6() {
      "大郎"
    }

    // 7. 如果函数无参，但是声明了参数列表，那么调用时，小括号，可加可不加
    def f7() = "大郎"

    // 8. 如果函数没有参数列表，那么小括号可以省略，调用时小括号必须省略
    def f8 = "大郎"

    // 9. 如果不关心名称，只关心逻辑处理，那么函数名 def 可以省略
    def f9 = (x:String)=>{println("武松")}

    // map 映射

    // Int => Int 如果不关心名称，只关心逻辑处理，函数名可以省略
    def map(arr: Array[Int], op: Int => Int) = {
      for (elem <- arr) yield op(elem)
    }

    val arr = map(Array(1, 2, 3, 4), (x: Int) => {
      x * x
    })
    println(arr.mkString(","))

    // 过滤。有参数，且参数在后面只使用一次，则参数省略且后面参数用 _ 表示
    def filter(arr:Array[Int], op:Int => Boolean) = {
      var arr1:ArrayBuffer[Int] = ArrayBuffer[Int]()
      for (elem <- arr if op(elem)) {
        arr1.append(elem)
      }
      arr1.toArray
    }
  }
}
