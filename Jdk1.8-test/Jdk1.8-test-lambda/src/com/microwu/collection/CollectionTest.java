package com.microwu.collection;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.PriorityQueue;

/**
 * Description:
 *  单线程集合：不支持多线程的集合
 *  List
 *  ArrayList：ArrayList 访问元素的时间开销固定。在尾部添加元素成本低（为常数复杂度），而在头部添加元素成本很高（线性复杂）。
 *      这是由 ArrayList 的实现原理 - 所有的元素从角标 0 开始一个接着一个排列造成的。也就是说，从要插入的元素位置往后，
 *      每个元素都要向后移动一个位置。CPU 缓存友好的集合是基于数组的
 *  LinkedList：
 *  Qeque： 每个节点都保存这上一个节点和下一个节点的指针。这就意味着数据的存取和更新具有线性复杂度（这也是一个最佳化的实现，
 *      每次操作都不会遍历数组一半以上，操作成本最高的元素就是中间的元素）。如果想写出一个搞笑的 LinkedList 代码可以使用
 *      List Iterators。如果你想用一个 Queue/Deque 实现 - ArrayDeque
 *
 *  Queues/deques
 *  ArrayDeque
 *      Deque 是基于有首尾指针的数组（环形缓冲区）实现的。和 LinkedList 不同，这个类没有实现 List 接口。因此，如果没有首尾元素的话，
 *      不能取出任何元素。这个类要比 LinkedList 要好一些，因为它产生的垃圾数量较少（在扩展的时候旧的数组会被丢弃）
 *  Stack
 *      后进先出的队列，使用 Deque 来代替 - ArrayDequeue
 *  PriorityQueue：
 *      一个基于优先级的队列。使用自然顺序或者指定的比较器来比较。poll/peek/remove/element 会返回一个队列的最小值。不仅如此，PriorityQueue
 *      还实现了 Iterable 接口，队列迭代时不进行排序（或者其他顺序）。在需要排序的集合中，使用这个队列会比 TreeSet 等其他队列要方便
 *
 *  Map
 *  HashMap：最常用的 Map 实现。只是将一个键和值相对应，get/put 有固定的复杂度
 *  EnumMap：枚举类型作为键值的 Map，因为键的数量固定，所以在内部用一个数组存储对应的值。通常来说，效率要高于 HashMap
 *  HashTable：
 *  IdentityHashMap：它使用 == 来判断。这个特性使得此集合在遍历图标的算法中非常实用 --- 可以方便的在 IdentityHashMap 中存储处理过的节点以及相关数据
 *  LinkedHashMap：HashMap 和 LikedList 的结合，所有元素的插入顺序存储在 LinkedList。这就是为什么迭代 LinkedHashMap 的条目总是遵循插入顺序。
 *      在 JDK 中，这是元素消耗内存最大的集合
 *  TreeMap：一种基于已排序且带导向信息 Map 的红黑树。每次插入会按照自然顺序或者给定的比较器排序。这个 Map 需要实现 equals 和 Comparable
 *  WeakHashMap：这种 Map 通常用在数据缓存中。它将键存储在 WeakReference 中，也就是说，如果没有强引用指向键对象的话，这些键就可以被垃圾回收线程回收。
 *
 *  Sets
 *  HashSet：基于 HashMap 的 Set 实现。其中，所有的值为“假值”。这个数据结构会消耗更多不必要的内存
 *  EnumSet：
 *  BitSet：
 *  LinkedHashMap：
 *  TreeSet：
 *
 * 并发集合：
 *  Lists：
 *  CopyOnWriteArrayList：每次更新都会产生一个新的隐含数组副本，所以这个操作成本很高。通常用在遍历操作比更新操作多的集合
 *
 *  Queues/Deques:
 *  ArrayBlockingQueue：基于数组实现的一个有界阻塞队列，大小不能重新定义。所以当你试图想一个满的队列添加元素的时候，就会收到阻塞，
 *      直到另一个方法从队列中取出元素。
 *  ConcurrentLinkedDeque/ConcurrentLinkedQueue：基于链表实现的无界队列，添加元素不会阻塞。但是要求这个集合的消费者和生产者速度一致，
 *      不然内存就会耗尽。严重依赖于 CAS 操作
 *  DelayQueue：无界的保存 Delayed 元素的集合。元素只有在延时已经过期的时候才能被取出。队列的第一个元素延期最小（包含负值 --- 延时已经过期）。
 *      当你要实现一个延期任务的队列时候，使用 ScheduledThreadPoolExecutor
 *  LinkedBlockingDeque/LinkedBlockingQueue：可选择有界或者无界基于链表的实现。在队列为空或者满的情况下使用 ReentrantLock - s
 *  LinkedTransferQueue： 基于链表的无界队列。除了通常的队列操作，它还有一系列的 transfer 方法，可以直接让生产者给等待的消费者传递消息，这样就不用将
 *      元素存储到队列中了。这是一个基于 CAS 操作的无锁集合
 *  PriorityBlockingQueue：PriorityQueue 的无锁版本
 *  SynchronousQueue：基于跳跃列表（Skip List）的ConcurrentNavigableMap实现。本质上这种集合可以当做一种TreeMap的线程安全版本来使用。
 *
 *  Sets
 *  ConcurrentSkipListSet：使用 ConcurrentSkipListMap来存储的线程安全的Set。
 *  CopyOnWriteArraySet：使用CopyOnWriteArrayList来存储的线程安全的Set。
 *
 *  Maps
 *  ConcurrentHashMap：get 操作全并发访问，put 操作可配置并发操作的哈希表。并发的级别可以通过构造函数中的 concurrentLevel 参数设置，该参数会在 Map 内部划分
 *      一些分区。在 put 操作的时候只有更新的分区是锁住的。
 *  ConcurrentSkipListMap：
 *
 *
 * https://houbb.github.io/2019/01/20/juc-00-index
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/12   15:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CollectionTest {

    public static void test() {
        ArrayQueue<Integer> integers = new ArrayQueue<Integer>(10);
        integers.add(1);
    }

    public static void test02() {
        PriorityQueue<Integer> integers = new PriorityQueue<>();
        integers.add(5);
        integers.add(4);
        integers.stream().forEach(System.out::println);
    }

    public static void main(String[] args) {
        test();
    }

}