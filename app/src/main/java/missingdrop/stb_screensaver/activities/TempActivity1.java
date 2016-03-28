package missingdrop.stb_screensaver.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;
import missingdrop.stb_screensaver.Utils.DownloadUtil;

public class TempActivity1 extends AppCompatActivity {
    private Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyJsonParser().execute();
            }
        });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getApp().active();
    }

    public MyApplication getApp() {
        return (MyApplication) this.getApplication();
    }

    private class MyJsonParser extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            DownloadUtil downloadUtil = new DownloadUtil();
            return downloadUtil.downloadStringContent("https://9c7504d4822f73bffc46d5ad2c612eb6c194e323.googledrive.com/host/0B364ILUXSyWXclFZRG9taEVKQ28");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Json String ",s);
            try{
                JSONObject jo = new JSONObject(s);
                JSONArray quests = jo.getJSONArray("questions");
                Log.d("JSON ARRAY",quests+"");
            }catch (JSONException je){
                je.printStackTrace();
            }
        }
    }
}
