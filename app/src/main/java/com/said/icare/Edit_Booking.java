package com.said.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class Edit_Booking extends AppCompatActivity {
    DatabaseReference db;
    Boolean isBooked = false;
    FirebaseAuth auth;
    FirebaseUser user;
    String patient, sID;
    Intent nintent;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);
        DatePicker dpDate = findViewById(R.id.dPicker);
        TimePicker tpTime= findViewById(R.id.tPicker);
        Button btnBook = findViewById(R.id.btnBook);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        db = FirebaseDatabase.getInstance().getReference();
        nintent = getIntent();
        String ID = nintent.getStringExtra("ID");

        btnBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isBooked = false;
                int Month = dpDate.getMonth()+1;
                String date = dpDate.getYear() + "/" + Month + "/" + dpDate.getDayOfMonth();
                String time = tpTime.getHour() + ":" + tpTime.getMinute();

                db.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String dDate = snapshot.child("Date").getValue().toString();
                            String tTime = snapshot.child("Time").getValue().toString();
                            i++;
                            if (dDate.equals(date) && tTime.equals(time)){
                                isBooked = true;
                            }

                        }
                        if(isBooked == false){
                            db.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Hashtable appoint = new Hashtable();
                                    appoint.put("Date",date);
                                    appoint.put("Time",time);

                                    db.child("Appointments").child(ID).updateChildren(appoint);
                                    Toast.makeText(Edit_Booking.this, "Your time has been changed", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Edit_Booking.this,Manage_Booking.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }

                            });
                        }
                        else{
                            Toast.makeText(Edit_Booking.this, "That time is taken", Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });




            }

        });
    }
}