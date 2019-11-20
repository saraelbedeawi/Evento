package com.sf.evento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import static android.content.ContentValues.TAG;


public class Profile extends AppCompatActivity {

    EditText full_name, mobile_number;
    ImageView profile_image;
    Button edit_button, delete_button,update_button;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        full_name=(EditText)findViewById(R.id.full_name);
        mobile_number=(EditText)findViewById(R.id.mobile_number);
        profile_image=(ImageView) findViewById(R.id.profile_image);
        edit_button=(Button) findViewById(R.id.edit_button);
        delete_button=(Button) findViewById(R.id.delete_button);
        update_button=(Button) findViewById(R.id.update_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        update_button.setVisibility(View.GONE);

        String name = user.getDisplayName();
        String number= user.getPhoneNumber();
        //Uri photoUrl = user.getPhotoUrl();
        full_name.setText(name);
        mobile_number.setText(number);
        //profile_image.setImageURI(photoUrl);


        edit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                full_name.setEnabled(true);
                edit_button.setVisibility(View.GONE);
                update_button.setVisibility(View.VISIBLE);

            }

        });

        delete_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }

        });

        update_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(full_name.getText().toString())
                        // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    Intent i = new Intent(Profile.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            }
                        });
            }

        });


    }




}
