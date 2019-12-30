package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sf.evento.Adapters.CustomListAdapterDialog;
import com.sf.evento.Classes.Event;
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
        event_location=findViewById(R.id.event_location);
        event_location.setText(latt+" and "+longg);
        name=findViewById(R.id.event_name);
        description=findViewById(R.id.event_info);
        Date= findViewById(R.id.event_date);
        address=findViewById(R.id.event_address);
        startTime=findViewById(R.id.event_start_time);
        endTime= findViewById(R.id.event_end_time);

        x= findViewById(R.id.guestTextView);
        submit=findViewById(R.id.submit);
        mItemSelected = findViewById(R.id.guest_spinner);






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
        if (task.isSuccessful()) {
            if(task.getResult().size() > 0) {
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
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(CreateEvent.this, adapter.getSelectedIds()+"", Toast.LENGTH_SHORT).show();
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
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                e.description=description.getText().toString();
                e.CreateEvent();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date.setText(sdf.format(myCalendar.getTime()));
    }


}




