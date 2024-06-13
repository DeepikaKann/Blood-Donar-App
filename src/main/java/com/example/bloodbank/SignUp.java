package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    String UserId;
    String TAG= "SignUp";

    private EditText Name,Email,Nic,Password;
    private Button Ok,Cancel;

    FirebaseAuth Auth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name =findViewById(R.id.ext_name);
        Nic= findViewById(R.id.etx_nic);
        Email= findViewById(R.id.ext_email);
        Password = findViewById(R.id.ext_password);
        Ok = findViewById(R.id.btn_ok);
        Cancel =findViewById(R.id.btn_cancel);


 //............ok button press..............
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name =  Name.getText().toString();
                String txt_nic = Nic.getText().toString();
                String txt_email = Email.getText().toString();
                String txt_Password = Password.getText().toString();
                Auth= FirebaseAuth.getInstance();
                fstore=FirebaseFirestore.getInstance();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_Password) ||TextUtils.isEmpty(txt_name)|| TextUtils.isEmpty(txt_nic)){
                    Toast.makeText(SignUp.this,"Empty credentials ",Toast.LENGTH_SHORT).show();
                }else if(txt_Password.length()<6){
                    Toast.makeText(SignUp.this,"Password too short",Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_email,txt_Password,txt_nic,txt_name);
                }
            }

            private void registerUser(String txt_email, String txt_password,String txt_nic, String txt_name) {
                Auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this,"Registration Successful ",Toast.LENGTH_SHORT).show();
                        UserId = Auth.getCurrentUser().getUid();
                        DocumentReference documentReference =fstore.collection("User").document(UserId);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",txt_name);
                        user.put("Email",txt_email);
                        user.put("Nic",txt_nic);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"User profile is create");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"User profile is create"+e.toString());
                            }
                        });
                        startActivity(new Intent(SignUp.this,MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(SignUp.this,"Registration failed ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

 //..............cancel button press...............

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUp.this,"Registration failed ",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this,MainActivity.class));
                finish();
            }
        });

    }
}