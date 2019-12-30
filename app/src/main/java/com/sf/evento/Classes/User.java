package com.sf.evento.Classes;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    private String token;


    public String getProfilePicture() {
        return profilePicture;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
    public void setId(String id) {
        this.id = id;
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

    public void Accept(FirebaseFirestore db, DocumentSnapshot friendDS)
    {
        Map<String, Object> userMap = new HashMap<>();
        String friendPhone = (String) friendDS.get("phoneNumber");
        String friendId = friendDS.getId();
        db.collection("users").document(this.id).collection("friends").document(friendPhone).set(userMap);
        db.collection("users").document(friendId).collection("friends").document(this.phoneNumber).set(userMap);
        CollectionReference friendRequestsRef = db.collection("FriendRequests");
        Query query = friendRequestsRef
                .whereEqualTo("from", friendPhone)
                .whereEqualTo("to", this.phoneNumber)
                .whereEqualTo("status", "Pending");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)
                        {
                            DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                            ds.getReference().update("status","Approved");

                        }

                    }
                });
    }
public  void Reject (FirebaseFirestore db, DocumentSnapshot friendDS)
{
    String friendPhone = (String) friendDS.get("phoneNumber");
    CollectionReference friendRequestsRef = db.collection("FriendRequests");
    Query query = friendRequestsRef
            .whereEqualTo("from", friendPhone)
            .whereEqualTo("to", this.phoneNumber)
            .whereEqualTo("status", "Pending");
    query.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult().size() > 0)
                    {
                        DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                        ds.getReference().update("status","Rejected");

                    }

                }
            });
}
public void SaveToken(FirebaseFirestore db)
{
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference washingtonRef = db.collection("users").document(user.getUid());
    washingtonRef
            .update("token", token);
}
    public  void Remove (FirebaseFirestore db, DocumentSnapshot friendDS)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        db.collection("users").document(user.getUid()).collection("friends").document((String)friendDS.get("phoneNumber")).delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document", e);
                }
            });
        db.collection("users").document((String)friendDS.getId()).collection("friends").document(user.getPhoneNumber()).delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document", e);
                }
            });
    }



}
