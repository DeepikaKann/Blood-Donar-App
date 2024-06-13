package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Donor_register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String selectBlood;
    String selectDistrict;

//............ variables of database......................................................
    private static final String TAG= "Donor_register";

    private static final String KEY_ID= "NIC_No";
    private static final String KEY_NAME= "Name";
    private static final String KEY_EMAIL= "Email";
    private static final String KEY_CONTACT_NO= "Contact_No";
    private static final String KEY_DISTRICT= "District";
    private static final String KEY_WEIGHT= "Weight";
    private static final String KEY_BLOOD_GROUP= "Blood_Group";
    private static final String KEY_LAST_DAY= "Day_of_the_last_blood_Donation";
    private static final String KEY_TIMES= "Number_of_time_given_blood";
    private static final String KEY_CHRONIC= "Suffer_from_chronic";

    private EditText editTextId, editTextName,editTextEmail,editTextLastDay,editTextWeight,editTextTimes,editTextChronic,editTextContactNo;
    FirebaseAuth Auth =FirebaseAuth.getInstance();
    FirebaseUser Fuser = Auth.getCurrentUser();
    public String UserId = Fuser.getUid();
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collection = db.collection("Donor");

    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_register);


//............................initialized EditText.......................................................
        editTextId = findViewById(R.id.ext_nic);
        editTextName = findViewById(R.id.ext_name);
        editTextEmail=findViewById(R.id.ext_email);
        editTextContactNo = findViewById(R.id.ext_phone);
        editTextWeight = findViewById(R.id.ext_weight);
        editTextLastDay =findViewById(R.id.ext_date);
        editTextTimes = findViewById(R.id.ext_number);
        editTextChronic = findViewById(R.id.ext_chronic);

//..........................................set Date...............................................
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        editTextLastDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Donor_register.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                editTextLastDay.setText(date);
            }
        };


//.................................set Spinner to blood and district................................

        Spinner spinnerBlood = findViewById(R.id.ext_blood);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.Blood_Group,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlood.setAdapter(adapter1);
        spinnerBlood.setOnItemSelectedListener(this);

        Spinner spinnerDistric = findViewById(R.id.ext_district);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.District,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerDistric.setAdapter(adapter2);
        spinnerDistric.setOnItemSelectedListener(this);

//.........................set navigation bar..................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.logout:
                    Intent int_logout = new Intent(Donor_register.this,LogOut.class);
                    startActivity(int_logout);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.home:
                    Intent int_home = new Intent(Donor_register.this,Home.class);
                    startActivity(int_home);
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
//....................set spinner method.................................................................
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.ext_blood)
        {
            selectBlood = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId() == R.id.ext_district)
        {
            selectDistrict = parent.getItemAtPosition(position).toString();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

//....................Connect database(Create Donor table)....................................................

    public void Register(View v){
        String Nic = editTextId.getText().toString();
        String Name = editTextName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String ContactNo = editTextContactNo.getText().toString();
        String Weight = editTextWeight.getText().toString();
        String LastDay = editTextLastDay.getText().toString();
        String Time = editTextTimes.getText().toString();
        String Chronics = editTextChronic.getText().toString();

        Map<String,Object> note= new HashMap<>();
        note.put(KEY_ID,Nic);
        note.put(KEY_NAME,Name);
        note.put(KEY_EMAIL,Email);
        note.put(KEY_CONTACT_NO,ContactNo);
        note.put(KEY_DISTRICT,selectDistrict);
        note.put(KEY_WEIGHT,Weight);
        note.put(KEY_BLOOD_GROUP,selectBlood);
        note.put(KEY_LAST_DAY,LastDay);
        note.put(KEY_TIMES,Time);
        note.put(KEY_CHRONIC,Chronics);


        if (editTextId.getText().toString().equals("") || editTextName.getText().toString().equals("") || editTextEmail.getText().toString().equals("") || editTextContactNo.getText().toString().equals("")|| editTextWeight.getText().toString().equals("")|| editTextTimes.getText().toString().equals("")|| editTextChronic.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_LONG).show();
        }else if(Integer.parseInt(editTextWeight.getText().toString())<50){
            Toast.makeText(getApplicationContext(), "You can't became a donor.your weight is less than 50 kg", Toast.LENGTH_LONG).show();
        }else if(editTextContactNo.getText().toString().length()>10){
            Toast.makeText(getApplicationContext(), "Phone number is incorrect", Toast.LENGTH_LONG).show();
        } else {
            db.collection("Donor").document(UserId).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Donor_register.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Donor_register.this, Home.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Donor_register.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
            });
        }
    }
}