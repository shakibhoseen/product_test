package com.example.producttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout name, description;
    private Button saveBtn, addVariantBtn;
    ImageView backBtn;
    List<Model> lists;
    LinearLayout linearLayout;
    private String createId;
    StorageReference storageReference;

    StorageTask<UploadTask.TaskSnapshot> whiteTask;
    StorageTask blackTask;

    private String urlWhiteImage, urlBlackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lists = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        name = findViewById(R.id.name_id);
        description = findViewById(R.id.description_id);
        linearLayout = findViewById(R.id.layout_id);

        saveBtn = findViewById(R.id.save_btn_id);
        addVariantBtn = findViewById(R.id.add_btn_id);
        backBtn = findViewById(R.id.back_image_btn_id);

        saveBtn.setOnClickListener(view -> {
            String nameTxt = name.getEditText().getText().toString();
            String detailsTxt = description.getEditText().getText().toString();

            if (detailsTxt.isEmpty() || nameTxt.isEmpty()) {
                Toast.makeText(this, "All filed are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int len = lists.size();
            if (len == 0) {
                Toast.makeText(this, "please add at least one variant", Toast.LENGTH_SHORT).show();
                return;
            }


            for (int i = 0; i < len; i++) {
                Model m = lists.get(i);
                String color = Objects.requireNonNull(m.getColorInput().getEditText()).getText().toString();
                String size = Objects.requireNonNull(m.getSizeInput().getEditText()).getText().toString();
                String newAm = Objects.requireNonNull(m.getNewAmountInput().getEditText()).getText().toString();
                String oldAm = Objects.requireNonNull(m.getOldAmountInput().getEditText()).getText().toString();
                Uri uri = m.getImageUri();
                if (color.isEmpty() || size.isEmpty() || newAm.isEmpty() || oldAm.isEmpty() || uri == null) {
                    Toast.makeText(this, "input or image missing for " + (i + 1) + " th product ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            saveDetails(nameTxt, detailsTxt);

        });


        addVariantBtn.setOnClickListener(view -> {
            addViews();
        });

        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

    }


    private void openImage(int REQUEST_IMAGE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = MainActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int len = lists.size();
        if (len + 100 > requestCode && requestCode >= 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            for (int i = 0; i < len; i++) {
                if (requestCode == 100 + i) {
                    lists.get(i).setImageUri(data.getData());
                    ImageView imageView = lists.get(i).getView().findViewById(R.id.image_id);
                    imageView.setImageURI(data.getData());
                    imageView.setVisibility(View.VISIBLE);
                }
            }


        } else
            Toast.makeText(MainActivity.this, "please select a file", Toast.LENGTH_SHORT).show();


    }


    private void saveDetails(String name, String details) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.PRODUCT);
        createId = reference.push().getKey();
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("saving ");
        pd.setCancelable(false);
        pd.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", createId);
        hashMap.put("name", name);
        hashMap.put("description", details);

        reference.child(createId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    threadBackground();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        });

    }


    private void threadBackground() {
        try {
            int len = lists.size();
            for (int i = 0; i < len; i++) {
                Model m = lists.get(i);
                String color = Objects.requireNonNull(m.getColorInput().getEditText()).getText().toString();
                String size = Objects.requireNonNull(m.getSizeInput().getEditText()).getText().toString();
                String newAm = Objects.requireNonNull(m.getNewAmountInput().getEditText()).getText().toString();
                String oldAm = Objects.requireNonNull(m.getOldAmountInput().getEditText()).getText().toString();
                Uri uri = m.getImageUri();
                saveVariant(size, color, newAm, oldAm, uri, i);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void saveVariant(String size, String color, String newPrice, String oldPrice, Uri uri, int position) {
        if (createId == null) return;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.VARIANT).child(createId);
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Uploading " + color + " " + size);
        pd.setCancelable(false);
        pd.show();


        if (lists.get(position).getUrlString() != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("imageUrl", urlWhiteImage);
            map.put("color", color);
            map.put("size", size);
            map.put("newPrice", newPrice);
            map.put("oldPrice", oldPrice);


            reference.push().setValue(map);
            pd.dismiss();

            return;
        }


        if (uri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));
            whiteTask = fileReference.putFile(uri);
            whiteTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        String mUri = downloadUri.toString();


                        lists.get(position).setUrlString(mUri);


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageUrl", mUri);
                        map.put("color", color);
                        map.put("size", size);
                        map.put("newPrice", newPrice);
                        map.put("oldPrice", oldPrice);


                        reference.push().setValue(map);
                        pd.dismiss();

                    } else {
                        Toast.makeText(MainActivity.this, "failed to success", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(MainActivity.this, "No file selected", Toast.LENGTH_SHORT).show();

    }


    private void addViews() {
        View view = getLayoutInflater().inflate(R.layout.variant_type, null, false);
        TextInputLayout sizeInput = view.findViewById(R.id.size_id);
        TextInputLayout colorInput = view.findViewById(R.id.color_id);
        TextInputLayout oldAmountInput = view.findViewById(R.id.old_price_id);
        TextInputLayout newAmountInput = view.findViewById(R.id.new_price_id);
        ImageView remove = view.findViewById(R.id.remove_id);
        ImageView image = view.findViewById(R.id.image_id);
        Button choose = view.findViewById(R.id.choose_btn_id);


        remove.setOnClickListener(view1 -> {
            findRemove(view);
            // linearLayout.removeView(view);

        });

        choose.setOnClickListener(view1 -> {

            openImage(100 + findPosition(view));
        });


        linearLayout.addView(view);

        Model model = new Model(view, sizeInput, colorInput, newAmountInput, oldAmountInput, null);

        lists.add(model);
    }


    private void findRemove(View view) {
        int position = 0;
        boolean isFound = false;
        for (Model model : lists) {
            if (model.getView() == view) {
                break;
            }
            position++;
        }
        lists.remove(position);
       

        linearLayout.removeView(view);
    }


    private int findPosition(View view) {
        int position = 0;
        boolean isFound = false;
        for (Model model : lists) {
            if (model.getView() == view) {
                break;
            }
            position++;
        }
        return position;
    }


}