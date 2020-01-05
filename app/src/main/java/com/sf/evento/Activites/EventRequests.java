package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.Adapters.EventRequestsAdapter;
import com.sf.evento.Adapters.ViewPageAdapter;
import com.sf.evento.Fragments.GoingEventsFragment;
import com.sf.evento.Fragments.MyEventsFragment;
import com.sf.evento.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EventRequests extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    TextView checking;
    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    public ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_requests);
        db = FirebaseFirestore.getInstance();

        tablayout=(TabLayout)findViewById(R.id.tabellayout_id);
        appBarLayout=(AppBarLayout) findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("                My Invitations");
        }

        NavigationView nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };



        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                if(menuItem.getItemId()==R.id.create_event)
                {
                    Intent i = new Intent(EventRequests.this, MapsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.home)
                {
                    Intent i = new Intent(EventRequests.this, MainActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.my_invitations)
                {
                    Intent i = new Intent(EventRequests.this, EventRequests.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.friends)
                {
                    Intent i = new Intent(EventRequests.this, FriendsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(EventRequests.this, SplashActivity.class));
                }
                else if(menuItem.getItemId()==R.id.my_profile)
                {
                    Intent i = new Intent(EventRequests.this, Profile.class);
                    startActivity(i);
                }

                //Toast.makeText(getApplicationContext(), menuItem.getTitle() + " Selected",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });
        checking= findViewById(R.id.checking);
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
                                checking.setVisibility(View.VISIBLE);

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
