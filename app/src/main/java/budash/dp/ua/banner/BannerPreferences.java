package budash.dp.ua.banner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Mikael on 08.08.2015.
 */
public class BannerPreferences {

    private static final Set<String> EMPTY_SET = Collections.unmodifiableSet(new HashSet<String>());

    private SharedPreferences mPreference;
    private Editor mEditor;

    public BannerPreferences(Context context) {
        mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreference.edit();
    }

    public void saveShowedTimes(Collection<Long> times){
        if(times != null && !times.isEmpty()){
            Set<String> saveTimes = new HashSet<String>();
            for(Long t : times){
                saveTimes.add(Long.toString(t));
            }
            mEditor.putStringSet("showed", saveTimes);
        }
    }

    public TreeSet<Long> loadShowedTimes(){
        TreeSet<Long> ret = new TreeSet<Long>();

        Set<String> savedTimes = mPreference.getStringSet("showed", EMPTY_SET);
        if(savedTimes != EMPTY_SET && !savedTimes.isEmpty()){
            for(String t : savedTimes){
                ret.add(Long.parseLong(t));
            }
        }
        return ret;
    }

    public void saveLastScreenOn(long time){
        mEditor.putLong("screen_on", time);
    }

    public long loadLastScreenOn(){
        return mPreference.getLong("screen_on", 0L);
    }

    public void saveLastOurAppVisible(long time){
        mEditor.putLong("our_app_visible", time);
    }

    public long getLastTimeVisibleOurApp(){
        return mPreference.getLong("our_app_visible", 0L);
    }

    public void saveChanges(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
            mEditor.commit();
        } else {
            mEditor.apply();
        }
    }
}
