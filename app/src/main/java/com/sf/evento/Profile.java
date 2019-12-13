package com.sf.evento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class Profile extends AppCompatActivity {

    StorageReference mStorageRef;
    private static final int PICK_IMAGE=1;

    Uri imgURI;
    EditText full_name, mobile_number;
    ImageView profile_image;
    Button edit_button, delete_button,update_button;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
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

        if(user.getPhotoUrl() != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
            mStorageRef.child(user.getPhotoUrl().getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Picasso.get().load(uri).into(profile_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        full_name.setText(name);
        mobile_number.setText(number);
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

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    Intent i = new Intent(Profile.this, SplashActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                }
                            }
                        });
            }

        });

        update_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(profile_image==null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(full_name.getText().toString())
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
                else
                {
                    fileUploader().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(full_name.getText().toString())
                                        .setPhotoUri(Uri.parse(downloadUri.getPath()))
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
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });

                }

            }

        });

        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fileChooser();
            }
        });


    }

    private void fileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE)
        {
            imgURI=data.getData();
            profile_image.setImageURI(imgURI);
        }
        else
        {
            profile_image.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/evento-d4c29.appspot.com/o/Images%2Fprofile.png?alt=media&token=fdc66cd9-ddbc-44d4-a058-6fd14a988ab2"));
        }
    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private Task<Uri> fileUploader()
    {
        final StorageReference ref=mStorageRef.child(System.currentTimeMillis()+"."+ getExtension(imgURI));
        return ref.putFile(imgURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(Profile.this,"image uploaded successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(Profile.this,"fashal ya homar",Toast.LENGTH_LONG).show();
                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                });

    }






}
