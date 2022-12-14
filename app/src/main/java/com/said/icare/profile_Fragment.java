package com.said.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_Fragment newInstance(String param1, String param2) {
        profile_Fragment fragment = new profile_Fragment();
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
    FirebaseAuth auth;
    TextView Email;
    TextView Name;
    TextView Surname;
    TextView Patient;
    TextView num;
    FirebaseUser user;
    String sName;
    String sSname;
    String sPatient;
    String sNum;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.fragment_profile_, container, false);
        //The code below connects to the Firebase Realtime database
        db = FirebaseDatabase.getInstance().getReference();
        //This snippet of code calls FirebaseAuth we dont need to specify a link as the project was connected
        //to firebase using android studio
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String sEmail = user.getEmail();
        db.child("User").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NotNull DataSnapshot shot){
                for(DataSnapshot snap : shot.getChildren()){
                    //Looping through the email table in order find a value
                    if(snap.child("Email").getValue().toString().toLowerCase().equals(sEmail.toLowerCase())){
                        //Storing the relevant data in variables
                        sName = snap.child("Name").getValue().toString();
                        sSname = snap.child("Surname").getValue().toString();
                        sPatient = snap.child("Patient").getValue().toString();

                        sNum = snap.child("Number").getValue().toString();
                    }
                }
                Email.setText(user.getEmail());
                Name.setText(sName);
                Surname.setText(sSname);
                Patient.setText(sPatient);
                num.setText(sNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        FloatingActionButton fabEdit = vue.findViewById(R.id.fabEdit);

        Email = vue.findViewById(R.id.txtE);
        Name = vue.findViewById(R.id.txtName);
        Surname = vue.findViewById(R.id.txtSname);
        Patient = vue.findViewById(R.id.txtPatient);
        num = vue.findViewById(R.id.txtNumber);



        fabEdit.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent intent = new Intent(getActivity(),Edit_Profile.class);
               startActivity(intent);

           }
        });




        return vue;

    }
}