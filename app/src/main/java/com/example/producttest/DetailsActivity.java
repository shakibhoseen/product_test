package com.example.producttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.producttest.adapter.ColorAdapter;
import com.example.producttest.adapter.SizeAdapter;

import java.util.List;

import static com.example.producttest.R.drawable.box_style;
import static com.example.producttest.R.drawable.box_style2;

public class DetailsActivity extends AppCompatActivity implements AdapterExample.OnProductItemClickListener,
        ColorAdapter.OnColorItemClickListener, SizeAdapter.OnSizeItemClickListener {
    private RecyclerView recyclerView, colorRecycler, sizeRecycler;
    private ProductModel productModel;

    TextView priceTxt, descriptionTxt, nameTxt, counterText, oldPriceTxt;
    ImageView showImage;

    LinearLayout minusLiner, plusLiner;
    String colorStr, sizeStr;

    private int safePosition = 0, count = 1;
    AdapterExample adapterExample;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        priceTxt = findViewById(R.id.price_id);
        oldPriceTxt = findViewById(R.id.old_price_id);
        descriptionTxt = findViewById(R.id.description_id);
        nameTxt = findViewById(R.id.name_id);
        showImage = findViewById(R.id.image_id);

        backBtn = findViewById(R.id.back_image_btn_id);

        minusLiner = findViewById(R.id.minus_liner_id);
        plusLiner = findViewById(R.id.plus_liner_id);

        counterText = findViewById(R.id.number_add_id);
        counterText.setText("" + 1);

        Bundle bundle1 = getIntent().getExtras();
        // quizModelList = new ArrayList<>();
        productModel = (ProductModel) bundle1.getSerializable("model");

        Toast.makeText(this, "" + productModel.getName(), Toast.LENGTH_SHORT).show();

        colorStr = productModel.getColorsBtnList().get(0);
        sizeStr = productModel.getSizeBtnList().get(0);

        recyclerView = findViewById(R.id.recycler_Id);
        colorRecycler = findViewById(R.id.recycler_color_id);
        sizeRecycler = findViewById(R.id.recycler_size_id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterExample = new AdapterExample(productModel.getVariantList(), this, safePosition);
        recyclerView.setAdapter(adapterExample);

        adapterExample.setOnProductItemClickListener(this);

        colorRecycler.setHasFixedSize(true);
        colorRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colorAdapter = new ColorAdapter(productModel.getColorsBtnList(), this, 0);
        colorRecycler.setAdapter(colorAdapter);
        colorAdapter.setOnColorItemClickListener(this);

        sizeRecycler.setHasFixedSize(true);
        sizeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sizeAdapter = new SizeAdapter(productModel.getSizeBtnList(), this, 0);
        sizeRecycler.setAdapter(sizeAdapter);
        sizeAdapter.setOnSizeItemClickListener(this);

        nameTxt.setText(productModel.getName());
        descriptionTxt.setText(productModel.getDescription());

        safePosition = findPosition(colorStr, sizeStr, 2);
        makeDistribution(safePosition);
        adapterExample.notifyDataSetChanged();


        minusLiner.setOnClickListener(view -> {
            if (count > 0) {
                count--;
                counterText.setText("" + count);
            } else {
                Toast.makeText(this, "you reached the minimum", Toast.LENGTH_SHORT).show();
            }
        });

        plusLiner.setOnClickListener(view -> {
            if (count < 100) {
                count++;
                counterText.setText("" + count);
            } else {
                Toast.makeText(this, "you reached the maximum", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }


    private void makeDistribution(int position) {
        ModelVariant modelVariant = productModel.getVariantList().get(position);

        Glide.with(DetailsActivity.this).load(modelVariant.getImageUrl()).into(showImage);
        String tk = getResources().getString(R.string.taka_name) + modelVariant.getNewPrice();
        priceTxt.setText(tk);
        //si.setText(modelVariant.getPrice());
        String tk2 = getResources().getString(R.string.taka_name) + modelVariant.getOldPrice();
        oldPriceTxt.setText(tk2);
        adapterExample.setPointerPosition(position);
        adapterExample.notifyDataSetChanged();
    }


    private int findPosition(String color, String size, int indicator) {

        boolean isFound = false, oneCheck=false;
        String tempColor = color, tempSize = size;
        int tempPosition = safePosition;

        int p = productModel.getVariantList().size();
        for (int i = 0; i < p; i++) {
            ModelVariant model = productModel.getVariantList().get(i);
            if (!oneCheck){
                if(indicator==Constant.COLOR_INT){
                    if(color.equals(model.getColor())){
                        tempSize = model.getSize();
                        tempPosition = i;
                        oneCheck =true;
                    }
                }else if(indicator==Constant.SIZE_INT){
                    if(size.equals(model.getSize())){
                        tempColor = model.getColor();
                        tempPosition = i;
                        oneCheck =true;
                    }
                }
            }

            if (color.equals(model.getColor()) && size.equals(model.getSize())) {
                safePosition = i;
                isFound = true;
                break;
            }
        }

        if(!isFound){
            Toast.makeText(this, size +" size "+color+" color product not available", Toast.LENGTH_SHORT).show();
            safePosition = tempPosition;
            colorStr = tempColor;
            sizeStr = tempSize;
        } else {

            colorStr = color;
            sizeStr = size;
        }

        setColorSelected(colorStr);
        setSizeSelected(sizeStr);
        colorAdapter.notifyDataSetChanged();
        sizeAdapter.notifyDataSetChanged();

        return safePosition;
    }

    private void setColorSelected(String color) {

        List<String> colorList = productModel.getColorsBtnList();
        int p = colorList.size();

        for (int i = 0; i < p; i++) {
            if (color.equals(colorList.get(i))) {
                colorAdapter.setPointerPosition(i);
                return;
            }
        }
    }

    private void setSizeSelected(String sizeBtn) {

        List<String> sizeList = productModel.getSizeBtnList();
        int p = sizeList.size();

        for (int i = 0; i < p; i++) {
            if (sizeBtn.equals(sizeList.get(i))) {
                sizeAdapter.setPointerPosition(i);
                return;
            }
        }
    }


    @Override
    public void onItemClick(int position) {
        colorStr = productModel.getVariantList().get(position).getColor();
        sizeStr = productModel.getVariantList().get(position).getSize();
        int x = findPosition(colorStr, sizeStr, 2);

        makeDistribution(x);
    }

    @Override
    public void onColorItemClick(int position) {
        String s = productModel.getColorsBtnList().get(position);
        int x = findPosition(s, sizeStr, Constant.COLOR_INT);
        makeDistribution(x);
    }

    @Override
    public void onSizeItemClick(int position) {
        String s = productModel.getSizeBtnList().get(position);
        int x = findPosition(colorStr, s, Constant.SIZE_INT);
        makeDistribution(x);
    }
}