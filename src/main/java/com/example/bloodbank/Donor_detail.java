package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Donor_detail extends AppCompatActivity {
    FirebaseAuth Auth =FirebaseAuth.getInstance();
    FirebaseUser Fuser = Auth.getCurrentUser();
    public String UserId = Fuser.getUid();
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private TextView TextId, TextName,TextBlood;
    EditText TextLastDay,TextWeight,TextTimes,TextDistrict,TextContactNo;
    private Button Load,EditDetail;

    private static final String TAG= "Donor_detail";
    private static final String KEY_ID= "NIC_No";
    private static final String KEY_NAME= "Name";
    private static final String KEY_CONTACT_NO= "Contact_No";
    private static final String KEY_DISTRICT= "District";
    private static final String KEY_WEIGHT= "Weight";
    private static final String KEY_BLOOD_GROUP= "Blood_Group";
    private static final String KEY_LAST_DAY= "Day_of_the_last_blood_Donation";
    private static final String KEY_TIMES= "Number_of_time_given_blood";

    DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_detail);


        TextId = findViewById(R.id.txt_nic);
        TextName = findViewById(R.id.txt_name);
        TextWeight = findViewById(R.id.txt_weight);
        TextDistrict = findViewById(R.id.txt_district);
        TextBlood = findViewById(R.id.txt_blood);
        TextContactNo = findViewById(R.id.txt_contact);
        TextTimes = findViewById(R.id.txt_no_time);
        TextLastDay = findViewById(R.id.txt_date);

        Load= findViewById(R.id.btn_load);
        EditDetail= findViewById(R.id.btn_edit);

        //..........................................set Date...............................................
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        TextLastDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Donor_detail.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                TextLastDay.setText(date);
            }
        };

   //...................press load button...............................................
        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  fetchdata();
            }
        });
  //..................press edit button...................................................

        EditDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });




//.........................set navigation bar..................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Intent int_logout = new Intent(Donor_detail.this,LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(Donor_detail.this,Home.class);
                        startActivity(int_home);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
//................................................fetch data from database..................................................................

    public void fetchdata(){
        DocumentReference document = db.collection("Donor").document(UserId);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    final String Nic = documentSnapshot.getString(KEY_ID);
                    String Name = documentSnapshot.getString(KEY_NAME);
                    String Contact = documentSnapshot.getString(KEY_CONTACT_NO);
                    String District = documentSnapshot.getString(KEY_DISTRICT);
                    String Blood = documentSnapshot.getString(KEY_BLOOD_GROUP);
                    String Weight = documentSnapshot.getString(KEY_WEIGHT);
                    String LastDay = documentSnapshot.getString(KEY_LAST_DAY);
                    String Times = documentSnapshot.getString(KEY_TIMES);

                    TextId.setText(Nic);
                    TextName.setText(Name);
                    TextWeight.setText(Weight);
                    TextDistrict.setText(District);
                    TextContactNo.setText(Contact);
                    TextTimes.setText(LastDay);
                    TextLastDay.setText(Times);
                    TextBlood.setText(Blood);
                } else {
                    Toast.makeText(Donor_detail.this, "does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Donor_detail.this, "error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }
//.............................................edit profile.....................................................
    public void EditProfile(){
        String Weight, LastDay, District,NoOfTime,Contact;
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Donor").document(UserId);

        Weight= TextWeight.getText().toString();
        LastDay=TextLastDay.getText().toString();
        District=TextDistrict.getText().toString();
        NoOfTime=TextTimes.getText().toString();
        Contact=TextContactNo.getText().toString();

        Map<String,Object> note = new HashMap<>();
        note.put(KEY_WEIGHT,Weight);
        note.put(KEY_CONTACT_NO,Contact);
        note.put(KEY_LAST_DAY,LastDay);
        note.put(KEY_DISTRICT,District);
        note.put(KEY_TIMES,NoOfTime);


        docRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Donor_detail.this,"Edit profile Success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Donor_detail.this,"Error",Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

    }

}