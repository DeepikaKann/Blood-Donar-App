package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class List_Of_Donors extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<model> datalist;
    FirebaseFirestore db;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__of__donors);

        recview =(RecyclerView)findViewById(R.id.recyclerview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new myAdapter(datalist);
        recview.setAdapter(adapter);

        db= FirebaseFirestore.getInstance();
        db.collection("Donor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    model obj = d.toObject(model.class);
                    datalist.add(obj);

                }
                adapter.notifyDataSetChanged();
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
                        Intent int_logout = new Intent(List_Of_Donors.this,Admin_LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(List_Of_Donors.this,Admin_Home.class);
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
class model{
    String NIC_No;
    String Name;
    String Email;
    String Contact_No;
    String District;
    String Weight;
    String Blood_Group;
    String Day_of_the_last_blood_Donation;
    String Number_of_time_given_blood;
    String Suffer_from_chronic;

    public model(){}

    public model(String NIC_No, String name, String email, String contact_No, String district, String weight, String blood_Group, String day_of_the_last_blood_Donation, String number_of_time_given_blood,String suffer_from_chronic) {
        this.NIC_No = NIC_No;
        this.Name = name;
        this.Email = email;
        this.Contact_No = contact_No;
        this.District = district;
        this.Weight = weight;
        this.Blood_Group = blood_Group;
        this.Day_of_the_last_blood_Donation = day_of_the_last_blood_Donation;
        this.Number_of_time_given_blood = number_of_time_given_blood;
        this.Suffer_from_chronic = suffer_from_chronic;
    }

    public String getNIC_No() {
        return NIC_No;
    }

    public void setNIC_No(String NIC_No) {
        this.NIC_No = NIC_No;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getContact_No() {
        return Contact_No;
    }

    public void setContact_No(String contact_No) {
        this.Contact_No = contact_No;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        this.District = district;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        this.Weight = weight;
    }

    public String getBlood_Group() {
        return Blood_Group;
    }

    public void setBlood_Group(String blood_Group) {
        this.Blood_Group = blood_Group;
    }

    public String getDay_of_the_last_blood_Donation() {
        return Day_of_the_last_blood_Donation;
    }

    public void setDay_of_the_last_blood_Donation(String day_of_the_last_blood_Donation) {
        this.Day_of_the_last_blood_Donation = day_of_the_last_blood_Donation;
    }

    public String getNumber_of_time_given_blood() {
        return Number_of_time_given_blood;
    }

    public void setNumber_of_time_given_blood(String number_of_time_given_blood) {
        this.Number_of_time_given_blood = number_of_time_given_blood;
    }

    public String getSuffer_from_chronic() {
        return Suffer_from_chronic;
    }

    public void setSuffer_from_chronic(String suffer_from_chronic) {
        Suffer_from_chronic = suffer_from_chronic;
    }
}
//............................................Adapter................................
class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder>{
    ArrayList<model> datalist;

    public myAdapter(ArrayList<model> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_one_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.Nic.setText("NIC No : "+datalist.get(position).getNIC_No());
        holder.Name.setText("Name : "+datalist.get(position).getName());
        holder.Blood.setText("Blood Group : "+datalist.get(position).getBlood_Group());
        holder.District.setText("District : "+datalist.get(position).getDistrict());
        holder.Date.setText("Day of the last blood donation : "+datalist.get(position).getDay_of_the_last_blood_Donation());
        holder.ContactNo.setText("Contact No :"+datalist.get(position).getContact_No());
        holder.Weight.setText("Weight : "+datalist.get(position).getWeight());
        holder.Number_time.setText("Number of time given blood : "+datalist.get(position).getNumber_of_time_given_blood());
        holder.Email.setText("Email : "+datalist.get(position).getEmail());
        holder.Chronics.setText("Suffer from chronics : "+datalist.get(position).getSuffer_from_chronic());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Blood, District,Date,Email,ContactNo,Weight,Number_time,Nic,Chronics;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.txt_name);
            Blood = itemView.findViewById(R.id.txt_bloodGroup);
            District = itemView.findViewById(R.id.txt_district);
            Date = itemView.findViewById(R.id.txt_date);
            Email = itemView.findViewById(R.id.txt_email);
            ContactNo = itemView.findViewById(R.id.txt_contact);
            Weight = itemView.findViewById(R.id.txt_weight);
            Number_time = itemView.findViewById(R.id.txt_times);
            Nic = itemView.findViewById(R.id.txt_nic);
            Chronics = itemView.findViewById(R.id.txt_chronics);
        }
    }
}