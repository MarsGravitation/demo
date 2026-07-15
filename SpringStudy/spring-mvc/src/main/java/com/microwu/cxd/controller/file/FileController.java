package com.microwu.cxd.controller.file;

/**
 * Description: 文件续传
 *
 * https://mp.weixin.qq.com/s?__biz=MzI4ODQ3NjE2OA==&mid=2247499642&idx=1&sn=ac1b388b915318c79ebed6d740b79072&chksm=ec3f621ddb48eb0b7f35b4191125ba4058329bd96fb0016fc2467fe8eedce5eef059f7d0cd3f&scene=178&cur_album_id=1880698411432116226#rd
 *
 * 秒传
 * 1. 什么是秒传
 *  MD5 校验
 * 2. 核心逻辑
 *  a. redis -> key: md5, value: isFinish
 *  b. 如果没有上传完成，需要保存块号文件记录的路径，key: md5 + 固定前缀，value: 块号文件记录路径
 *
 * 分片上传
 * 1. 分片上传
 *  就是将所要上传的文件，按照一定的大小，将整个文件分割成多个数据块（Part）
 *  来进行分别上传，上传完之后再由服务器对所有上传的文件进行汇总整合成原始的文件。
 *
 * 断点续传
 * 1. 断点续传
 * 2. 应用场景
 * 3. 核心逻辑
 * 4. 步骤
 *  a. 常规步骤
 *      - 将需要上传的文件按照一定的分割规则，分割成相同大小的数据块
 *      - 初始化一个分片上传任务，返回本次分片上传唯一标识
 *      - 按照一定策略（串行或兵线）发送各个分片数据块
 *      - 发送完成后，服务端根据判断数据上传是否完整，如果完整，则进行数据块
 *      合成得到原始文件
 *  b. 本文实现的步骤
 *      - 前端需要根据固定大小对文件进行分片，请求后端时要带上分片序号和大小
 *      - 服务端创建 conf 文件用来记录分块位置，conf 文件长度为总分片数，
 *      没上传一个分块即想 conf 文件中写入一个 127，那么没上传的位置就是
 *      默认的 0，已上传的就是 127
 *      - 服务器按照请求数据中给的分片序号和每片分块大小算出开始位置，与读取
 *      到的文件片段数据，写入文件
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/9/1   9:58
 * Update History:
 * Author        Time            Content
 */
public class FileController {
}
