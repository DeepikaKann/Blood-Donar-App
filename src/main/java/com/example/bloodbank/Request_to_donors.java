package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Request_to_donors extends AppCompatActivity {
    RecyclerView recview3;
    ArrayList<model3> datalist3;
    FirebaseFirestore db;
    myAdapter3 adapter3;

    TextView District,Blood;
    String Rec_District,Rec_BloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_to_donors);
//................data from recipient........................................................
        District= findViewById(R.id.txt_district);
        Blood= findViewById(R.id.txt_Blood);

        Rec_BloodGroup =getIntent().getExtras().getString("BloodGroup");
        Rec_District= getIntent().getExtras().getString("District");


//..................................retview data from database.........................................

        recview3 =(RecyclerView)findViewById(R.id.recyclerview3);
        recview3.setLayoutManager(new LinearLayoutManager(this));
        datalist3 = new ArrayList<>();
        adapter3 = new myAdapter3(datalist3);
        recview3.setAdapter(adapter3);

        db= FirebaseFirestore.getInstance();
        db.collection("Donor")
                .whereEqualTo("District",Rec_District).whereEqualTo("Blood_Group",Rec_BloodGroup)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    model3 obj = d.toObject(model3.class);
                    datalist3.add(obj);

                }
                adapter3.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


//..................set navigation bar.............................................................
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        Intent int_logout = new Intent(Request_to_donors.this,Admin_LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(Request_to_donors.this,Admin_Home.class);
                        startActivity(int_home);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}

//..................................model class...............................
class model3{
    String Name;
    String NIC_No;
    String Contact_No;
    String Day_of_the_last_blood_Donation;


    public model3(){}

    public model3(String name, String NIC_No, String contact_No, String day_of_the_last_blood_Donation) {
        this.Name = name;
        this.NIC_No = NIC_No;
        this.Contact_No = contact_No;
        this.Day_of_the_last_blood_Donation = day_of_the_last_blood_Donation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNIC_No() {
        return NIC_No;
    }

    public void setNIC_No(String NIC_No) {
        this.NIC_No = NIC_No;
    }

    public String getContact_No() {
        return Contact_No;
    }

    public void setContact_No(String contact_No) {
        this.Contact_No = contact_No;
    }

    public String getDay_of_the_last_blood_Donation() {
        return Day_of_the_last_blood_Donation;
    }

    public void setDay_of_the_last_blood_Donation(String day_of_the_last_blood_Donation) {
        this.Day_of_the_last_blood_Donation = day_of_the_last_blood_Donation;
    }
}
//............................................Adapter................................
class myAdapter3 extends RecyclerView.Adapter<myAdapter3.MyViewHolder3>{
    ArrayList<model3> datalist3;

    public myAdapter3(ArrayList<model3> datalist) {
        this.datalist3 = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_requet_to_donor,parent,false);
        return new MyViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        holder.DName.setText("Donor's Name : "+datalist3.get(position).getName());
        holder.DNic.setText("ID No: "+datalist3.get(position).getNIC_No());
        holder.DContact.setText("Contact No : "+datalist3.get(position).getContact_No());
        holder.DLastDay.setText("Day of the last blood donation : "+datalist3.get(position).getDay_of_the_last_blood_Donation());

//.....................press request button................................
        holder.Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//............................msg sent to Blood Bank.........................................................................................................................
                String Msg = " Hi "+datalist3.get(position).getName()+ "!................\n You have received an invitation to donate blood. Our blood bank will contact you as soon as possible. Or call 0776135091 to make an appointment for you. \n Blood Bank";

                String number =datalist3.get(position).getContact_No();
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" +number ));
                sendIntent.putExtra("sms_body",Msg);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.Request.getContext().startActivity(sendIntent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return datalist3.size();
    }

    class MyViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView DName,DLastDay,DContact,DNic;
        Button Request,Delete;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            DName = itemView.findViewById(R.id.txt_name);
            DLastDay= itemView.findViewById(R.id.txt_Lastday);
            DNic =itemView.findViewById(R.id.txt_Nic);
            DContact = itemView.findViewById(R.id.txt_contact);
            Request =itemView.findViewById(R.id.btn_request);
            Delete= itemView.findViewById(R.id.btn_delete);


        }

        @Override
        public void onClick(View v) {

        }
    }
}