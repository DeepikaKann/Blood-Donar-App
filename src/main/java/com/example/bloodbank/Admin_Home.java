package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_Home extends AppCompatActivity {
    private Button Donor,Recipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);

        Donor= findViewById(R.id.btn_DonorList);
        Recipient= findViewById(R.id.btn_RequestRecipient);

//............................press List of Donor button....................................................
        Donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_Home.this,List_Of_Donors.class));
                finish();
            }
        });

//..................................press List of request from Recipient button...................................

        Recipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_Home.this,Recipient_List.class));
                finish();
            }
        });


//..................set navigation bar.............................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Intent int_logout = new Intent(Admin_Home.this,Admin_LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }
}