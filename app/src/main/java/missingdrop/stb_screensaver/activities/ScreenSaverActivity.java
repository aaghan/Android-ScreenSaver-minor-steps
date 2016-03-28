package missingdrop.stb_screensaver.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;


/**
 * Created by sadip_000 on 16/03/2016.
 */
public class ScreenSaverActivity extends Activity {
    ImageView imgScreenSaver;
    FrameLayout screenSaverLayout;
    Handler screenSaverHandler;
    Runnable screenSaverRunnable;
    TextView txtTime;
    MyApplication serUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        Toast.makeText(this, "ScreenSaverActivity", Toast.LENGTH_SHORT).show();
        getApp().setScreenSaverStarted(true);
        screenSaverLayout = (FrameLayout) findViewById(R.id.layout_screen_saver);
        imgScreenSaver = (ImageView) findViewById(R.id.img_screenSaver);
        txtTime = (TextView) findViewById(R.id.text_time);
        Bundle bundle = getIntent().getExtras();
        imgScreenSaver.setImageResource(R.drawable.screensaver);
        changePositionForTextView();
//        repeatScreenSaver(bundle.getStringArrayList("imageList"), bundle.getInt("delay"));
//        repeatScreenSaver("",bundle.getInt("delay"));
        killBackgroundApps();
    }

    public MyApplication getApp() {
        return (MyApplication) this.getApplication();
    }

    private void changePositionForTextView() {
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(txtTime.getLayoutParams());
        final Random random = new Random();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (getApp().getScreenSaverStarted()) {
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    params.setMargins(random.nextInt(500), random.nextInt(500),
                                      random.nextInt(200), random.nextInt(200));
                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            txtTime.setLayoutParams(params);
                            Log.d(".ScreenSaverActivity", "layoutParams new set");
                        }
                    }));


                }

            }
        });

        t.start();

    }

    /**
     * kills backgeound process from pid as well packageName
     */
    private void killBackgroundApps() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        //for loop to kill all process except currentApp
        for (ActivityManager.RunningAppProcessInfo process : tasks) {
            if (process.pid != tasks.get(0).pid) {
                android.os.Process.killProcess(process.pid);
            }
        }
        for (ApplicationInfo packageInfo : getPackageManager().getInstalledApplications(0)) {
            if (packageInfo.packageName != getPackageName()) {
                am.killBackgroundProcesses(packageInfo.packageName);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getApp().setScreenSaverStarted(false);
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getApp().active();

//        ((MyApplication) this.getApplicationContext()).setUserInteractionThread(8000);
//        ((MyApplication) this.getApplicationContext()).startThread();


    }

    private void repeatScreenSaver(final ArrayList<String> imageList, final int milliseconds) {

        screenSaverHandler = new Handler();
        screenSaverRunnable = new Runnable() {
            @Override
            public void run() {

                if (imageList.size() > 1)
                    Ion.with(ScreenSaverActivity.this)
                            .load(imageList.get(
                                          new Random().nextInt(imageList.size()
                                          )
                                  )
                            )
                            .withBitmap()
                            .error(R.drawable.screensaver)
                            .intoImageView(imgScreenSaver);
                else
                    imgScreenSaver.setImageResource(R.drawable.screensaver);
                screenSaverHandler.postDelayed(this, milliseconds);


            }
        };
        screenSaverHandler.postDelayed(screenSaverRunnable, milliseconds);
    }
}
