package com.example.producttest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.producttest.R;
import com.example.producttest.account.StudentModel;

import java.util.List;

import javax.xml.namespace.QName;

public class OrderShowListAdapter extends RecyclerView.Adapter<OrderShowListAdapter.ViewHolder>{
  private List<StudentModel> models;
  private Context context;

    public OrderShowListAdapter(Context context,List<StudentModel> models) {
        this.models = models;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         StudentModel model = models.get(position);
          holder.name.setText(model.getUsername());
          holder.email.setText(model.getEmail());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
        }
    }
}
