package com.said.icare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String showrec = "";
    public Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homes2_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();
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
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Message> list;
    TextInputLayout message;
    FloatingActionButton send;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.chat, container, false);
        send =  vue.findViewById(R.id.fab_send);
        message = vue.findViewById(R.id.message);
        recyclerView = vue.findViewById(R.id.recyclerview);

        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        String uId = user.getUid();
        String uEmail = user.getEmail();
        String timeStamp = new SimpleDateFormat("dd-MM-yy HH:mm a").format(Calendar.getInstance().getTime());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getEditText().getText().toString();
                db.child("Groups").child(showrec).push().setValue(new Message(uEmail,msg,timeStamp)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        message.getEditText().setText("");
                    }
                });
            }
        });

        adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);



        return vue;
    }

    final static String DATA_RECEIVE = "data_receive";
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            showrec = args.getString(DATA_RECEIVE);
        }
        receiveMessages();
    }

    private void receiveMessages(){
        db.child("Groups").child(showrec).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                String realest = getActivity().getIntent().getStringExtra("email");

                for (DataSnapshot snap: snapshot.getChildren()) {
                    Message message = snap.getValue(Message.class);
                    adapter.addMessage(message);

                    //if(hope.equals(realest)) {
                    //    adapter.addMessage(message);
                    //}
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}