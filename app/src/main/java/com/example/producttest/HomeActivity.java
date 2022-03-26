package com.example.producttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FontMainAdapter.OnStoriesClickListener {
   private RecyclerView recyclerView;
   private List<ProductModel> productModelList;
   private  FontMainAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    FloatingActionButton addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addBtn = findViewById(R.id.add_btn_id);
        recyclerView = findViewById(R.id.recycler_Id);

        productModelList = new ArrayList<>();


        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FontMainAdapter(productModelList, this);

        adapter.setOnStoriesClickListener(this);

        recyclerView.setHasFixedSize(true);
       // recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getProduct();


        addBtn.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });
    }


    private void getProduct(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.PRODUCT);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ProductModel model = dataSnapshot.getValue(ProductModel.class);
                    productModelList.add(model);
                }

                int size = productModelList.size();
                for (int i =0; i<size; i++){
                    ProductModel model = productModelList.get(i);

                    getVariantList(model.getId(), i);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void getVariantList(String productId, int position){
        if (productId==null) return;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.VARIANT).child(productId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    return;
                }

                List<ModelVariant> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelVariant model = dataSnapshot.getValue(ModelVariant.class);
                   list.add(model);
                }
                productModelList.get(position).setVariantList(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onStoriesClick(int position) {
        ProductModel model = productModelList.get(position);
        if(model.getVariantList()==null) {
            Toast.makeText(this, "loading please wait or check internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (model.getVariantList().size()==0){
            Toast.makeText(this, "loading please wait or seems empty image", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", (Serializable) model );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}