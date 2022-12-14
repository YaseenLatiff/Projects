package com.said.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homes_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homes_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Homes_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homes_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Homes_Fragment newInstance(String param1, String param2) {
        Homes_Fragment fragment = new Homes_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    DatabaseReference db;
    Boolean isBooked = false;
    FirebaseAuth auth;
    FirebaseUser user;
    String patient, ID, sPatient, sId;

    int i = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.fragment_homes_, container, false);

        DatePicker dpDate = vue.findViewById(R.id.dPicker);
        TimePicker tpTime= vue.findViewById(R.id.tPicker);
        Button btnBook = vue.findViewById(R.id.btnBook);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Button Manage = vue.findViewById(R.id.btnManage);

        db = FirebaseDatabase.getInstance().getReference();

        btnBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isBooked = false;
                int Month = dpDate.getMonth()+1;
                String date = dpDate.getYear() + "/" + Month+ "/" + dpDate.getDayOfMonth();
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
                                if(!isBooked){
                                    db.child("User").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if(user.getEmail().equals(snapshot.child("Email").getValue().toString())){
                                                    patient = snapshot.child("Patient").getValue().toString();
                                                    ID = snapshot.child("Patient ID").getValue().toString();
                                                }
                                                Hashtable appoint = new Hashtable();
                                                appoint.put("Email",user.getEmail());
                                                appoint.put("Date",date);
                                                appoint.put("Time",time);
                                                appoint.put("Patient",patient);
                                                appoint.put("Patient ID",ID);
                                                db.child("Appointments").child(Integer.toString(i)).setValue(appoint);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    Toast.makeText(getActivity().getApplicationContext(), "Your time has been booked", Toast.LENGTH_SHORT).show();


                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(), "That time is already taken", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                });






            }

        });
        Manage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Manage_Booking.class);
                startActivity(intent);

            }

        });
        return vue;
    }
}