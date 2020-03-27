package com.microwu.cxd.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 * Channel 和 Buffer
 *  基本上, 所有的IO在NIO 都从一个Channel 开始, Channel有点像流. 数据可以从Channel 读到Buffer中, 也可以从Buffer 写到Channel中
 *
 *  FileChannel, DatagramChannel, SocketChannel, ServerSocketChannel
 *  ByteBuffer, CharBuffer, DoubleBuffer, FloatBuffer, IntBuffer, LongBuffer, ShortBuffer
 *
 *  Selector
 *  Selector 允许单个线程处理多个Channel. 如果你的应用打开了多个连接, 但是每个连接的流量都很低, 使用Selector 就会很方便.
 *  要想使用Selector, 得向Selector 注册Channel, 然后调用它的select()方法. 这个方法一直阻塞道某个注册的通道有时间就绪. 一旦
 *  这个方法返回, 线程就可以处理这些时间, 例子: 新连接进来, 数据接受等
 *
 *  Java NIO 的通道类似流, 但又有些不同:
 *      1. 既可以从通道中读取数据, 又可以写数据到通道中. 但流的读写通常是单向的
 *      2. 通道可以异步的读写
 *      3. 通道中的数据总是要先读到一个Buffer, 或者从一个Buffer中写入
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/19   9:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NioMain {
    public static void main(String[] args) throws IOException {
        test();

    }

    /**
     * FileChannel 读取数据
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/19  9:51
     *
     * @param
     * @return  void
     */
    public static void test() throws IOException {
        RandomAccessFile file = new RandomAccessFile("static/a.txt", "rw");
        FileChannel channel = file.getChannel();
        // 创建一个48 byte 的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(48);

        int read = channel.read(buf);
        while(read != -1) {
            System.out.println("read: " + read);
            // 把数据从Channel写入Buffer
            // 反转
            // 从Buffer 中读取数据
            // 切换到读模式
            buf.flip();

            while(buf.hasRemaining()) {
                // 一次读1 byte
                System.out.println((char) buf.get());
            }
            buf.clear();
            read = channel.read(buf);
        }

        file.close();

    }

    /**
     * Buffer 读写数据一般遵循四个步骤:
     *  1. 写入数据到Buffer
     *  2. 反转, flip() 方法
     *  3. 从Buffer 中读取数据
     *  4. 调用clear()/compact()方法
     *
     *  当向buffer 写入数据时, buffer 会记录下写了多少数据. 一旦要读取数据, 需要通过flip()
     *  将Buffer 从写模式切换到读模式. 在读模式下, 可以读取之前写入到buffer的所有数据
     *
     *  一旦读完了所有数据, 就需要清空缓冲区, 让他可以再次被写入. 有两种方式能清空缓冲区: 调用clear()
     *  或者compact(). clear 方法会清空整个缓冲区. compact()方法只会清除已经度过的数据. 任何未读的数据
     *  都被移到缓冲区的起始处, 新写入的数据将放倒缓冲区未读数据的后面
     *
     *  缓冲区本质上是一块可以写入数据, 然后可以从中读取数据的内存. 这块内存被包装成NIO Buffer对象, 并提供
     *  一组方法, 来方便的访问该块的内存
     *
     *  capacity, position, limit
     *
     *  position 和limit 取决于buffer 处于读模式还是写模式
     *  capacity 是buffer的固定大小, 一旦满了, 就需要清空数据才能继续写
     *
     *  position: 当你写数据到buffer 时, position 表示当前的位置. 初始为0, 但写入一个数据后, position 会先前
     *          移动个单位. 最大为-1
     *          但读数据时, 也是从某个特定位置读. 从写模式切换到读模式, position 会被重置为0. 当读取一个数据时,
     *          position移动到下一个可读位置
     *
     * limit: 在写模式下, limit表示你最多能写多少数据. 写模式下, limit = capacity
     *      当切换到读模式, limit表示你最多能读到多少数据.
     *
     *      Buffer 对应 不同的数据类型, 可以通过short, char等从左缓冲区的字节
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/19  10:08
     *
     * @param
     * @return  void
     */
    public static void test02() throws IOException {
        RandomAccessFile file = new RandomAccessFile("", "");
        FileChannel channel = file.getChannel();
        // 分配空间
        ByteBuffer buffer = ByteBuffer.allocate(48);
        // 向Buffer 写数据: 1. 从Channel写到Buffer
        channel.read(buffer); // read into buffer
        // 2. 通过Buffer的put方法写到Buffer里
        buffer.put((byte) 127);
        // buffer 从写模式切换到读模式
        // position = 0, limit = 写模式下的position
        buffer.flip();

        // 从buffer 中读取数据
        // 1. 从buffer 读取数据到Channel
        int write = channel.write(buffer);
        //2. get 从Buffer 读取数据
        byte b = buffer.get();

        // rewind - 将position = 0, limit不变, 重新读取数据

        // 一旦读完buffer 的数据, 需要buffer 再次被写入, 可以使用clear 或 compact
        // 如果调用clear 方法, position= 0, limit = capacity. 换句话说, buffer
        // 被清空了, 但是buffer的数据并未清除, 只是告诉可以从哪里写
        // 如果buffer中还有未读的数据, 将被遗忘

        // 如果还有未读数据, 可以使用compact 方法. compact 将未读数据拷贝到buffer起始处,
        // 然后将position设到最后一个未读元素后面, limit = capacity

        // mark 方法, 可以标记buffer中一个特定的position. 之后调用reset方法恢复到这position


    }

    /**
     * scatter: 指在读操作时将读取的数据写入多个buffer中
     * gather: 指在写操作时将多个buffer 的数据写入同一个Channel
     *
     * scatter/gather 常用于将传输的数据分开处理的场合, 例如传输一个
     * 有消息头和消息体组成的消息, 可以将消息体和消息头分散到不同的
     * buffer, 这样可以方便处理消息头和消息体
     *
     * 注意: 首先buffer被插入数组, 然后再将数组作为channel.read()的参数.
     *  read() 方法按照buffer 在数组中的顺序将从Channel中读取的数据
     *  写入到buffer, 当一个buffer写满后, Channel紧接着向另一个buffer中写
     *
     *  scatter 在移动下一个buffer前, 必须填满当前的buffer, 这就意味着它不适用于
     *  动态消息. 换句话说, 如果存在消息头和消息体, 消息头必须完成填充(128b),
     *  scatter 才能正常工作
     *
     *  channel.write(bytes)
     *  write 方法会按照数组顺序将数据写入, 只有position 和limit之间的数据才会被写入.
     *  这意味着gather 能较好的处理动态消息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/19  10:42
     *
     * @param
     * @return  void
     */
    public static void test03() {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);

        ByteBuffer[] buffers = {header, body};

        // channel.read(buffers)

    }

    public static void test04() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("", "");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("", "");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);

    }

    /**
     * selector: 仅使用单个线程来处理多个Channel
     *
     *         // 创建Selector
     *         Selector selector = Selector.open();
     *         // 向Selector注册通道
     *         // 为了将Channel和Selector配合使用, 必须将Channel注册到Selector上
     *         // 与Selector一起使用时, Channel必须处于非阻塞模式下, FileChannel不能切换到非阻塞模式
     *         // 而套接字通道可以
     *         SocketChannel socketChannel = new SocketChannel();
     *         socketChannel.configureBlocking(false);
     *         // 第二个参数是interest 集合儿, 意思是通过Selector监听Channel时对什么事件感兴趣
     *         // Connect - 某个Channel成功连接到服务器
     *         // Accept - 一个server socket Channel 准备好接受进入的连接
     *         // read - 一个数据可读的通道
     *         // write - 等待写数据的通道
     *         SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
     *         // 返回值包含你感兴趣的属性
     *         // interest 集合 按位 & 可以检测某个事件是否存在interest集合中
     *         // ready集合: 通道已经准备就绪的操作集合. 再一次selection之后, 你会首先访问这个ready set
     *         int readyOps = selectionKey.readyOps();
     *
     *         // 可以通过按位 &, 检测Channel什么时间已就绪, 也可以使用以下四个方法
     *         selectionKey.isAcceptable();
     *         selectionKey.isConnectable();
     *         selectionKey.isReadable();
     *         selectionKey.isWritable();
     *
     *         // 从selectionKey 访问Channel和Selector
     *         SelectableChannel channel = selectionKey.channel();
     *         Selector selector2 = selectionKey.selector();
     *
     *         // 附加对象, 可以将一个或更多信息附着到selectionKey,方便识别某个给定的通道
     *         // 例如可以附加于通道一起食用的Buffer, 或者包含聚集数据的某个对象
     *         selectionKey.attach("");
     *         Object attachment = selectionKey.attachment();
     *         // 还可以在register 方法向Selector 注册Channel附加对象
     *         SelectionKey key = channel.register(selector, SelectionKey.OP_READ, attachment);
     *
     *         // 通过Selector选择通道, 一旦向Selector注册了一个或多个通道, 当返回你所感兴趣的时间
     *         // 可以返回那些对事件感兴趣的通道
     *         // select - 阻塞到至少有一个通道在你的注册事件上就绪了
     *         // select 方法返回的int值表示有多少通道已经就绪. 自上次调用select 方法后有多少通道变成
     *         // 就绪状态. 如果调用select, 因为有一个通道变成就绪状态, 返回了1, 若再次select 方法,
     *         // 如果另一个通道就绪了, 它再次返回1. 如果对第一个就绪Channel没有任何操作, 现在就有两个
     *         // 就绪通道, 但每次select 方法调用之间, 只有一个通道就绪了
     *
     *         // selectedKeys - 一旦调用了select方法, 并且返回值表明有一个或更多通道就绪了, 然后可以通过调用
     *         // Selector的selectedKeys方法, 访问已选择键集
     *         Set<SelectionKey> selectionKeys = selector.selectedKeys();
     *         // 当向Selector注册通道时, Channel.register 方法会返回一个SelectionKey对象.
     *         // 这个对象代表了注册到该Selector的通道. 可以通过set 集合访问这些对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/19  11:05
     *
     * @param
     * @return  void
     */
    public static void test05() throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 创建Selector
        Selector selector = Selector.open();
        // 将Channel注册到Selector, 并设置为非阻塞模式
        channel.configureBlocking(false);
        // 第二个参数是interest集合, 意思是通过selector监听Channel时对什么事件感兴趣
        // 通道触发了一个事件意思是该事件已经就绪
        // 如果你对多个时间感兴趣, 使用 位或 将常量联系起来
        channel.register(selector, SelectionKey.OP_READ);
        // SelectionKey 包含了interest集合, ready集合, Channel, Selector
        // interest 包含了你感兴趣的集合
        // ready 是通道已经准备就绪的操作的集合. 在一次选择之后, 你首先会访问这个ready
        while(true) {
            // 一旦向Selector注册了通道, 这个方法返回你所感兴趣的时间并且已经准备就绪的通道
            // 阻塞到至少有一个通道在你注册的事件上就绪了
            // 返回值表示有多少通道已经就绪
            // 自上次调用select方法后有多少通道变成就绪状态
            // 如果调用select方法, 因为有一个通道变成了就绪状态,返回1
            // 若再次调用select方法, 如果另一个通道就绪了, 它会再次返回1
            // 如果对第一个就绪的Channel没有任何操作, 现在有两个通道, 但是每次select方法之间, 只有一个通道就绪了
            int readyChannels = selector.select();
            if(readyChannels == 0) {continue;}
            // 走到这里说明有一个或多个通道就绪了, 调用selectedKeys, 访问已选择键集中的就绪通道
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                // 最后必须手动移除已就绪的键集
                keyIterator.remove();
            }
        }

    }
    
    /**
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/19  14:18
     *
     * @param   	
     * @return  void
     */
    public static void test06() throws IOException {
        RandomAccessFile file = new RandomAccessFile("", "");
        // 打开Channel
        FileChannel channel = file.getChannel();

        // 从Channel中读数据

        // 首先分配一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 将数据从Channel读到Buffer中
        // 返回值表示有多少字节被读到Buffer, -1 表示末尾
        int read = channel.read(buffer);

        // 向Channel写数据
        String newData = "";

        buffer.clear();
        buffer.put(newData.getBytes());

        buffer.flip();

        // 无法保证writer一次向Channel写入多少字节, 需要循环调用
        while(buffer.hasRemaining()) {
            channel.write(buffer);
        }

        channel.close();
    }

    public static void test07() throws IOException {
        // 创建SocketChannel, 连接到TCP
        SocketChannel socketChannel = SocketChannel.open();
        // 1. 打开一个SocketChannel并连接道某台服务器
        // 2. 一个新连接到达ServerSocketChannel, 会创建一个SocketChannel
        socketChannel.connect(new InetSocketAddress("http://www.baidu.com", 80));
        // 读写模式和FileChannel一样

        // 设置非阻塞模式, 可以在异步模式下调用connect, read, write
        socketChannel.configureBlocking(false);
    }

    public static void test08() throws IOException {
        // ServerSocketChannel 监听连接进来的TCP通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
    }

    public static void test09() throws IOException {
        // DatagramChannel 是发送UDP包的通道
        // 因为UDP是无连接的网络协议, 不能想起他通道那样读取/写入
        // 它发送的是数据包
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(8080));

        // 接收数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.clear();
        // 将数据包内容复制到buffer, 如果容不下, 多出的数据被丢弃
        channel.receive(byteBuffer);

        // 发送数据
        byte[] bytes = "".getBytes();
        byteBuffer.clear();
        byteBuffer.put(bytes);
        byteBuffer.flip();

        int send = channel.send(byteBuffer, new InetSocketAddress("", 8080));
    }
}