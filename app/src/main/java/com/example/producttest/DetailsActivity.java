package com.example.producttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static com.example.producttest.R.drawable.box_style;
import static com.example.producttest.R.drawable.box_style2;

public class DetailsActivity extends AppCompatActivity implements AdapterExample.OnProductItemClickListener {
  private RecyclerView recyclerView;
  private ProductModel productModel;

  TextView priceTxt, descriptionTxt, nameTxt, counterText;
  ImageView showImage;

  LinearLayout smallLiner, largeLiner, whiteLiner, blackLiner, minusLiner, plusLiner;
  String colorStr , sizeStr;

  private int safePosition =0, count=1;
    AdapterExample adapterExample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        colorStr = Constant.WHITE;
        sizeStr = Constant.SMALL;

        priceTxt = findViewById(R.id.price_id);
        descriptionTxt = findViewById(R.id.description_id);
        nameTxt = findViewById(R.id.name_id);
        showImage = findViewById(R.id.image_id);

        smallLiner = findViewById(R.id.small_liner_id);
        largeLiner = findViewById(R.id.large_liner_id);
        whiteLiner = findViewById(R.id.white_liner_id);
        blackLiner = findViewById(R.id.black_liner_id);

        minusLiner = findViewById(R.id.minus_liner_id);
        plusLiner = findViewById(R.id.plus_liner_id);

        counterText = findViewById(R.id.number_add_id);
        counterText.setText(""+1);

        Bundle bundle1 = getIntent().getExtras();
        // quizModelList = new ArrayList<>();
        productModel = (ProductModel) bundle1.getSerializable("model");

        Toast.makeText(this, ""+productModel.getName(), Toast.LENGTH_SHORT).show();



        recyclerView = findViewById(R.id.recycler_Id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         adapterExample = new AdapterExample(productModel.getVariantList(), this, safePosition);
        recyclerView.setAdapter(adapterExample);

        adapterExample.setOnProductItemClickListener(this);

        nameTxt.setText(productModel.getName());
        descriptionTxt.setText(productModel.getDescription());

        safePosition = findPosition(Constant.WHITE, Constant.SMALL);
        makeDistribution(safePosition);
        adapterExample.notifyDataSetChanged();

        largeLiner.setOnClickListener(view -> {
            int x = findPosition(colorStr, Constant.LARGE);
            makeDistribution(x);
        });

        smallLiner.setOnClickListener(view -> {
            int x =  findPosition(colorStr, Constant.SMALL);
            makeDistribution(x);
        });

        whiteLiner.setOnClickListener(view -> {

            int x = findPosition(Constant.WHITE, sizeStr);
            makeDistribution(x);
        });

        blackLiner.setOnClickListener(view -> {
            int x = findPosition(Constant.BLACK, sizeStr);
            makeDistribution(x);
        });


        minusLiner.setOnClickListener(view -> {
            if(count>0){
                count--;
                counterText.setText(""+count);
            }else{
                Toast.makeText(this, "you reached the minimum", Toast.LENGTH_SHORT).show();
            }
        });

        plusLiner.setOnClickListener(view -> {
            if(count<100){
                count++;
                counterText.setText(""+count);
            }else{
                Toast.makeText(this, "you reached the maximum", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void makeDistribution(int position){
       ModelVariant modelVariant = productModel.getVariantList().get(position);

        Glide.with(DetailsActivity.this).load(modelVariant.getImageUrl()).centerCrop().into(showImage);
        priceTxt.setText(modelVariant.getPrice());
        //si.setText(modelVariant.getPrice());


        adapterExample.setPointerPosition(position);
        adapterExample.notifyDataSetChanged();
    }


    private int findPosition(String color, String size){
        colorStr = color;
        sizeStr = size;
        setBox();
        if (color.equals(Constant.WHITE)){
            whiteLiner.setBackground(getResources().getDrawable(box_style));
        }else{
            blackLiner.setBackground(getResources().getDrawable(box_style));
        }

        if (size.equals(Constant.SMALL)){
            smallLiner.setBackground(getResources().getDrawable(box_style));
        }else{
            largeLiner.setBackground(getResources().getDrawable(box_style));
        }



        int p = productModel.getVariantList().size();
        for (int i = 0; i <p ; i++) {
            ModelVariant model = productModel.getVariantList().get(i);
            if (color.equals(model.getColor()) && size.equals(model.getSize())){
                return i;
            }
        }
        return 0;
    }


    private void setBox(){
        smallLiner.setBackground(getResources().getDrawable(box_style2));
        largeLiner.setBackground(getResources().getDrawable(box_style2));
        whiteLiner.setBackground(getResources().getDrawable(box_style2));
        blackLiner.setBackground(getResources().getDrawable(box_style2));
    }

    @Override
    public void onItemClick(int position) {
        colorStr = productModel.getVariantList().get(position).getColor();
        sizeStr = productModel.getVariantList().get(position).getSize();
        int x =findPosition(colorStr, sizeStr);

       makeDistribution(x);
    }
}