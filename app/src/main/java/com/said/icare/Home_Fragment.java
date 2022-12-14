package com.said.icare;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
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
    EditText email,password;
    TextView txtErr;

    Button login,signup,forgot;
    FirebaseAuth auth;
    //Login
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Opening or Creating the SQLite database
        SQLiteDatabase mydb = getActivity().getBaseContext().openOrCreateDatabase("iCare.db", MODE_PRIVATE, null);;

        View vue = inflater.inflate(R.layout.fragment_home, container, false);

        email = vue.findViewById(R.id.edtName);
        password = vue.findViewById(R.id.edtpass);
        txtErr = vue.findViewById(R.id.txtError2);

        login = vue.findViewById(R.id.btnLog);
        signup = vue.findViewById(R.id.btnReg);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean empty;
                //Checking if the fields have values
                if((email.length() == 0) || (password.length() == 0) ){
                    empty = true;
                }
                else{
                    empty = false;
                }
                if(!empty){ //Ensuring no empty values cause glitches or errors by ensuring the code doesnt run if there is empty values
                    String e = email.getText().toString();
                    String p = password.getText().toString();
                    //Firebase auth  logging in the user for us
                    auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //The user is only sent to the next screen if the details are correct
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //We do this so we know the user has already logged in so that they dont have to login again
                                mydb.execSQL("INSERT INTO Account(Email, Password) VALUES('"+e+"', '"+p+"')");
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                intent.putExtra("email",e);
                                startActivity(intent);
                                //Checking for errors with the inputs
                            }else{
                                txtErr.setText("Login Failed! "+ task.getException().getMessage());
                                if(task.getException().getMessage().equals("The email address is badly formatted.")){
                                    email.setBackgroundColor(Color.RED);
                                    password.setBackgroundColor(Color.WHITE);
                                }
                                if(task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                    email.setBackgroundColor(Color.RED);
                                    password.setBackgroundColor(Color.WHITE);
                                }
                                if(task.getException().getMessage().equals("The password is invalid or the user does not have a password.")){
                                    password.setBackgroundColor(Color.RED);
                                    email.setBackgroundColor(Color.WHITE);
                                }
                                //Checking for errors with the inputs
                            }

                        }
                    });
                }
                else{
                    txtErr.setText("Login Failed! please make sure to enter data into all fields");

                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Register_Fragment reg = new Register_Fragment();
                FragmentManager fragman = getActivity().getSupportFragmentManager();
                FragmentTransaction fragtranact = fragman.beginTransaction();
                fragtranact.replace(R.id.nav_fragment,reg);
                fragtranact.commit();
            }
        });
        return vue;

    }
}