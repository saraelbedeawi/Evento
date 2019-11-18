package com.sf.evento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class LoginActivity extends AppCompatActivity {


    private ImageView profile_image;
    EditText full_name;
    private Button save_button;
    private static final int PICK_IMAGE=1;

    StorageReference mStorageRef;
    public Uri selected_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        full_name=(EditText)findViewById(R.id.full_name);
        save_button=(Button)findViewById(R.id.save_button) ;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fileChooser();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fileUploader();
            }
        });



    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader()
    {
        StorageReference Ref=mStorageRef.child(System.currentTimeMillis()+"."+ getExtension(selected_image));
        Ref.putFile(selected_image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(LoginActivity.this,"image uploaded successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(LoginActivity.this,"fashal ya homar",Toast.LENGTH_LONG).show();
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
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            selected_image=data.getData();
            profile_image.setImageURI(selected_image);

        }
    }
}
