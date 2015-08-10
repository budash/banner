package budash.dp.ua.banner;

/**
 * Created by Mikael on 08.08.2015.
 */
public class Constants {

    public static final String TAG = "BANNER";

    public static final String ACTION_NEXT_CHECK = "ua.dp.budash.action.NEXT_CHECK";

    public static final boolean DEBUG = false;

    public static final int NUM_BANNERS_PER_DAY = 3;

    public static final int ONE_MINUTE = DEBUG ? 20 * 1000
                                                : 60 * 1000;

    public static final int ONE_DAY = DEBUG ? 20 * 60 * 1000
                                                : 24 * 60 * ONE_MINUTE;

    public static final int TIME_SHOW_AFTER_UNLOCKING = ONE_MINUTE;

    public static final int TIME_SHOW_AFTER_SHOW_OUR_APP = 5 * ONE_MINUTE;

    public static final int MISTAKE_TIMETICK = ONE_MINUTE / 10;//of the order of

    public static final int CHECK_OUR_APP_WORK_INTERVAL = ONE_MINUTE / 30;
}
