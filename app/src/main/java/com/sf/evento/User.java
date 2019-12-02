package com.sf.evento;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class User
{
    private String id;
    private String fullName;
    private String phoneNumber;
    private String profilePicture;
    private String inGoingFriends[];
    private String outGoingFriends[];
    private String myFriends[];

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public User ()
    {

    }


    public User(String full_name, String phoneNumber,String profilePicture, String id) {
        this.fullName = full_name;
        this.phoneNumber = phoneNumber;
        this.profilePicture=profilePicture;
        this.id=id;
    }

    public String getfullName() {
        return fullName;
    }

    public void setFull_name(String full_name) {
        this.fullName = full_name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void SaveUser(FirebaseFirestore db)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates;
        if(this.profilePicture==null)
        {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(this.fullName)
                    .build();
        }
        else {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(this.fullName)
                    .setPhotoUri(Uri.parse(this.profilePicture))
                    .build();
        }

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", this.fullName);
        userMap.put("profilePicture", this.profilePicture);
        userMap.put("phoneNumber", user.getPhoneNumber());

        db.collection("users").document(user.getUid())
            .set(userMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });

    }

    public void SendRequest(FirebaseFirestore db, String userId,String friendId)
    {

        /*
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Atomically add a new region to the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"));

        * */
        List<String> requests  = new ArrayList<>();
        requests.add(friendId);

        db.collection("users").document(userId).collection("outGoingFriends")
                .document("outGoing").set(requests)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constraints.TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constraints.TAG, "Error writing document", e);
                    }
                });
    }

}
