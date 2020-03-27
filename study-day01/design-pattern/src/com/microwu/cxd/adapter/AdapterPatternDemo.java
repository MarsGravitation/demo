package com.microwu.cxd.adapter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:38
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AdapterPatternDemo {
    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "光辉岁月");
        audioPlayer.play("mp4", "空城");
    }
}