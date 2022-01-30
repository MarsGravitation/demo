/*

15.3.2 掩码
    与运算通常跟掩码一起使用。掩码是某些为设为开而某些位设置为关的位组合。

    flags = flags * MASK;
    flags &= MASK;

    ch &= 0xff;

15.3.3 打开位
    或运算用于打开特定的位，同时保持其他位不变。

    flags = flags | MASK;
    flags |= MASK;

15.3.4 关闭位
    不影响其他位，

*/