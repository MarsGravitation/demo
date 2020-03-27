package com.microwu.cxd.adapter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:31
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MediaAdapter implements MediaPlayer {
    AdvanceMediaPlayer advanceMediaPlayer;

    public MediaAdapter(String audioType) {
        if(audioType.equalsIgnoreCase("v1c")){
            advanceMediaPlayer = new V1cPlayer();
        }else if(audioType.equalsIgnoreCase("mp4")){
            advanceMediaPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audiType, String fileName) {
        if(audiType.equalsIgnoreCase("v1c")){
            advanceMediaPlayer.playV1c(fileName);
        }else if(audiType.equalsIgnoreCase("mp4")){
            advanceMediaPlayer.playMp4(fileName);
        }
    }
}