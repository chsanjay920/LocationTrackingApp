package com.example.locationtrackingapp;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class updateLocation extends BroadcastReceiver {
    Context context1;
    String Longitude, Latitude;
    LocationListener locationListener;
    LocationManager locationManager;
    Date currentTime;
    Location l;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context1 = context;

//        Toast.makeText(context.getApplicationContext(),"hey hey .. its working",Toast.LENGTH_LONG).show();
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), notification);
//        mp.start();
        GetLocation getLocation= new GetLocation(context);
        l = getLocation.getCurrentLocation();
        Latitude = getLocation.updateLatitude(l);
        Longitude = getLocation.updateLongitude(l);

        currentTime = Calendar.getInstance().getTime();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");



        reference.keepSynced(true);
        reference
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Latitude")
                .setValue(Latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(),Latitude+" hey hey .. its working "+Longitude,Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
                    //to user profile
                }
                else{
                    Toast.makeText(context.getApplicationContext(),"Failed to update Location!",Toast.LENGTH_LONG).show();
                }
            }
        });
        reference
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Longitude")
                .setValue(Longitude);
        reference
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Time")
                .setValue(currentTime);
    }
}
