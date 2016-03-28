package missingdrop.stb_screensaver;

import android.app.ActivityManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import missingdrop.stb_screensaver.activities.ScreenSaverActivity;


/**
 * Created by sadip on 21/03/2016.
 * Point to change before using the sample:
 * open build:gradle(Module:app)
 * paste "  useLibrary 'org.apache.http.legacy' " inside android tag
 * paste "    compile 'com.koushikdutta.ion:ion:2.+'  " inside dependency tag
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getName();

    private UserInteractionThread waiter;
    private boolean screenSaverStarted = false; // flag that knows if screensaver is on top or not
    private boolean videoPlaying = false;// flag that knows if video is being played in application so that screensaver should not be started


    public boolean getVideoPlaying() {
        return videoPlaying;
    }


    public void setVideoPlaying(boolean isVideoPlaying) {
        this.videoPlaying = isVideoPlaying;
    }

    public boolean getScreenSaverStarted() {
        return screenSaverStarted;
    }

    public void setScreenSaverStarted(boolean isScreenSaverOn) {
        screenSaverStarted = isScreenSaverOn;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting Application" + this.toString());
        waiter = new UserInteractionThread(8000);// starts checking after 8 seconds
        waiter.start();

    }

    /**
     * this method must be called from every activity in onUserInteraction() to find user inactivity
     */
    public void active() {
        waiter.active();
        setScreenSaverStarted(false);
    }

    /**
     * @return the flag that recognizes if app is on top of any other application
     */
    private boolean currentAppInForeground() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Log.e(TAG, "Current App in foreground is: " + currentApp);
        if (currentApp.equalsIgnoreCase(getApplicationContext().getPackageName())) {
            return true;
        } else {
            return false;
        }


    }

    /**
     * it is the main thread that finds user interation whole over the app
     * it keeps running even if the app is closed pressing back, couldnt figure that why?
     * but can be stopped if within the running perion the flag stop is set to true
     * shows screen saver only if the app is in foregorund
     */
    private class UserInteractionThread extends Thread {
        private long lastUsed;
        private long period;
        private boolean stop;


        public UserInteractionThread(long period) {
            this.period = period;
            stop = false;
        }

        public void run() {
            long idle = 0;
            this.active();
            Looper.prepare();
            while (!stop) {
                try {
                    Thread.sleep(8 * 1000); //checks to show screensaver after every 8 seconds
                } catch (InterruptedException e) {
                    Log.d(TAG, "Waiter interrupted!");
                }
                idle = System.currentTimeMillis() - lastUsed;
                Log.d(TAG, "Application is idle for " + idle + " ms");
                if (idle > period) {
                    idle = 0;

                    /**
                     * show screenSaver only
                     * if the app is running on foreground
                     * if screensaver is not already shown
                     * if video is not being played
                     */

                    if (currentAppInForeground() && !getScreenSaverStarted() && !getVideoPlaying()) {
                        Toast.makeText(MyApplication.this, "start screen saver", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startScreenSaver();
                        onTrimMemory(TRIM_MEMORY_BACKGROUND);
                        Log.d(TAG, "Screen saver started");
                    }

                }

            }
            /**
             * if needed to stop thread running set the flag stop here to true
             */

        }

        private synchronized void active() {
            lastUsed = System.currentTimeMillis();
        }

        /**
         * it is to show the screen saver which is shown as activity
         */
        private void startScreenSaver() {
            ArrayList<String> imagelist = new ArrayList<>();
            imagelist.add("");
            Intent i = new Intent(getApplicationContext(), ScreenSaverActivity.class);
            i.putExtra("imageList", imagelist);
            i.putExtra("delay", 3000);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }

        // for soft stopping of thread within the loop that keeps running thread on background too
        public void stopThread() {
            stop = true;
        }

    }


}
