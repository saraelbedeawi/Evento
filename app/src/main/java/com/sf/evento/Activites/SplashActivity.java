package com.sf.evento.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.sf.evento.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_user_info), MODE_PRIVATE);
        int userId = sharedPref.getInt(getString(R.string.user_info_id), -1);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.finish();
                    startActivity(i);
                }
            }, 3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, PhoneVerificationActivity.class);
                    SplashActivity.this.finish();
                    startActivity(i);
                }
            }, 3000);
        }


    }
}
