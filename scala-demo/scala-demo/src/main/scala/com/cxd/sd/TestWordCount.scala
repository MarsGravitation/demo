package com.cxd.sd

object TestWordCount {

  def main(args: Array[String]): Unit = {
    val stringList = List("Hello Scala Hbase kafka", "Hello Scala Hbase", "Hello Scala", "Hello")

    // 1. 将每一个字符串转换成一个一个单词
    val wordList:List[String] = stringList.flatMap(str => str.split(" "))
    // 2. 将相同的单词放置在一起
    val wordToWordsMap:Map[String, List[String]] = wordList.groupBy(word => word)
    // 3. 对相同的单词进行计数
    val wordToCountMap: Map[String, Int] = wordToWordsMap.map(tuple => (tuple._1, tuple._2.size))
    // 4. 对计数完成后的结果进行排序
    val sortList: List[(String, Int)] = wordToCountMap.toList.sortWith((left, right)=>{
      left._2 > right._2
    })
    // 5. 取前 3 名
    val resultList:List[(String, Int)] = sortList.take(3)

    println(resultList)
  }

}
