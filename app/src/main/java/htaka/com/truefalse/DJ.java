package htaka.com.truefalse;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by admin on 10/15/16.
 */

public class DJ {
    private static MediaPlayer player;
    private static boolean keepMusicOn;

    public static void iAmIn(Context context){
        if (player == null){
            player = MediaPlayer.create(context, R.raw.background_music);
            player.setLooping(true);

            try{
                player.prepare();
            }
            catch (IllegalStateException e){}
            catch (IOException e){}
        }

        if(!player.isPlaying()){
            player.start();
        }

        keepMusicOn= false;
    }

    public static void keepMusicOn(){
        keepMusicOn= true;
    }

    public static void iAmLeaving(){

        if(!keepMusicOn){
            player.pause();
        }
    }
}