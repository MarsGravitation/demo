package com.microwu.cxd.queue.tool;

import org.jctools.queues.MpscChunkedArrayQueue;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/17   16:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MainUsingMpscChunked {

    public static void main(String[] args) {
        MpscChunkedArrayQueue<Integer> queue = new MpscChunkedArrayQueue<Integer>(1024, 8 * 1024);
        int i = 0;
        while (queue.offer(i)) {
            i++;
        }
        System.out.println("Added " + i);
        i = 0;
        while (queue.poll() != null) {
            i++;
        }
        System.out.println("Removed " + i);
    }

}