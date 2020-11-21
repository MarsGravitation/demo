package com.microwu.callback;

/**
 * Description: 回调函数
 *  回调函数和应用处于同一个抽象层。回调是高层调用底层，底层再回过头来调用高层
 *  这里面是一个三方的关系：回调函数、中间函数（接受一个回调函数）、起始函数（主函数）
 *
 * https://www.zhihu.com/question/19801131
 *
 * 在 C 或者 C++ 的定义：程序在调用函数时，将自己的函数的地址作为参数传递给程序调用的函数
 * Java 中没有指针，一般采用接口回调实现：把实现某一接口的类创建创建的对象的引用赋给该接口声明的接口变量，那么该接口变量就可以调用被类实现的接口的方法。
 * 实现回调原理：首先创建一个回调函数，然后再创建一个控制器对象，将回调对象需要被调用的方法告诉控制器对象。控制器对象负责检查某个场景是否出现或者某个条件
 * 是否满足，当此场景出现或者此条件满足时，自动调用回调函数方法。
 *
 * 举例：借书，但是被其他人借走了，读者将手机号告诉管理员，当书被归还时给读者打电话
 * 读者是回调对象，管理员是控制器对象，电话号码是回调对象方法
 *
 * https://www.cnblogs.com/leon19870907/articles/2024077.html
 *
 * Java 回调函数解读
 * 模块间调用：
 *  同步调用：a -> b，a 调用 b 方法，一直等待到 b 方法执行完毕。这种调用方式适用于 b 执行时间不长的情况
 *  异步调用：异步调用是为了解决调用中可能出现的阻塞。a 通过新启线程的方式调用 b 方法，代码接着往下执行，但是无法获取执行结果。必须通过一定的方式对 b 方法执行结果进行监听。
 *  在 Java 中，可以使用 Future + Callable 做到这一点
 *  回调：回调的思想是 a 调用 b 的方法，b 方法执行完毕主动调用 a 的 callback 方法
 *
 *  案例：老师调用学生接口，向学生提问，学生解决完调用老师的回调方法
 *  使用第二种方式解读：老师是回调对象，学生是控制器对象，老师有一个回调方法
 *  回调的核心就是将本身即 this 传递给调用方
 *
 * https://www.cnblogs.com/shenwen/p/9046482.html
 *
 * 异步回调
 *
 * https://www.cnblogs.com/Ming8006/p/11351647.html#o
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   9:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CallbackTest {
    public static void main(String[] args) {
        Ricky ricky = new Ricky();
        Teacher teacher = new Teacher(ricky);

        teacher.askQuestion();
    }
}