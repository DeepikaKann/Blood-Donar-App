package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin_Login extends AppCompatActivity {
    private EditText editTextUserName,editTextPassword;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        editTextUserName = findViewById(R.id.ext_username);
        editTextPassword = findViewById(R.id.ext_password);
        Login= findViewById(R.id.btn_login);

        String UName= "BloodBank123";
        String Passw = "s14365";

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserName = editTextUserName.getText().toString();
                String Password = editTextPassword.getText().toString();

                if(UserName.isEmpty() || Password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter all details",Toast.LENGTH_SHORT).show();
                }else if(UserName.equals(UName) && Password.equals(Passw)){
                    Toast.makeText(getApplicationContext(),"Loging succesull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admin_Login.this, Admin_Home.class));
                    finish();
                }

//                if ((UserName == "") && (Password == "")) {
//
//                }
            }
        });
    }
}