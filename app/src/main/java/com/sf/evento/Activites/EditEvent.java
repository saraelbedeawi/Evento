package com.sf.evento.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EditEvent extends AppCompatActivity {
    TextView name,from, adresse,date;
    TextView startTime;
    TextView endTime;
    Button guestTextView;
    DatePickerDialog picker;
    ArrayList guests;
    ArrayList oldGuests;
    AlertDialog.Builder mBuilder;
    private CustomListAdapterDialog adapter;
    private FirebaseFirestore db;
    private FirebaseUser user;
    String eventId;
    ImageView location;
    Button submit;
    double lat;
    double longg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_edit_event);
        name = findViewById(R.id.event_name);
        adresse=findViewById(R.id.event_address);
        date=findViewById(R.id.event_date);
        startTime=findViewById(R.id.event_start_time);
        endTime=findViewById(R.id.event_end_time);
        guestTextView=findViewById(R.id.guestTextView);
        submit=findViewById(R.id.submit);
        location=findViewById(R.id.gps);
        name.setText(getIntent().getStringExtra("name"));
        adresse.setText(getIntent().getStringExtra("address"));
        date.setText(getIntent().getStringExtra("date"));
        startTime.setText(getIntent().getStringExtra("startTime"));
        endTime.setText(getIntent().getStringExtra("endTime"));
        guests = getIntent().getStringArrayListExtra("guests");
        oldGuests = new ArrayList(guests);
        eventId= getIntent().getStringExtra("eventId");
        date.setOnFocusChangeListener
                (new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus)
                    {
                        date.setShowSoftInputOnFocus(false);
                        if(hasFocus)
                        {
                            final Calendar cldr = Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);
                            // date picker dialog
                            picker = new DatePickerDialog(EditEvent.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        CollectionReference Friends = rootRef.collection("users").document(user.getUid()).collection("friends");
        Friends.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size() > 0) {
                        mBuilder  = new AlertDialog.Builder(EditEvent.this);
                        List<DocumentSnapshot> ds = task.getResult().getDocuments();
                        adapter= new CustomListAdapterDialog(ds, db, guests);
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
                        Toast.makeText(EditEvent.this, "No Friend ", Toast.LENGTH_LONG
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

        guestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
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
                if (name.getText().toString().trim().isEmpty())
                {
                    name.setError("Please fill the field");
                    name.requestFocus();
                    return;
                }
                else if (adresse.getText().toString().trim().isEmpty())
                {
                    adresse.setError("Please fill the field");
                    adresse.requestFocus();
                    return;
                }
                if(adapter.getSelectedIds().size()<=0)
                {
                    Toast.makeText(EditEvent.this, "Please choose your Guests", Toast.LENGTH_SHORT).show();

                }
                else if(startTime.getText().toString().equals(endTime.getText().toString()))
                {
                    Toast.makeText(EditEvent.this, "Please set start time diffrent than endtime ", Toast.LENGTH_SHORT).show();

                }
                else if (date.getText().toString().trim().isEmpty())
                {
                    date.setError("Please fill the field");
                    date.requestFocus();
                    return;
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
                else{
                Event e= new Event();
                e.name=name.getText().toString();
                e.adresse=adresse.getText().toString();
                e.date=date.getText().toString();
                e.startTime=startTime.getText().toString();
                e.endTime=endTime.getText().toString();
                e.creatorId=user.getUid();
                e.id=eventId;
                e.oldguests = oldGuests;
                e.guests = adapter.getSelectedIds();
                e.longitude=longg;
                e.latitude=lat;
                e.UpdateEvent();
                Intent i = new Intent(EditEvent.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                Toast.makeText(EditEvent.this, "Event Updated Successfully. ", Toast.LENGTH_LONG).show();
            }}
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvent.this, EditMapsActivity.class);
                intent.putExtra("latt",getIntent().getStringExtra("x"));
                intent.putExtra("long",getIntent().getStringExtra("y"));
                startActivityForResult(intent,1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000 && resultCode==Activity.RESULT_OK)
        {
            lat= data.getExtras().getDouble("latt");
            longg= data.getExtras().getDouble("long");
        }
    }
}
