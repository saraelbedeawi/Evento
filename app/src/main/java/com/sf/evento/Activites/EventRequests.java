package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.Adapters.EventRequestsAdapter;
import com.sf.evento.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EventRequests extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_requests);
        db = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.eventsRequestsRecycler);
        CollectionReference Requesets = db.collection("eventsRequests");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = Requesets.whereEqualTo("to", user.getPhoneNumber()).whereEqualTo("status","pending");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                List<DocumentSnapshot> ds = task.getResult().getDocuments();
                                EventRequestsAdapter eventRequestsAdapter = new EventRequestsAdapter(db, ds);
                                recyclerView.setLayoutManager(new LinearLayoutManager(EventRequests.this, RecyclerView.VERTICAL,false));
                                recyclerView.setAdapter(eventRequestsAdapter);
                            }
                            else
                            {
                                Toast.makeText(
                                        EventRequests.this, "No Events", Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error getting documents: "+ e.getMessage());
                    }
                });
    }
}
