package missingdrop.stb_screensaver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Random;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;


/**
 * Created by sadip_000 on 16/03/2016.
 */
public class ScreenSaverActivity extends Activity {
    ImageView imgScreenSaver;
    LinearLayout screenSaverLayout;
    Handler screenSaverHandler;
    Runnable screenSaverRunnable;
    MyApplication serUtil;
    public MyApplication getApp(){
        return (MyApplication)this.getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        screenSaverLayout = (LinearLayout) findViewById(R.id.layout_screen_saver);
        imgScreenSaver = (ImageView) findViewById(R.id.img_screenSaver);
//        Bundle bundle = getIntent().getExtras();
        imgScreenSaver.setImageResource(R.drawable.screensaver);
//        repeatScreenSaver(bundle.getStringArrayList("imageList"), bundle.getInt("delay"));
//        repeatScreenSaver("",bundle.getInt("delay"));
    }

    private void repeatScreenSaver(final ArrayList<String> imageList, final int milliseconds) {

        screenSaverHandler = new Handler();
        screenSaverRunnable = new Runnable() {
            @Override
            public void run() {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgScreenSaver.getLayoutParams());
//                Random random = new Random();
//
//                params.setMargins(random.nextInt(500), random.nextInt(500),
//                        random.nextInt(200), random.nextInt(200));
//                imgScreenSaver.setLayoutParams(params);
                Ion.with(ScreenSaverActivity.this)
                        .load(imageList.get(
                                        new Random().nextInt(imageList.size()
                                        )
                                )
                        )
                        .withBitmap()
                        .error(R.drawable.screensaver)
                        .intoImageView(imgScreenSaver);
                imgScreenSaver.setImageResource(R.drawable.screensaver);
                screenSaverHandler.postDelayed(this, milliseconds);

            }
        };
        screenSaverHandler.postDelayed(screenSaverRunnable, milliseconds);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getApp().active();
        getApp().setScreenSaverStarted(false);
        finish();
//        ((MyApplication) this.getApplicationContext()).setUserInteractionThread(8000);
//        ((MyApplication) this.getApplicationContext()).startThread();



    }


}
