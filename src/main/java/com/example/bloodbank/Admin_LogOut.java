package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Admin_LogOut extends AppCompatActivity {
    Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__log_out);

        LogOut=findViewById(R.id.btn_logout);

        Intent intent = new Intent(Admin_LogOut.this,MainActivity.class);
        startActivity(intent);
    }
}