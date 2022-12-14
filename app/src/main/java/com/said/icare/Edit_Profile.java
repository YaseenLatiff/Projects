package com.said.icare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Hashtable;

public class Edit_Profile extends AppCompatActivity {
    DatabaseReference db;
    FirebaseAuth auth;
    EditText Email, Name, Surname, num, Pnum, Idnum;

    FirebaseUser user;
    String sName, sSname, sNum, sPNum, sEmail, sPId;
    private static final int SMS_CODE = 103;
    Button Save;
    Button Add;
    String key;
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Edit_Profile.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Edit_Profile.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(Edit_Profile.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Email = findViewById(R.id.edtEmail);
        Name = findViewById(R.id.edtName);
        Surname = findViewById(R.id.edtsName);
        num = findViewById(R.id.edtsNumber);
        Pnum = findViewById(R.id.edtPatientphone);
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        sEmail = user.getEmail();

        db.child("User").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NotNull DataSnapshot shot){
                for(DataSnapshot snap : shot.getChildren()){
                    if(snap.child("Email").getValue().toString().toUpperCase().equals(sEmail.toUpperCase())){
                        sName = snap.child("Name").getValue().toString();
                        sSname = snap.child("Surname").getValue().toString();
                        sNum = snap.child("Number").getValue().toString();
                        sPNum = snap.child("Patient Number").getValue().toString();
                        sPId = snap.child("Patient ID").getValue().toString();
                        key = snap.getKey().toString();
                        if(!sName.equals("None"))
                            Name.setText(sName);
                        if(!sSname.equals("None"))
                            Surname.setText(sSname);
                        num.setText(sNum);
                        Email.setText(sEmail);
                        Pnum.setText(sPNum);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Edit_Profile.this, "Not Connected to the internet: "+error, Toast.LENGTH_SHORT).show();
            }

        });

        Add = findViewById(R.id.btnAdd);
        Save = findViewById(R.id.btnSave);

        Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Pnum.length()>0){
                    checkPermission(Manifest.permission.SEND_SMS, SMS_CODE);
                    if (ContextCompat.checkSelfPermission(Edit_Profile.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Pnum.getText().toString(), null, "Please enter the unique key into the emergency button app: " + key, null, null);
                        Toast.makeText(Edit_Profile.this, "A message was sent to the patients phone", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Edit_Profile.this, "Please give us the permission to send SMS's", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(Edit_Profile.this, "Please enter the patients phone number in the above edit box", Toast.LENGTH_SHORT).show();
                    Pnum.setBackgroundColor(Color.RED);
                }

            }
        });
        Save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if((Email.length()>0) && (Name.length()>0) && (Surname.length()>0) && (num.length()>0) && (Pnum.length()>0)){
                    Hashtable usr = new Hashtable();
                    usr.put("Email",Email.getText().toString());
                    usr.put("Name",Name.getText().toString());
                    usr.put("Surname",Surname.getText().toString());
                    usr.put("Number",num.getText().toString());
                    usr.put("Patient Number",Pnum.getText().toString());
                    db.child("User").child(key).updateChildren(usr);
                    user.updateEmail(Email.getText().toString());
                    Intent intent = new Intent(Edit_Profile.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(Edit_Profile.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();


            }
        });
    }
}