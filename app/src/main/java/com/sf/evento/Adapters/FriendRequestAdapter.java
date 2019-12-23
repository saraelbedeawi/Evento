package com.sf.evento.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sf.evento.R;
import com.sf.evento.User;
import com.sf.evento.fragmentAddFriends;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private List<DocumentSnapshot> requests;
    FirebaseFirestore db;
    StorageReference mStorageRef;

    public FriendRequestAdapter(FirebaseFirestore db, List<DocumentSnapshot> requests) {
        this.db = db;
        this.requests = requests;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendRequestAdapter.ViewHolder(LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.requests_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        DocumentSnapshot req = requests.get(position);
        String phone = (String) req.get("from");
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("phoneNumber", phone);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                String image= (String) ds.get("profilePicture");
                holder.name.setText((String) ds.get("name"));
                holder.mobile.setText((String)ds.get("PhoneNumber"));
                holder.accept.setTag(ds);
                holder.reject.setTag(ds);

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

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentSnapshot ds = (DocumentSnapshot) holder.accept.getTag();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        User u=new User();
                        u.setId(user.getUid());
                        u.setPhoneNumber(user.getPhoneNumber());
                        u.Accept(db, ds);
                        requests.remove(holder.getAdapterPosition());
                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentSnapshot ds = (DocumentSnapshot) holder.reject.getTag();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        User u=new User();

                        u.setPhoneNumber(user.getPhoneNumber());
                        u.Reject(db, ds);
                       requests.remove(holder.getAdapterPosition());
                    }
                });


            }
        });
    }


    @Override
    public int getItemCount() {

        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView name;
        TextView mobile;
        Button accept;
        Button reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            mobile= itemView.findViewById(R.id.mobile_number);
            accept = itemView.findViewById(R.id.acceptButton);
            reject = itemView.findViewById(R.id.rejectButton);

        }
    }
}
