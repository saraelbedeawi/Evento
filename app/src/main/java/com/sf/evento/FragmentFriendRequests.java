package com.sf.evento;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentFriendRequests extends Fragment
{
    View view;
    private RecyclerView mRequestList;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TextView name,mobile;
    private ImageView profileImage;
    String fromid;
    public FragmentFriendRequests() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.friend_request_fragment,container,false);
//        mRequestList=(RecyclerView)view.findViewById(R.id.RequestsRecycler);
//        mRequestList.setHasFixedSize(true);
//        mRequestList.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        name=(TextView)view.findViewById(R.id.name);
        mobile=(TextView)view.findViewById(R.id.mobile_number);

        CollectionReference Requesets = db.collection("FriendRequests");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = Requesets.whereEqualTo("to", user.getUid());
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                                fromid= (String) ds.get("from");
                                DocumentReference  docRef  =    db.collection("users").document(fromid);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                name.setText((String)document.get("name"));
                                                String p = (String)document.get("ProfilePicture");
                                                mobile.setText((String)document.get("phoneNumber"));
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
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
