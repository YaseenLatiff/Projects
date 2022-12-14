package com.said.icare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Register_Fragment newInstance(String param1, String param2) {
        Register_Fragment fragment = new Register_Fragment();
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
    FirebaseAuth auth;
    DatabaseReference db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.fragment_register_, container, false);
        EditText email = vue.findViewById(R.id.txtEmail);
        EditText password = vue.findViewById(R.id.txtpass);
        EditText cpass = vue.findViewById(R.id.txtCpass);
        EditText Number = vue.findViewById(R.id.edtNumber);
        db = FirebaseDatabase.getInstance().getReference();
        Button login = vue.findViewById(R.id.btnLogin2);
        Button signup = vue.findViewById(R.id.btnCreate);
        TextView err = vue.findViewById(R.id.txtError);
        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Boolean empty;
                if ((email.length() == 0) || (password.length() == 0) || (cpass.length() == 0) || (Number.length() == 0)) {
                    empty = true;
                } else {
                    empty = false;
                }
                if (!empty) {
                    String e = email.getText().toString();
                    String p = password.getText().toString();
                    String n = Number.getText().toString();

                    if (cpass.getText().toString().equals(p)) {
                        if (Number.length() == 10) {
                            auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Dictionary usr = new Hashtable();
                                        usr.put("Email", e);
                                        usr.put("Name", "None");
                                        usr.put("Surname", "None");
                                        usr.put("Patient", "None");
                                        usr.put("Number", n);
                                        usr.put("Patient Number", "none");
                                        usr.put("Patient ID", "None");


                                        db.child("User").push().setValue(usr);

                                        Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        Home_Fragment home = new Home_Fragment();
                                        FragmentManager fragman = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragtranact = fragman.beginTransaction();
                                        fragtranact.replace(R.id.nav_fragment,home);
                                        fragtranact.commit();
                                    } else {
                                        err.setText("Registration Failed! " + task.getException().getMessage());
                                        if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                            email.setBackgroundColor(Color.RED);
                                            Number.setBackgroundColor(Color.WHITE);
                                            cpass.setBackgroundColor(Color.WHITE);
                                            password.setBackgroundColor(Color.WHITE);

                                        }
                                        if (task.getException().getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                                            password.setBackgroundColor(Color.RED);
                                            email.setBackgroundColor(Color.WHITE);
                                            Number.setBackgroundColor(Color.WHITE);
                                            cpass.setBackgroundColor(Color.WHITE);

                                        }
                                    }

                                }
                            });
                            ;
                        } else {
                            err.setText("Registration Failed! Phone numbers need atleast 10 digits");
                            Number.setBackgroundColor(Color.RED);
                            password.setBackgroundColor(Color.WHITE);
                            cpass.setBackgroundColor(Color.WHITE);
                            email.setBackgroundColor(Color.WHITE);

                        }

                    }
                    else {
                        err.setText("Registration Failed! Please make sure both passwords match");
                        password.setBackgroundColor(Color.RED);
                        cpass.setBackgroundColor(Color.RED);
                        email.setBackgroundColor(Color.WHITE);
                        Number.setBackgroundColor(Color.WHITE);
                    }


                }
                else{
                    err.setText("Registration Failed! Please make sure to fill in all fields");

                }

            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Home_Fragment home = new Home_Fragment();
                FragmentManager fragman = getActivity().getSupportFragmentManager();
                FragmentTransaction fragtranact = fragman.beginTransaction();
                fragtranact.replace(R.id.nav_fragment,home);
                fragtranact.commit();
            }
        });
        return vue;
    }
}