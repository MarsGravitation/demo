package function

/*
当 panic 异常发生时，程序会中断运行，并立即执行在该 goroutine 中被延迟的函数（defer机制）。
随后，程序崩溃并输出日志信息。

 */
