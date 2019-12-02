package com.sf.evento;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class fragmentAddFriends extends Fragment
{
    private FirebaseFirestore db;
    View view;
    TextView name;
    ImageView profileImage;
    Button addButton;
    RelativeLayout layout;
    public fragmentAddFriends() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.add_friends_fragment,container,false);
        db = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.name);
        profileImage =view.findViewById(R.id.profile_image);
        addButton=view.findViewById(R.id.add);
        // Create a query against the collection.
        Button search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference usersRef = db.collection("users");
                EditText number = view.findViewById(R.id.phone);
                String mobile = number.getText().toString();
                Query query = usersRef.whereEqualTo("phoneNumber", "+2"+mobile);
                query.get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().size() > 0) {
                                        DocumentSnapshot ds = task.getResult().getDocuments().get(0);

                                    String Name= (String) ds.get("name");
                                    String image =(String) ds.get("profilePicture");
                                    addButton.setTag(ds.getId());
                                    name.setText(Name);
                                    }
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                    }
                                } else {
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
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String userId = (String) addButton.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                com.sf.evento.User u= new com.sf.evento.User();
                u.SendRequest(db,user.getUid(),userId);


            }
        });
        return view;
    }


}
