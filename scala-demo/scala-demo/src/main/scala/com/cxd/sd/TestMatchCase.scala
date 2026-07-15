package com.cxd.sd

object TestMatchCase {
  def main(args: Array[String]): Unit = {
    var a:Int = 10
    var b:Int = 20
    var operator:Char = 'd'

    var result = operator match {
      case '+' => a + b
      case _ => "illegal"
    }

    println(result)
  }

  def describe(x: Any) = x match {
    case i:Int => "Int"
    case s:String => ""
  }
}
