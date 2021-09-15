package com.example.locationtrackingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register,forgetpassword;
    private EditText editTextEmail,editTextPassword;
    private Button SignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgetpassword = (TextView) findViewById(R.id.forgetpassword);
        forgetpassword.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

        SignIn = (Button) findViewById(R.id.login);
        SignIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }
//    private void keepuserSignedIn() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // User is signed in
//            Intent i = new Intent(MainActivity.this, MainActivity3.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        } else {
//            // User is signed out
//            Log.d(TAG, "onAuthStateChanged:signed_out");
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,MainActivity2.class));
                break;
            case R.id.login:
                UserLogin();
                break;
            case R.id.forgetpassword:
                startActivity(new Intent(this,MainActivity4.class));
                break;

        }
    }

    private void UserLogin() {
        String Email = editTextEmail.getText().toString().trim();
        String Password = editTextPassword.getText().toString().trim();
        if (Email.isEmpty()){
            editTextEmail.setError("Enter Email Id");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Provide Valide Email id");
            editTextEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //redirect to our app
                    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Login successfull",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this,MainActivity3.class));
                    }
                    else{
                        user.sendEmailVerification();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Verification Email has sent to Your email!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Login failed! invalid credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}