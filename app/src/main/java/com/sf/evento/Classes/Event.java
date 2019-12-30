package com.sf.evento.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Event
{
    private FirebaseFirestore db;
    Map<String, Object> eventRequest = new HashMap<>();

    public String id;
    public  String creatorId;
    public String adresse;
    public String name;
    public String date;
    public String startTime;
    public  String endTime;
    public  double latitude;
    public  double longitude;
    public   ArrayList<String> guests;
    public  String description;

    public Event(String creatorId, String adresse, String name, String date, String startTime, String endTime, double latitude, double longitude, ArrayList<String> guests, String description) {
        this.creatorId = creatorId;
        this.adresse = adresse;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.guests = guests;
        this.description = description;
    }

    public Event() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<String> guests) {
        this.guests = guests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   public  void CreateEvent()
   {
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       db = FirebaseFirestore.getInstance();


       //guests = new ArrayList<>();
       Map<String, Object> userMap = new HashMap<>();
       userMap.put("name", this.name);
       userMap.put("eventDate", this.date);
       userMap.put("creatorId", user.getUid());
       userMap.put("adresse", this.adresse);
       userMap.put("startTime", this.startTime);
       userMap.put("endTime", this.endTime);
       userMap.put("description", this.description);
       userMap.put("location", new com.google.firebase.firestore.GeoPoint(this.latitude, this.longitude));
       userMap.put("guests",this.guests);

       final ArrayList<String> guest2 = this.guests;

       db.collection("events")
               .add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                       Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                       eventRequest.put("eventId",documentReference.getId());
                       eventRequest.put("from",user.getPhoneNumber());
                       eventRequest.put("to", guest2);
                       eventRequest.put("status","pending");

                       for(int i =0;i<guest2.size();i++)
                       {
                           eventRequest.put("to", guest2.get(i));
                           db.collection("eventsRequests").add(eventRequest)
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

       })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.w(TAG, "Error adding document", e);
                   }
               });



//       db.collection("events").document(user.getUid())
//               .set(eventRequest)
//               .addOnSuccessListener(new OnSuccessListener<Void>() {
//                   @Override
//                   public void onSuccess(Void aVoid) {
//                       Log.d(TAG, "DocumentSnapshot successfully written!");
//                   }
//               })
//               .addOnFailureListener(new OnFailureListener() {
//                   @Override
//                   public void onFailure(@NonNull Exception e) {
//                       Log.w(TAG, "Error writing document", e);
//                   }
//               });




   }
    public void Accept(FirebaseFirestore db ,String id)
    {

        DocumentReference friendRequestsRef =  db.collection("eventsRequests").document(id);
        friendRequestsRef.update("status", "Approved")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    public void remove(FirebaseFirestore db ,String id)
    {

        DocumentReference friendRequestsRef =  db.collection("eventsRequests").document(id);
        friendRequestsRef.update("status", "Rejected")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


}
