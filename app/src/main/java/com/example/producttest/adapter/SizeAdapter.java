package com.example.producttest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.producttest.R;

import java.util.List;

import static com.example.producttest.R.drawable.box_style;
import static com.example.producttest.R.drawable.box_style2;

public class SizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> lists;
    private Context context;
    private  OnSizeItemClickListener mListener;
    int pointerPosition;

    public interface OnSizeItemClickListener {
        void onSizeItemClick(int position);
    }

    public void setOnSizeItemClickListener(OnSizeItemClickListener listener) {
        mListener = listener;
    }

    public SizeAdapter(List<String> lists, Context context, int pointerPosition) {
        this.lists = lists;
        this.context = context;
        this.pointerPosition = pointerPosition;

    }

    public void setPointerPosition(int pointerPosition){
        this.pointerPosition = pointerPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_btn_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         String st = lists.get(position);

        ((ViewHolder)holder).title.setText(st);

        if (pointerPosition==position){
            ((ViewHolder) holder).pointer.setBackground(context.getResources().getDrawable(box_style));
        }else{
            ((ViewHolder) holder).pointer.setBackground(context.getResources().getDrawable(box_style2));
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public LinearLayout pointer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_id);
            pointer = itemView.findViewById(R.id.liner_id);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onSizeItemClick(position);
                        }
                    }


                }
            });

        }
    }
}
