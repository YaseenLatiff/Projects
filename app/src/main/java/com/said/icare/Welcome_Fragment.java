package com.said.icare;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Welcome_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Welcome_Fragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Welcome_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Welcome_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Welcome_Fragment newInstance(String param1, String param2) {
        Welcome_Fragment fragment = new Welcome_Fragment();
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
    Button start;
    Button login;
    String e, p;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SQLiteDatabase mydb = getActivity().getBaseContext().openOrCreateDatabase("iCare.db", MODE_PRIVATE, null);;
        mydb.execSQL("CREATE TABLE IF NOT EXISTS Account(Email VARCHAR, Password VARCHAR);");

        Cursor curse = mydb.rawQuery("SELECT * FROM Account",null);
        curse.moveToFirst();
        if(curse.getCount()>0){
            auth = FirebaseAuth.getInstance();
            e = curse.getString(0);
            p = curse.getString(1);
            //ensuring the user doesnt have to login again after logging in once
            auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.putExtra("email",e);
                        startActivity(intent);
                    }

                }
            });

        }
        View vue =  inflater.inflate(R.layout.fragment_welcome_, container, false);

        start = vue.findViewById(R.id.btnStart);
        login = vue.findViewById(R.id.btnLogin);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //This code is used to move between fragments
                Register_Fragment reg = new Register_Fragment();
                FragmentManager fragman = getActivity().getSupportFragmentManager();
                FragmentTransaction fragtranact = fragman.beginTransaction();
                fragtranact.replace(R.id.nav_fragment,reg);
                fragtranact.commit();
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