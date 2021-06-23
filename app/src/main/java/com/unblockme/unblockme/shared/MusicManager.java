package com.unblockme.unblockme.shared;

import android.content.Context;
import android.media.MediaPlayer;

import com.unblockme.unblockme.R;

public class MusicManager  {
    private static   MediaPlayer player ,blockMove,winLevel;

    private volatile boolean running = true;
    public MusicManager(Context context)
    {
        player=MediaPlayer.create(context, R.raw.victorymusic);
        blockMove=MediaPlayer.create(context,R.raw.blockmoveeffect);
        winLevel=MediaPlayer.create(context,R.raw.winmusiceffect);

    }
public static void startEffect(boolean allowMusic , int musicType){
      if(allowMusic)
      {
          switch (musicType)
          {
              case 1:
                  winLevel.setLooping(false);
                  winLevel.setVolume(100, 100);
                  winLevel.start();
                  break;
              case 2:
                  blockMove.setLooping(false);
                  blockMove.setVolume(100, 100);
                  blockMove.start();
                  break;
          }
      }
}


    public void pauseMusic() {
        running = false;
        player.pause();
    }

    public void startMusic() {
        player.setLooping(true);
        player.setVolume(100, 100);
        player.start();
        running = true;

    }
    public void refreshTheMusic(){
        player.start();
    }
}
