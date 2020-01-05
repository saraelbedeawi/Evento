package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.Adapters.CustomListAdapterDialog;
import com.sf.evento.Adapters.ViewPageAdapter;
import com.sf.evento.Classes.Event;
import com.sf.evento.Fragments.FragmentFriendRequests;
import com.sf.evento.Fragments.FragmentMyFriends;
import com.sf.evento.Fragments.GoingEventsFragment;
import com.sf.evento.Fragments.MyEventsFragment;
import com.sf.evento.Fragments.fragmentAddFriends;
import com.sf.evento.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CreateEvent extends AppCompatActivity {

    private FirebaseUser user;
    Button x;
    Button submit;
    TextView mItemSelected;
    EditText Date,name,address,startTime,endTime,description;
    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    public ViewPager viewPager;
    DatePickerDialog picker;
    EditText event_location;
    private FirebaseFirestore db;

    AlertDialog.Builder mBuilder;
    private  CustomListAdapterDialog adapter;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        final String latt=getIntent().getStringExtra("x");
        final String longg=getIntent().getStringExtra("y");
        name=findViewById(R.id.event_name);
        Date= findViewById(R.id.event_date);
        address=findViewById(R.id.event_address);
        startTime=findViewById(R.id.event_start_time);
        endTime= findViewById(R.id.event_end_time);
        x= findViewById(R.id.guestTextView);
        submit=findViewById(R.id.submit);
        mItemSelected = findViewById(R.id.guest_spinner);

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
                    Intent i = new Intent(CreateEvent.this, MapsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.home)
                {
                    Intent i = new Intent(CreateEvent.this, MainActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.my_invitations)
                {
                    Intent i = new Intent(CreateEvent.this, EventRequests.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.friends)
                {
                    Intent i = new Intent(CreateEvent.this, FriendsActivity.class);
                    startActivity(i);
                }
                else if(menuItem.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(CreateEvent.this, SplashActivity.class));
                }
                else if(menuItem.getItemId()==R.id.my_profile)
                {
                    Intent i = new Intent(CreateEvent.this, Profile.class);
                    startActivity(i);
                }

                //Toast.makeText(getApplicationContext(), menuItem.getTitle() + " Selected",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });




        Date.setOnFocusChangeListener
                (new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                Date.setShowSoftInputOnFocus(false);
                if(hasFocus)
                {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(CreateEvent.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    picker.show();
                }
            }
        });

     //   event_start_time = (EditText) findViewById(R.id.event_start_time);
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                startTime.setShowSoftInputOnFocus(false);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                endTime.setShowSoftInputOnFocus(false);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
//-------------------------------------
        CollectionReference Friends = rootRef.collection("users").document(user.getUid()).collection("friends");
        Friends.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful())
        {
            if(task.getResult().size() > 0)
            {
               mBuilder  = new AlertDialog.Builder(CreateEvent.this);
                List<DocumentSnapshot> ds = task.getResult().getDocuments();

                adapter= new CustomListAdapterDialog(ds, db);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int item)
                                {

                                }
                            });
            }
                        else
                        {
                            Toast.makeText(CreateEvent.this, "No Friend ", Toast.LENGTH_LONG
                            ).show();
                            return;
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




        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mBuilder!=null)
                {
                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //    Toast.makeText(CreateEvent.this, adapter.getSelectedIds()+"", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            adapter.mSelectedItemsIds=new ArrayList<>();
                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().isEmpty())
                {
                    name.setError("Please fill the field");
                    name.requestFocus();
                    return;
                }
                else if (address.getText().toString().trim().isEmpty())
                {
                    address.setError("Please fill the field");
                    address.requestFocus();
                    return;
                }
                else if (Date.getText().toString().trim().isEmpty())
                {
                    Date.setError("Please fill the field");
                    Date.requestFocus();
                    return;
                }
                if(adapter.getSelectedIds().size()<=0)
                {
                    Toast.makeText(CreateEvent.this, "Please choose your Guests", Toast.LENGTH_SHORT).show();

                }
                else if(startTime.getText().toString().equals(endTime.getText().toString()))
                {
                    Toast.makeText(CreateEvent.this, "Please set start time diffrent than endtime ", Toast.LENGTH_SHORT).show();

                }
                else if (startTime.getText().toString().trim().isEmpty())
                {
                    startTime.setError("Please fill the field");
                    startTime.requestFocus();
                    return;
                }
                else if (endTime.getText().toString().trim().isEmpty())
                {
                    endTime.setError("Please fill the field");
                    endTime.requestFocus();
                    return;
                }
                else
                {
                    Event e= new Event();
                    e.name=name.getText().toString();
                    e.adresse=address.getText().toString();
                    e.date=Date.getText().toString();
                    e.startTime=startTime.getText().toString();
                    e.endTime=endTime.getText().toString();
                    e.guests=adapter.getSelectedIds();
                    e.latitude= Double.parseDouble(latt);
                    e.longitude= Double.parseDouble(longg);
                    e.creatorId=user.getUid();
                    //e.description=description.getText().toString();
                    e.CreateEvent();
                    Intent i = new Intent(CreateEvent.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(CreateEvent.this, "Event Created Successfully. ", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date.setText(sdf.format(myCalendar.getTime()));
    }


}




