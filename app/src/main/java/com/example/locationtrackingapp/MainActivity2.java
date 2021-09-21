package com.example.locationtrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private EditText name,email,password;
    private TextView login;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(this);

        name = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.emailid);
        password = (EditText) findViewById(R.id.Password);
        login = (TextView) findViewById(R.id.login_text);

        login.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_text:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.register_button:
                registerUser();
                break;
                
        }
    }

    private <RegisterUser> void registerUser() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Name = name.getText().toString().trim();
        Date createdTime = null;

        if (Email.isEmpty()){
            email.setError("Enter Email Id");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Provide Valide Email id");
            email.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            password.setError("Enter Password");
            password.requestFocus();
            return;
        }
        if(Password.length() < 6){
            password.setError("Minimum 6 letters Password Required");
            password.requestFocus();
            return;
        }
        if(Name.isEmpty()){
            name.setError("enter Full Name");
            name.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(Name,Email,Password,"","",createdTime);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"User has been successfully registered",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        //to user profile
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Failed to Register!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failed to Register!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}