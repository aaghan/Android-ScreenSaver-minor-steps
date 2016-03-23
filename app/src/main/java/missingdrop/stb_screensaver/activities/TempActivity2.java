package missingdrop.stb_screensaver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;

public class TempActivity2 extends AppCompatActivity {

    private static final String TAG = TempActivity2.class.getName();

    public MyApplication getApp(){
        return (MyApplication)this.getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp2);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getApp().active();
    }


    public void startAnotherActivity(View view){
        Intent i = new Intent(TempActivity2.this,TempActivity1.class);
        startActivity(i);
    }

}
