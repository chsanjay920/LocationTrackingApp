package com.example.locationtrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity{
    private EditText ResetEmail;
    private Button ResetButton;
    private ProgressBar progressBar;
    FirebaseAuth mauth;
    private Context ForgetPssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ResetButton = (Button) findViewById(R.id.resetButton);
        ResetEmail = (EditText) findViewById(R.id.resetEmail);
        progressBar = (ProgressBar) findViewById(R.id.pgbar);

        mauth= FirebaseAuth.getInstance();
        ResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = ResetEmail.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ResetEmail.setError("Enter Valid Email id");
            ResetEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetPssword,"Reset Link sent to the mail ",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(ForgetPssword,"Something Went Wrong",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}