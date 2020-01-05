package com.sf.evento.Adapters;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sf.evento.R;
import com.sf.evento.Classes.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.ViewHolder> {

    private List<DocumentSnapshot> friends;
    FirebaseFirestore db;
    StorageReference mStorageRef;

    public MyFriendsAdapter(FirebaseFirestore db, List<DocumentSnapshot> friends) {
        this.db = db;
        this.friends = friends;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFriendsAdapter.ViewHolder(LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.my_friends_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        DocumentSnapshot req = friends.get(position);
        CollectionReference users = db.collection("users");
       final String mobile1= (String) req.getId();
        Query query = users.whereEqualTo("phoneNumber",mobile1 );


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                String image= (String) ds.get("profilePicture");
                holder.name.setText((String) ds.get("name"));
                holder.mobile.setText((String) ds.get("phoneNumber"));
                holder.remove.setTag(ds);

                if(image!=null) {
                    mStorageRef.child(Uri.parse(image).getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Picasso.get().load(uri).into(holder.profileImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }

            }


        });
      holder.remove.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                DocumentSnapshot ds = (DocumentSnapshot) holder.remove.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                User u=new User();
                u.setId(user.getUid());u.setPhoneNumber(user.getPhoneNumber());
               u.Remove(db, ds);
               friends.remove(position);
               notifyDataSetChanged();

           }
        });

    }


    @Override
    public int getItemCount() {

        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView name;
        TextView mobile;
        Button remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            mobile= itemView.findViewById(R.id.mobile_number);
            remove = itemView.findViewById(R.id.removeButton);

        }
    }
}
