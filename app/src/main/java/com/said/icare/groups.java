package com.said.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link groups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class groups extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public groups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment groups.
     */
    // TODO: Rename and change types and number of parameters
    public static groups newInstance(String param1, String param2) {
        groups fragment = new groups();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vue = inflater.inflate(R.layout.fragment_groups, container, false);
        ArrayList<String> list = new ArrayList<>();
        Button btnAdd = vue.findViewById(R.id.btnAdd);
        ListView Group = vue.findViewById(R.id.lstGroups);
        DatabaseReference db  = FirebaseDatabase.getInstance().getReference();


        db.child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getKey().toString();
                    list.add(name);
                }
                ArrayAdapter<String> adapt = new ArrayAdapter(getActivity().getBaseContext(), R.layout.listsize, list);
                Group.setAdapter(adapt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Group.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) Group.getItemAtPosition(position);
                Toast.makeText(getActivity(),"You selected : " + item,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),ChatRoom.class);
                intent.putExtra("group",item);
                startActivity(intent);


            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               EditText edtGroup = vue.findViewById(R.id.edtGroup);

               if(edtGroup.length() == 0){
                   Toast.makeText(getActivity().getApplicationContext(), "PPlease fill in the group name edit box above to add a new group or click on one of the groups below to enter a group chat.", Toast.LENGTH_LONG).show();

               }
               else{
                   String sGroup = edtGroup.getText().toString();
                   Intent intent = new Intent(getActivity(),ChatRoom.class);
                   intent.putExtra("group",sGroup);
                   startActivity(intent);
               }


           }
        });
        return vue;
    }
    public void onStart() {

        super.onStart();

    }
}