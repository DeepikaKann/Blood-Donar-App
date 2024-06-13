package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
   private Button login, signUp, Admin;
   private EditText Email,Password;
   private FirebaseAuth auth;
   TextView forgotText;
   ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Admin = findViewById(R.id.btn_admin);
        login=findViewById(R.id.btn_login);
        signUp= findViewById(R.id.btn_signUp);
        Email = findViewById(R.id.ext_emaillog);
        Password = findViewById(R.id.ext_passwordlog);
        progressBar = findViewById(R.id.progress);
        forgotText= findViewById(R.id.forgotPassword);
        auth=FirebaseAuth.getInstance();

 //...........................press forgot password..................................

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(v.getContext());
                passwordDialog.setTitle("Reset Password ?");
                passwordDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordDialog.setView(resetMail);

                passwordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Link Sent To Your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error ! Reset Link is Not Sent",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordDialog.create().show();
            }
        });


//..........................press admin button.................................................
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Admin_Login.class));
                finish();
            }
        });

// .....press signUp.................................
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
                finish();
            }
        });

//..........press login....................................
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().equals("") || Password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_LONG).show();
                }else{
                    String txt_email = Email.getText().toString();
                    String txt_password = Password.getText().toString();
                    LoginUser(txt_email,txt_password);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void LoginUser(String txt_email, String txt_password) {
        auth.signInWithEmailAndPassword(txt_email,txt_password).addOnSuccessListener(authResult -> {
            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Incorrect entry",Toast.LENGTH_SHORT).show();
            }
        });
    }
//............if press back ......................................

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to Exit from App?")
                .setTitle("Exit")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog msgBox = builder1.create();
        msgBox.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
           startActivity(new Intent(MainActivity.this,Home.class));
           finish();
        }
    }
}