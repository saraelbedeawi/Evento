package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sf.evento.Classes.User;
import com.sf.evento.R;

public class MainActivity extends AppCompatActivity {

    ImageView logout,my_profile,friends,create_event,my_event,my_invitations;
    FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout=(ImageView)findViewById(R.id.logout);
        friends=(ImageView)findViewById(R.id.friends);
        my_profile=(ImageView)findViewById(R.id.my_profile);
        create_event=(ImageView)findViewById(R.id.create_event);
        my_event=(ImageView)findViewById(R.id.my_events);
        my_invitations=(ImageView)findViewById(R.id.my_invitations);
        db = FirebaseFirestore.getInstance();

        my_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Profile.class);
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
                startActivity(i);
            }

        });

        create_event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }

        });
        my_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.whatsapp");
//                startActivity(sendIntent);
                Intent i = new Intent(MainActivity.this, MyEvents.class);
                startActivity(i);

            }
        });
        my_invitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventRequests.class);
                startActivity(i);
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("failed", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        User u= new User();
                        u.setToken(token);
                        u.SaveToken(db);
                        // Log and toast
                        String msg = "hiii";
                        Log.d("notfication", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
