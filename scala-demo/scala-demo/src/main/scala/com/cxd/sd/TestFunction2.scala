package com.cxd.sd

object TestFunction2 {

  def main(args: Array[String]): Unit = {
    def f1()={
      var a:Int = 10
      def f2(b:Int)={
        a + b
      }
      // 在被调用函数 foo 后面加上 _，相当于把函数 foo 当成一个整体，传递给变量 f1
      f2 _
    }

    // 在调用时，f1 函数执行完毕后，局部变量 a 应该随着栈空间释放掉
    val f = f1()

    // 但是在此处，变量 a 其实并没有释放，而是包含在了 f2 函数的内部，形成了闭合的效果
    println(f(3))

    println(f1() (3))

    // 函数科里化，其实就是将复杂的参数逻辑变得简单化，函数科里化一定存在闭包
//    def f3()(b:Int)={
//      a + b
//    }
//    println(f3()(3))
  }

}
