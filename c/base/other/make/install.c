/*
https://blog.csdn.net/weixin_34198797/article/details/92511022

congifure 作用：是源码安装软件时配置环境用的，它根据你的配置选项
和你的系统情况生成 makefile 文件，为 make 做准备

最常用的参数：./configure --prefix
不指定 prefix，则可执行文件默认放在 /usr/local/bin，库文件 /usr/local/lib，
配置文件 /usr/local/etc。其他的资源文件放在 /usr/local/share。你要
卸载这个程序，要么在原来的 make 目录下用一次 make uninstall，要
么去上述目录里面把相关的文件一个一个删除。指定 prefix，直接删掉一
个文件夹就够了。

软件的安装

Linux 下软件的安装主要有两种不同的形式。第一种安装文件名为 xxx.tar.gz;
另一种安装文件名为 xxx.i386.rpm。以第一种方式发型的软件多以源码
形式发送的；第二种方式则是直接以二进制形式发送的。

对于第一种，安装方法如下：
1. cp xxx.tar.gz /root
2. tar zxvf filename.tar.gz
3. vim Install
4. ./configure
    执行解压缩后产生的一个名为 configure 的可执行脚本程序。它是用
于检查系统是否有编译时所需的库，以及库的版本是否满足编译的需要等安
装所需要的系统信息。为随后的编译工作做准备。

如果想把软件安装到指定目录，用 
./configure --prefix=/xxx

例如：
./configure --prefix=/opt/mlterm

5. 检查通过后，将生成用于编译的 Makefile 文件。此时，可以开始进行
编译了。编译的过程视软件的规模和计算性能不同，所耗费的时间也不同。

make

6. 成功编译后，键入如下的命令开始安装：

make install

7. 安装完毕，应清除编译过程中产生的临时文件和配置过程中产生的文件

make clean
make disclean

对于第二种，其安装方法要简单得多

rpm -i filename.i386.rpm

rpm 将自动将安装文件解包，并将软件安装到缺省的目录下。并将软件的
安装信息注册到 rpm 的数据库中。参数 i 的作用是 rpm 进入安装模式。

软件的卸载

1. 软件的卸载主要是使用 rpm 来进行的。卸载软件首先要知道软件包在
系统中注册的名称。

rpm -q -a

2. 确定了要卸载软件的名称，就可以开始实际卸载该软件。

rpm -e [package name]

即可卸载软件。参数 e 的作用是使 rpm 进入卸载模式。对名为 [package 
name] 的软件包进行卸载。由于系统中各个软件包之间相互有依赖关系。
如果因存在依赖关系而不能卸载，rpm 将给予提示并停止卸载。你可以使
用如下的的命令来忽略依赖关系，直接开始卸载。

rpm -e [package name] -nodeps

忽略依赖关系的卸载可能导致系统中的其他的软件无法使用

如果向知道 rpm 包安装到哪里了呢？

rpm -ql [pacange name]

3. 如何卸载用源码包安装的软件
最好是看 README 和 INSTALL；

如果安装软件时，指定了目录。

./configure --prefix=/opt/gaim
make
make install

rm -rf /opt/gaim

有些软件要遭解压安装目录中执行
make uninstall


*/