package com.sf.evento.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sf.evento.Activites.EventRequests;
import com.sf.evento.Activites.FriendsActivity;
import com.sf.evento.Activites.MapsActivity;
import com.sf.evento.Activites.MyEvents;
import com.sf.evento.Activites.Profile;
import com.sf.evento.Activites.SplashActivity;
import com.sf.evento.Adapters.ViewPageAdapter;
import com.sf.evento.Classes.User;
import com.sf.evento.Fragments.GoingEventsFragment;
import com.sf.evento.Fragments.MyEventsFragment;
import com.sf.evento.R;

public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    public ViewPager viewPager;
    FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        tablayout=(TabLayout)findViewById(R.id.tabellayout_id);
        appBarLayout=(AppBarLayout) findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        ViewPageAdapter adpter = new ViewPageAdapter((getSupportFragmentManager()));
        adpter.AddFragment(new MyEventsFragment(),"My Events");
        adpter.AddFragment(new GoingEventsFragment(),"Going Events");
        viewPager.setAdapter(adpter);
        tablayout.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
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
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.home)
                {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.my_invitations)
                {
                    Intent i = new Intent(MainActivity.this, EventRequests.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.friends)
                {
                    Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, SplashActivity.class));
                }
                else if(menuItem.getItemId()==R.id.my_profile)
                {
                    Intent i = new Intent(MainActivity.this, Profile.class);
                    startActivity(i);
                }

                //Toast.makeText(getApplicationContext(), menuItem.getTitle() + " Selected",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("failed", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        User u= new User();
                        u.setToken(token);
                        u.SaveToken(db);
                        // Log and toast
                    }
                });



    }






}