package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogOut extends AppCompatActivity {
    FirebaseAuth Auth ;
    FirebaseUser user;
    private Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        Auth =FirebaseAuth.getInstance();
        user= Auth.getCurrentUser();

        Logout = findViewById(R.id.btn_logout);

 //...............press the logout button..............
         Logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Auth.signOut();
                 Toast.makeText(LogOut.this,"Logged Out",Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(LogOut.this,MainActivity.class));
                 finish();
             }
         });

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent int_home = new Intent(LogOut.this,Home.class);
                        startActivity(int_home);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout:
                        return true;
                }
                return false;
            }
        };

    }
}