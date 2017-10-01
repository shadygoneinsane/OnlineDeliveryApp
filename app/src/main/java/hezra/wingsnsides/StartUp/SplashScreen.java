package hezra.wingsnsides.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import hezra.wingsnsides.Main.MainNavigationActivity;
import hezra.wingsnsides.R;

public class SplashScreen extends AppCompatActivity {
    private Handler h;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (h == null) {
            h = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    SplashScreen.this.finish();
                    startActivity(new Intent(SplashScreen.this, MainNavigationActivity.class));
                }
            };
            h.postDelayed(r, 1500);
        } else {
            h.removeCallbacksAndMessages(null);
            h = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    SplashScreen.this.finish();
                    startActivity(new Intent(SplashScreen.this, MainNavigationActivity.class));
                }
            };
            h.postDelayed(r, 1500);
        }

    }
}
