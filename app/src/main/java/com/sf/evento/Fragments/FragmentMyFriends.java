package com.sf.evento.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.Adapters.MyFriendsAdapter;
import com.sf.evento.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentMyFriends extends Fragment
{
    private FirebaseFirestore db;
    View view;
    private FirebaseUser user;

    private RecyclerView recyclerView;

    public FragmentMyFriends() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.my_friends_fragment,container,false);


        db = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.friendsRecycler);
        user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference friends = db.collection("users").document(user.getUid()).collection("friends");
        friends.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    if(task.getResult().size() > 0) {
                        List<DocumentSnapshot> ds = task.getResult().getDocuments();
                        MyFriendsAdapter myFriendsAdapter = new MyFriendsAdapter(db, ds);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentMyFriends.this.getContext(),RecyclerView.VERTICAL,false));
                        recyclerView.setAdapter(myFriendsAdapter);
                    }
                    else
                    {
                        Toast.makeText(
                                getActivity().getApplicationContext(), "No Friend ", Toast.LENGTH_LONG
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
