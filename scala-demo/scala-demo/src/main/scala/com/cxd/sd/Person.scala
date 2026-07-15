package com.cxd.sd

// 如果主构造器无参数，小括号可以省略
class Person (){
  var name: String = _
  var age: Int = _

  def this(age: Int) {
    this()
    this.age = age
    println("辅助构造器")
  }

}

class Person2(name: String, var age: Int, val sex: String) {
  // name 局部变量
  // age 成员属性，可以修改
  // sex 只读属性，不能修改
}

class Person3 {
  val name: String = "person"

  def hello(): Unit = {
    println("hello person")
  }
}

class Teacher extends Person3 {
  override val name: String = "teacher"

  override def hello(): Unit = {
    println("hello teacher")
  }
}

object Person {
  def main(args: Array[String]): Unit = {
    var person = new Person(18)
  }
}
