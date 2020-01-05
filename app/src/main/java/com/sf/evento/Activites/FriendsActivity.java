package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.sf.evento.Adapters.ViewPageAdapter;
import com.sf.evento.Fragments.FragmentFriendRequests;
import com.sf.evento.Fragments.FragmentMyFriends;
import com.sf.evento.Fragments.fragmentAddFriends;
import com.sf.evento.R;

public class FriendsActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        tablayout=(TabLayout)findViewById(R.id.tabellayout_id);
        appBarLayout=(AppBarLayout) findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        ViewPageAdapter adpter = new ViewPageAdapter((getSupportFragmentManager()));
        adpter.AddFragment(new FragmentMyFriends(),"My Friends");
        adpter.AddFragment(new FragmentFriendRequests(),"Friends Requests");
        adpter.AddFragment(new fragmentAddFriends(),"Add Friends");
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
                    Intent i = new Intent(FriendsActivity.this, MapsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.home)
                {
                    Intent i = new Intent(FriendsActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.my_invitations)
                {
                    Intent i = new Intent(FriendsActivity.this, EventRequests.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.friends)
                {
                    Intent i = new Intent(FriendsActivity.this, FriendsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(FriendsActivity.this, SplashActivity.class));
                }
                else if(menuItem.getItemId()==R.id.my_profile)
                {
                    Intent i = new Intent(FriendsActivity.this, Profile.class);
                    startActivity(i);
                }

                //Toast.makeText(getApplicationContext(), menuItem.getTitle() + " Selected",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });
    }
}