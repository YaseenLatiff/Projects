package com.said.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Manage_Booking extends AppCompatActivity {
    String Date, day, key;
    ArrayAdapter<String> adapt;
    ListView Book;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseUser use;
    FloatingActionButton delete, edit;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking);
        delete = findViewById(R.id.fabDelete);
        edit = findViewById(R.id.fabEdit);
        db = FirebaseDatabase.getInstance().getReference();


        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> number = new ArrayList<>();
        Book = findViewById(R.id.lstBookings);
        auth = FirebaseAuth.getInstance();
        use = auth.getCurrentUser();

        day = "";
        db.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("Email").getValue().toString().equals(use.getEmail())){
                        key = snapshot.getKey().toString();
                        Date = key+" Date: "+snapshot.child("Date").getValue().toString()+"\n Time: "+snapshot.child("Time").getValue().toString();
                        number.add(key);
                        count++;
                        list.add(Date);


                    }

                }
                adapt = new ArrayAdapter(getApplicationContext(), R.layout.listsize, list);
                Book.setAdapter(adapt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        Book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                day = Book.getItemAtPosition(position).toString();
            }
        });
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!day.equals("")){
                    Intent intent = new Intent(Manage_Booking.this,Edit_Booking.class);
                    intent.putExtra("ID",key);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(Manage_Booking.this, "Please choose a date to edit", Toast.LENGTH_SHORT).show();



            }

        });
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                for(int i = 0; i < count;i++){
                    if(list.get(i).equals(day)){
                        db.child("Appointments").child(number.get(i)).removeValue();
                        count--;
                        list.remove(i);
                        number.remove(i);
                        adapt.remove(day);
                        Book.setAdapter(adapt);
                    }
                }


            }

        });

    }
}