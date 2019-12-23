package com.sf.evento;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageReference;
import com.sf.evento.Adapters.FriendRequestAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentFriendRequests extends Fragment
{
    View view;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private RecyclerView recyclerView;

    public FragmentFriendRequests() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.friend_request_fragment,container,false);
        db = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.RequestsRecycler);
        CollectionReference Requesets = db.collection("FriendRequests");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = Requesets.whereEqualTo("to", user.getPhoneNumber()).whereEqualTo("status","Pending");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                List<DocumentSnapshot> ds = task.getResult().getDocuments();
                                FriendRequestAdapter friendRequestAdapter = new FriendRequestAdapter(db, ds);
                                recyclerView.setLayoutManager(new LinearLayoutManager(FragmentFriendRequests.this.getContext(),RecyclerView.VERTICAL,false));
                                recyclerView.setAdapter(friendRequestAdapter);
                            }
                            else
                            {
                                Toast.makeText(
                                        getActivity().getApplicationContext(), "No Friend Requests", Toast.LENGTH_LONG
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
