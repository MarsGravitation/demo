package com.microwu.cxd.adapter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:35
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AudioPlayer implements  MediaPlayer {
    MediaPlayer mediaPlayer;
    @Override
    public void play(String audiType, String fileName) {
        if(audiType.equalsIgnoreCase("mp3")){
            System.out.println("播放MP3：" + fileName);
        }else if(audiType.equalsIgnoreCase("v1c") || audiType.equalsIgnoreCase("mp4")){
            mediaPlayer = new MediaAdapter(audiType);
            mediaPlayer.play(audiType, fileName);
        }else{
            System.out.println("无效的音乐播放器");
        }
    }
}