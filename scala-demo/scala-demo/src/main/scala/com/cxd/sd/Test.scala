package com.cxd.sd

object Test {
  def main(args: Array[String]): Unit = {
    // 通过伴生对象的 apply 方法，实现不使用 new 关键字创建对象
    val p1 = Person4()
  }
}

class Person4 private(cName: String) {
  var name: String = cName
}

object Person4 {
  def apply(): Person4 = {
    println("空参被调用")
  new Person4("xx")
  }

  def apply(name: String): Person4 = {
    println("有参被调用")
    new Person4(name)
  }
}

// 枚举类
object Color extends Enumeration {
  val RED = Value(1, "red")
}

// 应用类
object Test20 extends App {
  println("xxx")
}
