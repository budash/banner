package budash.dp.ua.banner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.TreeSet;

import static budash.dp.ua.banner.Constants.*;

import static budash.dp.ua.banner.Utils.*;

/**
 * Created by Mikael on 08.08.2015.
 */
public class BannerLogicVisualization {

    private BannerPreferences mBannerPreferences;
    private Context mContext;
    private long mCurrTime;
    private boolean mNextCheck;
    private TreeSet<Long> mLastVisibleBannerTimes;
    private long mLastScreenOn;
    private boolean mIsShowOurApp;
    private long mTimeLastOurAppVisible;
    private boolean mNeedHandle;

    public BannerLogicVisualization(Context context){
        mBannerPreferences = new BannerPreferences(context);
        mContext = context;
        mCurrTime = getCurrentTime();
        mLastScreenOn = mBannerPreferences.loadLastScreenOn();
        mIsShowOurApp = isShowOurApp(context);
        if(mIsShowOurApp){
            mTimeLastOurAppVisible = mCurrTime;
            mBannerPreferences.saveLastOurAppVisible(mTimeLastOurAppVisible);
        } else {
            mTimeLastOurAppVisible = mBannerPreferences.getLastTimeVisibleOurApp();
        }
        mLastVisibleBannerTimes = mBannerPreferences.loadShowedTimes();
        removeOldData();
        mNeedHandle = mLastVisibleBannerTimes.size() < NUM_BANNERS_PER_DAY;
    }

    public boolean needHandle(){
        return mNeedHandle;
    }

    public void screenOn(){
        mBannerPreferences.saveLastScreenOn(mCurrTime);
        mLastScreenOn = mCurrTime;
    }

    public void nextCheck(){
        mNextCheck = true;
    }

    public boolean needShowBanner(){
        if(mIsShowOurApp){
            if(DEBUG) Log.d(TAG, "mIsShowOurApp");
            return false;
        }
        if(!mNextCheck){
            if(DEBUG) Log.d(TAG, "!mUseTimeTick");
            return false;
        }
        if(!isScreenOn(mContext)) {
            if(DEBUG) Log.d(TAG, "screen off");
            return false;
        }
        if(!timeCome(mTimeLastOurAppVisible + TIME_SHOW_AFTER_SHOW_OUR_APP)){
            if(DEBUG) Log.d(TAG, "time after our app is not coming");
            return false;
        }
        if(!timeCome(mLastScreenOn + TIME_SHOW_AFTER_UNLOCKING)){
            if(DEBUG) Log.d(TAG, "time after screen on is not coming");
            return false;
        }
        if(mNeedHandle){
            return true;
        }
        return false;
    }

    private boolean timeCome(long borderTime){
        return borderTime < mCurrTime
            || Math.abs(borderTime - mCurrTime) < MISTAKE_TIMETICK;
    }

    private void removeOldData(){
        if(mLastVisibleBannerTimes.isEmpty()){
            return;
        }
        long first = mLastVisibleBannerTimes.first();
        if(first + ONE_DAY < mCurrTime){
            mLastVisibleBannerTimes.remove(first);
            removeOldData();
        }
    }

    public void bannerShowed(){
        mNeedHandle = false;
        mLastVisibleBannerTimes.add(mCurrTime);
    }

    public void endSession(){
        resendNextAlarm();
        mBannerPreferences.saveShowedTimes(mLastVisibleBannerTimes);
        mBannerPreferences.saveChanges();
    }

    private void resendNextAlarm(){
        if(mNeedHandle){
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = buildPendingIntent();
            alarmManager.cancel(pi);
            long nextTime = mCurrTime;
            if(!mIsShowOurApp){
                long interval = mTimeLastOurAppVisible + TIME_SHOW_AFTER_SHOW_OUR_APP - mCurrTime;
                if(interval < 0){
                    interval = mLastScreenOn + TIME_SHOW_AFTER_UNLOCKING - mCurrTime;
                }
                nextTime += interval;
            } else {
                nextTime += CHECK_OUR_APP_WORK_INTERVAL;
            }
            alarmManager.set(AlarmManager.RTC, nextTime, pi);
        }
    }

    private PendingIntent buildPendingIntent(){
        Intent intent = new Intent(ACTION_NEXT_CHECK);
        intent.setClass(mContext, BannerTimerReceiver.class);
        return PendingIntent.getBroadcast(mContext, 0, intent, 0);
    }
}
