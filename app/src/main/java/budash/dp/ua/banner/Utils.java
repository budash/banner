package budash.dp.ua.banner;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.util.List;

import static budash.dp.ua.banner.Constants.TAG;
import static budash.dp.ua.banner.Constants.DEBUG;

/**
 * Created by Mikael on 08.08.2015.
 */
public class Utils {

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public static boolean launchBanner(Context context){
        Intent intent = new Intent();
        intent.setClass(context, BannerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "no can start banner activity", e);
            return false;
        }
    }

    public static boolean isScreenOn(Context context){
        PowerManager powerManager=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH){
            return powerManager.isScreenOn();
        } else {
            return powerManager.isInteractive();
        }
    }

    //TODO support LOLLIPOP and higher
    public static boolean isShowOurApp(Context context){
        String packName = context.getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if(DEBUG) {
            Log.d(TAG, "CURRENT Activity ::" + taskInfo.get(0).topActivity);
        }
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if(packName.equals(componentInfo.getPackageName())){
            return true;
        } else {
            return false;
        }
    }
}
