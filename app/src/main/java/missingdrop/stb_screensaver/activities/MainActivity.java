package missingdrop.stb_screensaver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;
import missingdrop.stb_screensaver.Utils.DownloadUtil;


public class MainActivity extends AppCompatActivity {
    LinearLayout screenSaverLayout;
    ImageView imgScreenSaver;
    ArrayList<String> bannerlinks = new ArrayList<>();
    Handler stopShowingHandler;
    Runnable stopShowingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenSaverLayout = (LinearLayout) findViewById(R.id.layout_screen_saver);
        imgScreenSaver = (ImageView) findViewById(R.id.img_screensaver);
        populateListView();

    }

    private void populateListView() {
        String[] listItems = new String[]{"Screen Saver Test apk", "ScreenSaver that is rejected", "other temp activities"};
        ArrayList<String> myAccountArray = new ArrayList<String>();
        myAccountArray.addAll(Arrays.asList(listItems));
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.list_row_main,
                myAccountArray);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:

                        new LoadScreenSaverBanners().execute();

                        break;
                    case 2:
                        otherTempActivities();
                    default:
                        break;

                }
            }
        });
    }

    private void otherTempActivities() {
        Intent i = new Intent(MainActivity.this, TempActivity2.class);
        startActivity(i);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        ((MyApplication) this.getApplicationContext()).active();
        try {
            stopShowingHandler.removeCallbacks(stopShowingRunnable);
            screenSaverLayout.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    private class LoadScreenSaverBanners extends AsyncTask<Void, Void, String> {
        ProgressDialog loading;
        String url = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MainActivity.this);
            loading.setMessage("Loading ...");
            loading.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            return new DownloadUtil().downloadStringContent(url);
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            loading.dismiss();
            try {
                JSONObject jo = new JSONObject(value);
                JSONArray images = jo.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {

                    bannerlinks.add(images.getJSONObject(i).getString("image"));

                }
            } catch (JSONException e) {
                bannerlinks.add("");
                e.printStackTrace();
            }
            stopShowingHandler = new Handler();
            stopShowingRunnable = new Runnable() {
                @Override
                public void run() {
                    screenSaverLayout.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgScreenSaver.getLayoutParams());
                    Random random = new Random();

                    params.setMargins(100, random.nextInt(200),
                            random.nextInt(70), random.nextInt(70));
                    imgScreenSaver.setLayoutParams(params);

                    imgScreenSaver.setLayoutParams(params);
                    Ion.with(MainActivity.this)
                            .load(bannerlinks.get(
                                            new Random().nextInt(
                                                    bannerlinks.size()
                                            )
                                    )
                            )
                            .withBitmap()
                            .error(R.drawable.screensaver)
                            .intoImageView(imgScreenSaver);


                    stopShowingHandler.postDelayed(stopShowingRunnable, 2000);

                }
            };
            stopShowingHandler.postDelayed(stopShowingRunnable, 5000);


        }
    }
}
