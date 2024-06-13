package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Recipient_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String selectBlood;
    String selectDistrict;

    private EditText editTextNic, editTextName, editTextContact;
    private Button FindDonor;

//............ variables of database......................................................
    private static final String TAG= "Recipient_page";

    private static final String KEY_ID= "NIC_No";
    private static final String KEY_NAME= "Name";
    private static final String KEY_CONTACT_NO= "Contact_No";
    private static final String KEY_DISTRICT= "District";
    private static final String KEY_BLOOD_GROUP= "Blood_Group";

    FirebaseAuth Auth =FirebaseAuth.getInstance();
    FirebaseUser Fuser = Auth.getCurrentUser();
    public String UserId = Fuser.getUid();
    private FirebaseFirestore db =FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_page);

//............................initialized EditText.......................................................
        editTextName = findViewById(R.id.ext_name);
        editTextNic = findViewById(R.id.ext_nic);
        editTextContact = findViewById(R.id.ext_phone);
        FindDonor = findViewById(R.id.btn_find);

//.................................set Spinner to blood and district................................

        Spinner spinnerBlood1 = findViewById(R.id.ext_blood);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.Blood_Group,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlood1.setAdapter(adapter3);
        spinnerBlood1.setOnItemSelectedListener(this);

        Spinner spinnerDistric1 = findViewById(R.id.ext_district);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.District,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerDistric1.setAdapter(adapter4);
        spinnerDistric1.setOnItemSelectedListener(this);

//.............................press button.........................................................

        FindDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });


//.........................set navigation bar..................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Intent int_logout = new Intent(Recipient_page.this,LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(Recipient_page.this,Home.class);
                        startActivity(int_home);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
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

    public void Register(){
        String Nic = editTextNic.getText().toString();
        String Name = editTextName.getText().toString();
        String ContactNo = editTextContact.getText().toString();

        Map<String,Object> note= new HashMap<>();
        note.put(KEY_ID,Nic);
        note.put(KEY_NAME,Name);
        note.put(KEY_CONTACT_NO,ContactNo);
        note.put(KEY_DISTRICT,selectDistrict);
        note.put(KEY_BLOOD_GROUP,selectBlood);

        if(editTextNic.getText().toString().equals("") || editTextName.getText().toString().equals("") || editTextContact.getText().toString().equals("") ){
            Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_LONG).show();
        }else if(editTextContact.getText().toString().length()>10){
            Toast.makeText(getApplicationContext(), "Phone number is incorrect", Toast.LENGTH_LONG).show();
        }else {


//............................msg sent to Blood Bank.........................................................................................................................
            String Msg = "............. A new Blood request has arrived. Pay attention to that.............\nRecipient Name : " + Name + "\nRecipient NIC : " + Nic + "\nNeeded Blood Group : " + selectBlood + "\nDistrict : " + selectDistrict + "\nContact No : " + ContactNo;

            String number = "0776135091";
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
            sendIntent.putExtra("sms_body", Msg);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendIntent);
            Toast.makeText(Recipient_page.this, "Successfully Send a Msg to Blood Group", Toast.LENGTH_SHORT).show();

//.............................................................................................................................................................................


            db.collection("Recipient").document(UserId).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Recipient_page.this, "Data saved", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Recipient_page.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
            });

         //...................................All recipient...............................

            db.collection("AllRecipient").document(UserId).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, e.toString());
                }
            });
        }
    }

}