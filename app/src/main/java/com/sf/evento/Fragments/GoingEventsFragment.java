package com.sf.evento.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.sf.evento.Activites.EventRequests;
import com.sf.evento.Adapters.EventRequestsAdapter;
import com.sf.evento.Adapters.FriendRequestAdapter;
import com.sf.evento.Adapters.MyGoingEventsAdapter;
import com.sf.evento.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoingEventsFragment extends Fragment {
    View view;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private RecyclerView recyclerView;

    public GoingEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_going_events,container,false);
        db = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.GoingEventsRecycler);
        CollectionReference Requesets = db.collection("eventsRequests");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = Requesets.whereEqualTo("to", user.getPhoneNumber()).whereEqualTo("status","Approved");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                List<DocumentSnapshot> ds = task.getResult().getDocuments();
                                MyGoingEventsAdapter myGoingEventsAdapter = new MyGoingEventsAdapter(db, ds);
                                recyclerView.setLayoutManager(new LinearLayoutManager(GoingEventsFragment.this.getContext(), RecyclerView.VERTICAL,false));
                                recyclerView.setAdapter(myGoingEventsAdapter);
                            }
                            else
                            {
                                Toast.makeText(
                                        getActivity().getApplicationContext(), "No Events", Toast.LENGTH_LONG
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
        return view;
    }

}
