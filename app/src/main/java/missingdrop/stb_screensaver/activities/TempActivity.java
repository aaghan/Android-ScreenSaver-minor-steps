package missingdrop.stb_screensaver.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import missingdrop.stb_screensaver.MyApplication;
import missingdrop.stb_screensaver.R;

public class TempActivity extends AppCompatActivity {
    public MyApplication getApp(){
        return (MyApplication)this.getApplication();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        getApp().active();
    }
}
