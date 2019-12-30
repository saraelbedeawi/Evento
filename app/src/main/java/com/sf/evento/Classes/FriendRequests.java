package com.sf.evento.Classes;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FriendRequests
{

    private String userId;
    private String friendId;
    private String Status = "pending";
    private Long date;


    public FriendRequests(String userId, String friendId, String status, Long date) {
        this.userId = userId;
        this.friendId = friendId;
        this.date = date;
        this.Status=status;
    }
    public FriendRequests(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        friendId = friendId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


    public void SendRequest(FirebaseFirestore db)
    {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("from",this.userId );
        userMap.put("date", System.currentTimeMillis());
        userMap.put("to", this.friendId);
        userMap.put("status", "Pending");
        /*
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        // Atomically add a new region to the "regions" array field.
        washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"));

        * */
        db.collection("FriendRequests").add(userMap)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.w("FIREBASE", "Success on Adding");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("FIREBASE", "Failed on Adding");
                }
            });
    }
}
