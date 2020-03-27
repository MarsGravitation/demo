package com.microwu.cxd.adapter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:30
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class V1cPlayer implements AdvanceMediaPlayer {
    @Override
    public void playV1c(String fileName) {
        System.out.println("播放V1c：" + fileName);
    }

    @Override
    public void playMp4(String fileName) {

    }
}