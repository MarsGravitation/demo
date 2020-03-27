package com.microwu.cxd.adapter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:31
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Mp4Player implements AdvanceMediaPlayer {
    @Override
    public void playV1c(String fileName) {

    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("播放MP4：" + fileName);
    }
}