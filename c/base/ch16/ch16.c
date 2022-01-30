/*

16.2 明显常量 #define
    预处理器指令从 # 开始，到其后第一个换行符为止。

16.3 在 #define 中使用参数
    

16.5 文件包含
    预处理器发现 #include 指令后，就会寻找后跟的文件名并把这个文件的内容包含到当前文件中。被包
含文件中的文本将替换源代码中的 #include 智力，就像您把被包含中的全部内容键入到源文件中
的这个特定位置一样。

    #include <stdio.h> --- 搜索系统目录
    #include "hot.H" --- 搜索当前工作目录

16.6.3 条件编译
    #ifdef、#lese、#endif

    #ifdef 指令说明：如果预处理器已经定义了后面的标识符，那么执行所有指令并编译 C 代
码，知道下一个 #else 或 #endif

    #ifdef MAVIS
        #include "horse.h" // 如果已经用 #define 定义了 MAVIS，则执行这里的指令
    #else
        #include "cow.h"
    #endif

    #inndef 指令：判断后面的标识符是否未定义的

    #if #elif 指令：#if 后跟常量整数表达式

    #if SYS == 1
    #include "ibm.h"
    #endif

*/