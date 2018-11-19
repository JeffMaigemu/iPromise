package com.maigemu.jeffrey.ipromise;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maigemu.jeffrey.ipromise.data.AppStatus;

public class SignupActivity extends AppCompatActivity {

    private EditText reg_email_field, reg_password_field, reg_confirm_password_field;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin;
    private DatabaseReference mDatabaseUsers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");


        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reg_email_field = (EditText) findViewById(R.id.reg_email);
        reg_password_field = (EditText) findViewById(R.id.reg_password);
        reg_confirm_password_field = (EditText) findViewById(R.id.reg_confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.reg_progressBar);
        btnSignup = (Button) findViewById(R.id.reg_btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(loginIntent);

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = reg_email_field.getText().toString();
                String pass = reg_password_field.getText().toString();
                String confirm_pass = reg_confirm_password_field.getText().toString();

                btnLogin.setEnabled(false);

                if (TextUtils.isEmpty(email)) {
                    btnLogin.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(confirm_pass)) {
                    btnLogin.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Confirm password!", Toast.LENGTH_SHORT).show();
                    return;

                }


                if (TextUtils.isEmpty(pass)) {
                    btnLogin.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    btnLogin.setEnabled(false);

                    Toast toast = Toast.makeText(getApplicationContext(), "Please, enter a valid email address", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 80);
                    toast.show();
                    // Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                if (pass.equals(confirm_pass)){

                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendToMain();
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 80);
                                toast.show();

                            }


                        }
                    });
                } else {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast toast = Toast.makeText(getApplicationContext(), "Error. Username and password are not the same", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 80);
                    toast.show();

                }



            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){

            sendToMain();

        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}


