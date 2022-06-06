package com.example.producttest;

import static com.example.producttest.account.Constant.PRODUCT_ORDER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.producttest.account.StudentModel;
import com.example.producttest.adapter.OrderShowListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowOrderListActivity extends AppCompatActivity {
 private RecyclerView recyclerView;
 private List<StudentModel> models;
  private   OrderShowListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_list);
        models = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_Id);
        String id = getIntent().getStringExtra("id");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OrderShowListAdapter(this ,models);
        recyclerView.setAdapter(adapter);
        if(id!=null)
        collectOrder(id);
    }


    private  void collectOrder(String productId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(PRODUCT_ORDER).child(productId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                models.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    StudentModel model = snapshot.getValue(StudentModel.class);
                    models.add(model);
                }
                Toast.makeText(ShowOrderListActivity.this, ""+models.size(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}