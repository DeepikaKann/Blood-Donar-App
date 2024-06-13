package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;

public class Recipient_List extends AppCompatActivity {
    RecyclerView recview2;
    ArrayList<model2> datalist2;
    myAdapter2 adapter2;
    FirebaseFirestore  db= FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient__list);

 //..................................retview data from database.........................................

        recview2 =(RecyclerView)findViewById(R.id.recyclerview2);
        recview2.setLayoutManager(new LinearLayoutManager(this));
        datalist2 = new ArrayList<>();
        adapter2 = new myAdapter2(datalist2);
        recview2.setAdapter(adapter2);


        db.collection("Recipient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){

                    String documentId =d.getId();

                    model2 obj = d.toObject(model2.class);

                    obj.setDocumentId(documentId);
                    datalist2.add(obj);

                }
                adapter2.notifyDataSetChanged();
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
                        Intent int_logout = new Intent(Recipient_List.this,Admin_LogOut.class);
                        startActivity(int_logout);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        Intent int_home = new Intent(Recipient_List.this,Admin_Home.class);
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
 class model2{
    String Name;
    String NIC_No;
    String District;
    String Blood_Group;
    String documentId;


    public model2(){}

    public model2(String name, String NIC_No, String district, String blood_Group) {
        this.Name = name;
        this.NIC_No = NIC_No;
        this.District = district;
        this.Blood_Group = blood_Group;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        this.District = district;
    }

    public String getBlood_Group() {
        return Blood_Group;
    }

    public void setBlood_Group(String blood_Group) {
        this.Blood_Group = blood_Group;
    }
}
//............................................Adapter................................
class myAdapter2 extends RecyclerView.Adapter<myAdapter2.MyViewHolder>{

   // DocumentReference docRef;
    ArrayList<model2> datalist2;


    public myAdapter2(ArrayList<model2> datalist) {
        this.datalist2 = datalist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_one_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.RName.setText("Recipient Name : "+datalist2.get(position).getName());
        holder.RNic.setText("ID No: "+datalist2.get(position).getNIC_No());
        holder.RBlood.setText("Needed Blood Group : "+datalist2.get(position).getBlood_Group());
        holder.RDistrict.setText(" Blood needed District "+datalist2.get(position).getDistrict());

        holder.Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.Ok.getContext(),Request_to_donors.class);
                intent.putExtra("BloodGroup",datalist2.get(position).getBlood_Group());
                intent.putExtra("District",datalist2.get(position).getDistrict());
                holder.Ok.getContext().startActivity(intent);

            }
        });

        //....................press delete button..............................................
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("....................................................."+datalist2.get(position).getDocumentId());
                FirebaseFirestore  db= FirebaseFirestore.getInstance();
                db.collection("Recipient").document(datalist2.get(position).getDocumentId()).delete();
                datalist2.remove(datalist2.get(position));
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return datalist2.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView RName,RDistrict,RBlood,RNic;
        Button Ok,Delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            RName = itemView.findViewById(R.id.txt_name);
            RDistrict = itemView.findViewById(R.id.txt_District);
            RNic =itemView.findViewById(R.id.txt_Nic);
            RBlood = itemView.findViewById(R.id.txt_Blood);
            Ok =itemView.findViewById(R.id.btn_ok);
            Delete= itemView.findViewById(R.id.btn_delete);


        }

        @Override
        public void onClick(View v) {

        }
    }
}

