package com.sf.evento.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sf.evento.Classes.User;
import com.sf.evento.Classes.FriendRequests;
import com.sf.evento.R;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class fragmentAddFriends extends Fragment
{
    private FirebaseFirestore db;
    StorageReference mStorageRef;
    FirebaseUser user;

    View view;
    TextView name;
    ImageView profileImage;
    Button addButton;
    Button acceptButton;
    Button rejectButton;
    RelativeLayout profileCardLayout;
    RelativeLayout addButtonLayout;
    RelativeLayout acceptButtonLayout;



    public fragmentAddFriends() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.add_friends_fragment,container,false);
        db = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.name);
        profileImage =view.findViewById(R.id.profile_image);
        addButton=view.findViewById(R.id.addButton);
        profileCardLayout =view.findViewById(R.id.profileCard);
        addButtonLayout = view.findViewById(R.id.addButtonLayout);
        acceptButtonLayout = view.findViewById(R.id.acceptButtonLayout);
        acceptButton=view.findViewById(R.id.acceptButton);
        rejectButton=view.findViewById(R.id.rejectButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        // Create a query against the collection.
        Button search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAddFriends.this.clearButtons();

                EditText number = view.findViewById(R.id.phone);
                String mobile = "+2" + number.getText().toString();
                fragmentAddFriends.this.checkIfAlreadyAFriend(mobile);


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String userPhone = (String) addButton.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FriendRequests request= new FriendRequests(user.getPhoneNumber(),userPhone);
                request.SendRequest(db);
                profileCardLayout.setVisibility(view.GONE);
                addButtonLayout.setVisibility(view.GONE);
                clearButtons();
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot ds = (DocumentSnapshot) profileCardLayout.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                User u=new User();
                u.setId(user.getUid());
                u.setPhoneNumber(user.getPhoneNumber());

                u.Accept(db, ds);
                profileCardLayout.setVisibility(view.GONE);
                acceptButtonLayout.setVisibility(view.GONE);
               fragmentAddFriends.this.clearButtons();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot ds = (DocumentSnapshot) profileCardLayout.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                User u=new User();

                u.setPhoneNumber(user.getPhoneNumber());
                u.Reject(db, ds);
                profileCardLayout.setVisibility(view.GONE);
                acceptButtonLayout.setVisibility(view.GONE);
                fragmentAddFriends.this.clearButtons();
            }
        });
        return view;
    }

    private void checkIfAlreadyAFriend(final String mobile) {
        CollectionReference friendRequestsRef = db.collection("users").document(user.getUid()).collection("friends");
        DocumentReference docRef  = friendRequestsRef.document(mobile);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        fragmentAddFriends.this.getUserByPhone(document.getId());

                    } else {
                        Log.d(TAG, "No such document");
                        fragmentAddFriends.this.checkIfAlreadySentARequest(mobile);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void getUserByPhone(String phoneNumber) {

        CollectionReference friendRequestsRef = db.collection("users");
        Query query = friendRequestsRef
                .whereEqualTo("phoneNumber", phoneNumber);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if(task.getResult().size() > 0) {
                                DocumentSnapshot ds = task.getResult().getDocuments().get(0);

                                String Name= (String) ds.get("name");
                                String image =(String) ds.get("profilePicture");

                                name.setText(Name);
                                mStorageRef.child(Uri.parse(image).getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        Picasso.get().load(uri).into(profileImage);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

                                profileCardLayout.setVisibility(View.VISIBLE);
                                profileCardLayout.setTag(ds);

                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void checkIfAlreadySentARequest(final String mobile) {
        CollectionReference friendRequestsRef = db.collection("FriendRequests");
        Query query = friendRequestsRef
                            .whereEqualTo("from", user.getPhoneNumber())
                            .whereEqualTo("to", mobile)
                            .whereEqualTo("status", "Pending");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)
                        {

                            DocumentSnapshot ds = task.getResult().getDocuments().get(0);

//                            String Name= (String) ds.get("name");
//                            String image =(String) ds.get("profilePicture");
                            fragmentAddFriends.this.getUserByPhone(mobile);
//                            name.setText(Name);
//                            Picasso.get().load(image).into(profileImage);
//                            profileCardLayout.setVisibility(View.VISIBLE);

                            addButtonLayout.setVisibility((View.VISIBLE));
                            addButton.setText("Pending");
                            addButton.setEnabled(false);

                        }
                        else {
                            fragmentAddFriends.this.checkIfAlreadyReceivedARequest(mobile);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(
                                getActivity().getApplicationContext(), "No user found", Toast.LENGTH_LONG
                        );
                        // Set the Toast display position profileCardLayout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();                     }
                });
    }

    private void checkIfAlreadyReceivedARequest(final String mobile) {
        CollectionReference friendRequestsRef = db.collection("FriendRequests");
        Query query = friendRequestsRef
                .whereEqualTo("from", mobile)
                .whereEqualTo("to", user.getPhoneNumber())
                .whereEqualTo("status", "Pending");
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)
                        {

                            fragmentAddFriends.this.getUserByPhone(mobile);
                            profileCardLayout.setVisibility(View.VISIBLE);
                            acceptButtonLayout.setVisibility((View.VISIBLE));
                        }
                        else {
                            fragmentAddFriends.this.checkIfUserExists(mobile);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(
                                getActivity().getApplicationContext(), "No user found", Toast.LENGTH_LONG
                        );
                        // Set the Toast display position profileCardLayout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();                     }
                });
    }

    private void checkIfUserExists(final String mobile) {
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef
                .whereEqualTo("phoneNumber", mobile);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)
                        {

                            DocumentSnapshot ds = task.getResult().getDocuments().get(0);

                            String Name= (String) ds.get("name");
                            String image =(String) ds.get("profilePicture");

                            name.setText(Name);
                            mStorageRef.child(Uri.parse(image).getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    Picasso.get().load(uri).into(profileImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                            profileCardLayout.setVisibility(View.VISIBLE);
                            profileCardLayout.setVisibility(View.VISIBLE);

                            addButtonLayout.setVisibility((View.VISIBLE));
                            addButton.setText("Add");
                            addButton.setTag(mobile);
                            addButton.setEnabled(true);

                        }
                        else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(), "No user found", Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(
                                getActivity().getApplicationContext(), "No user found", Toast.LENGTH_LONG
                        );
                        // Set the Toast display position profileCardLayout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();                     }
                });
    }

    private void clearButtons()
    {
        addButtonLayout.setVisibility(View.GONE);
        acceptButtonLayout.setVisibility(View.GONE);

    }

}
