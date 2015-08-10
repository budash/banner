package budash.dp.ua.banner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static budash.dp.ua.banner.Constants.*;

/**
 * Created by Mikael on 08.08.2015.
 */
public class BannerTimerReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(DEBUG) Log.d(TAG, "BannerTimerReceiver intent=" + intent);
        BannerLogicVisualization logic = new BannerLogicVisualization(context);
        try {
            if (!logic.needHandle()) {
                return;
            }
            String action = intent.getAction();
            if (Intent.ACTION_USER_PRESENT.equals(action)) {
                logic.screenOn();
            } else if (ACTION_NEXT_CHECK.equals(action)) {
                logic.nextCheck();
            }

            if (logic.needShowBanner()) {
                if (Utils.launchBanner(context)) {
                    logic.bannerShowed();
                }
            }
        } finally {
            logic.endSession();
        }

    }
}
