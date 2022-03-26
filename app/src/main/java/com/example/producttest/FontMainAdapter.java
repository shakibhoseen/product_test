package com.example.producttest;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;


import java.util.List;


public class FontMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIDEO_VIEW = 0;
    private final int STORY_VIEW = 1;
    private List<ProductModel> lists;
    private Context mcontext;
    private OnStoriesClickListener mListener;


    public interface OnStoriesClickListener {
        void onStoriesClick(int position);
    }

    public void setOnStoriesClickListener(OnStoriesClickListener listener) {
        mListener = listener;
    }

    public FontMainAdapter(List<ProductModel> lists, Context mcontext) {
        this.lists = lists;
        this.mcontext = mcontext;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIDEO_VIEW) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.child_item1, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.child_item2, parent, false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final ProductModel model = lists.get(position);

        if (holder instanceof ViewHolder1) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);


            ((ViewHolder1) holder).title.setText(model.getName());
            if (model.getVariantList() != null)
                if (model.getVariantList().size() > 0)
                    Glide.with(mcontext).load(model.getVariantList().get(0).getImageUrl()).centerCrop().into(((ViewHolder1) holder).productImg);

        } else if (holder instanceof ViewHolder2) {
            // story work
            ((ViewHolder2) holder).title.setText(model.getName());
            if (model.getVariantList() != null)
                if (model.getVariantList().size() > 0)
                    Glide.with(mcontext).load(model.getVariantList().get(0).getImageUrl()).centerCrop().into(((ViewHolder2) holder).productImg);

        } else {
            Toast.makeText(mcontext, "view3", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 3 == 0) {
            return VIDEO_VIEW;
        } else {
            return STORY_VIEW;
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView title;

        public ImageView productImg;


        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            //video
            title = itemView.findViewById(R.id.story_title_id);

            productImg = itemView.findViewById(R.id.image_id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onStoriesClick(position);
                        }
                    }
                }
            });

        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView title;

        public ImageView productImg;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            //story
            title = itemView.findViewById(R.id.story_title_id);

            productImg = itemView.findViewById(R.id.image_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onStoriesClick(position);
                        }
                    }
                }
            });

        }
    }


}





