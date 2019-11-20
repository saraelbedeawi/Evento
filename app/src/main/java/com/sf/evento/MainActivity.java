package com.sf.evento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageView logout,my_profile,friends;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout=(ImageView)findViewById(R.id.logout);
        friends=(ImageView)findViewById(R.id.friends);
        my_profile=(ImageView)findViewById(R.id.my_profile);
        my_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Profile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));

            }

        });

        friends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

        });


    }
}
