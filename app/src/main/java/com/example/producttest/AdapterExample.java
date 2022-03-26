package com.example.producttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterExample extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIDEO_VIEW = 0;
    private final int STORY_VIEW = 1;
    private List<ModelVariant> lists;
    private Context context;
    private  OnProductItemClickListener mListener;
    int pointerPosition;

    public interface OnProductItemClickListener {
        void onItemClick(int position);
    }

    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        mListener = listener;
    }

    public AdapterExample(List<ModelVariant> lists, Context context, int pointerPosition) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.child_item3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         ModelVariant modelVariant = lists.get(position);

        Glide.with(context).load(modelVariant.getImageUrl()).centerCrop().into(((ViewHolder) holder).productImg);

        if (pointerPosition==position){
            ((ViewHolder) holder).pointer.setVisibility(View.VISIBLE);
        }else{
            ((ViewHolder) holder).pointer.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;

        public ImageView productImg, pointer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.story_title_id);

            productImg = itemView.findViewById(R.id.image_id);
            pointer = itemView.findViewById(R.id.pointer_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }


                }
            });

        }
    }
}
