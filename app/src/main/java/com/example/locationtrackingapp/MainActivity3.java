package com.example.locationtrackingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener
{
    private Button getUsers,signout,Activate_alaram,Deactivate;

    Context context = null;
    private TextView groupName;
    private ListView UsersList;
    private FirebaseAuth mAuth;
    private AlarmManager alarmManager;
    private PendingIntent pi;
    private Object GetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        
        GetLocation getLocation= new GetLocation(this);
        
        signout = (Button) findViewById(R.id.Signout);
        signout.setOnClickListener(this);
        getUsers = (Button) findViewById(R.id.getusers);
        getUsers.setOnClickListener(this);
        UsersList = (ListView) findViewById(R.id.usersList);
        Activate_alaram = (Button) findViewById(R.id.activate);
        Activate_alaram.setOnClickListener(this);
        Deactivate = (Button) findViewById(R.id.deactivate);
        Deactivate.setOnClickListener(this);
//        keepuserSignedIn();
        initializeAlaramManager();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Signout:
                signOutUser();
                break;
            case R.id.getusers:
                displayList();
                break;
            case R.id.activate:
                pushMyLocation();
                break;
            case R.id.deactivate:
                CancleLocationSharing();
                break;
        }
    }

    private void CancleLocationSharing() {
        if (alarmManager!=null){
            alarmManager.cancel(pi);
            Toast.makeText(MainActivity3.this,"alaram cancled",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity3.this,"Something failed" ,Toast.LENGTH_LONG).show();
        }

    }

    private void pushMyLocation() {
        Toast.makeText(MainActivity3.this,"alaram activated",Toast.LENGTH_SHORT).show();
        int time = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE){
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+(time*1000),time*1000,pi);
      alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                          SystemClock.elapsedRealtime(),
                          2*1000,
                          pi);
        }
        else{
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,System.currentTimeMillis()+(time*1000),time*1000,pi);
        }
        Toast.makeText(MainActivity3.this, "alaram set at "+time, Toast.LENGTH_LONG).show();
    }
    private void initializeAlaramManager(){
        Intent intent = new Intent(this,updateLocation.class);
        pi = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager=(AlarmManager) this.getSystemService(ALARM_SERVICE);
    }
    private void signOutUser() {
       FirebaseAuth.getInstance().signOut();
       startActivity(new Intent(this,MainActivity.class));
    }
    private void displayList() {
        ArrayList<String> arrayList = new ArrayList<>();
        HashMap<String,User> hashmap = new HashMap<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.users,arrayList);
        UsersList.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    User userprofile = snapshot1.getValue(User.class);
                    arrayList.add( userprofile.fullName);
                    hashmap.put(userprofile.fullName,userprofile);

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        UsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                User user_ = hashmap.get(arrayList.get(position));
                String la = user_.Latitude;
                String lo = user_.Longitude;
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.google.com/maps/place/"+la+","+lo));
                startActivity(viewIntent);
            }
        });
    }
}