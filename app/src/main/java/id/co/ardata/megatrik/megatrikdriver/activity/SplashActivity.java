package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (sessionManager.isLoggedIn()){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                handler.removeCallbacks(this);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
