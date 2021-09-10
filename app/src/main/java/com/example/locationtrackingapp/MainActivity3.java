package com.example.locationtrackingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener
{
    private Button getUsers;
    private ListView UsersList;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getUsers = (Button) findViewById(R.id.getusers);
        getUsers.setOnClickListener(this);
        UsersList = (ListView) findViewById(R.id.usersList);
    }
    @Override
    public void onClick(View view) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.users,arrayList);
        UsersList.setAdapter(adapter);
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 arrayList.clear();
                 for(DataSnapshot snapshot1: snapshot.getChildren()){
                     arrayList.add(snapshot1.getValue().toString());
                 }
                 adapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}