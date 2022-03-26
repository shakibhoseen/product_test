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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE1 = 98;  //white small
    private static final int REQUEST_IMAGE2 = 99;   //black small
    private static final int REQUEST_IMAGE3 = 97;   //white large
    private static final int REQUEST_IMAGE4 = 96;   //black small
    private TextInputLayout name, description, smallWhiteAmount, largeWhiteAmount,
          smallBlackAmount,  largeBlackAmount;
  private Button whiteBtn, blackBtn, saveBtn, largeWhiteBtn, largeBlackBtn;
  Uri whiteImageUri, blackImageUri, largeWhiteUri, largeBlackUri;
  private String createId;
  StorageReference storageReference ;
  ImageView whiteImage, blackImage, largeWhiteImg, largeBlackImg;
  StorageTask<UploadTask.TaskSnapshot> whiteTask;
    StorageTask blackTask;

    private String urlWhiteImage, urlBlackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        name = findViewById(R.id.name_id);
        description = findViewById(R.id.description_id);
        smallBlackAmount = findViewById(R.id.small_black_id);
        smallWhiteAmount = findViewById(R.id.small_white_id);
        largeBlackAmount = findViewById(R.id.large_black_id);
        largeWhiteAmount = findViewById(R.id.large_white_id);

        whiteBtn = findViewById(R.id.choose_white_btn_id);
        blackBtn = findViewById(R.id.choose_black_btn_id);

        whiteImage = findViewById(R.id.white_image_id);
        blackImage = findViewById(R.id.black_image_id);

        largeWhiteBtn = findViewById(R.id.large_choose_white_btn_id);
        largeBlackBtn = findViewById(R.id.large_choose_black_btn_id);
        largeWhiteImg = findViewById(R.id.large_white_image_id);
        largeBlackImg = findViewById(R.id.large_black_image_id);





        saveBtn = findViewById(R.id.save_btn_id);




        whiteBtn.setOnClickListener(view -> {
            openImage(REQUEST_IMAGE1);
        });

        blackBtn.setOnClickListener(view -> {
            openImage(REQUEST_IMAGE2);
        });

        largeWhiteBtn.setOnClickListener(view -> {
            openImage(REQUEST_IMAGE3);
        });

        largeBlackBtn.setOnClickListener(view -> {
            openImage(REQUEST_IMAGE4);
        });


        saveBtn.setOnClickListener(view -> {
            String nameTxt = name.getEditText().getText().toString();
            String detailsTxt = description.getEditText().getText().toString();
            if (whiteImageUri== null || blackImageUri== null|| detailsTxt==null || nameTxt == null ||
                    largeWhiteUri== null || largeBlackUri== null){
                Toast.makeText(this, "All filed are required", Toast.LENGTH_SHORT).show();
                return;
            }

            saveDetails(nameTxt, detailsTxt);

        });

    }




    private void openImage(int REQUEST_IMAGE){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_IMAGE);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = MainActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(99>=requestCode && requestCode>=96&&resultCode ==RESULT_OK && data!=null && data.getData()!=null){

            if (requestCode == REQUEST_IMAGE1 ){
                whiteImageUri = data.getData();
                whiteImage.setImageURI(whiteImageUri);
               whiteImage.setVisibility(View.VISIBLE);


            } if (requestCode == REQUEST_IMAGE2 ){
                blackImageUri = data.getData();
                blackImage.setImageURI(blackImageUri);
                blackImage.setVisibility(View.VISIBLE);
            }

            if (requestCode == REQUEST_IMAGE3 ){
                largeWhiteUri = data.getData();
                largeWhiteImg.setImageURI(largeWhiteUri);
                largeWhiteImg.setVisibility(View.VISIBLE);
            }
            if (requestCode == REQUEST_IMAGE4 ){
                largeBlackUri = data.getData();
                largeBlackImg.setImageURI(largeBlackUri);
                largeBlackImg.setVisibility(View.VISIBLE);
            }

        } else Toast.makeText(MainActivity.this, "please select a file", Toast.LENGTH_SHORT).show();



    }


    private void saveDetails(String name, String details){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.PRODUCT);
        createId = reference.push().getKey();
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("saving ");
        pd.setCancelable(false);
        pd.show();

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("id", createId);
        hashMap.put("name", name);
        hashMap.put("description", details);

       reference.child(createId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                    threadBackground();
               }else{
                   Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
               pd.dismiss();
           }
       });

    }



    private void threadBackground(){



        try {
            saveVariant(Constant.SMALL, Constant.WHITE, smallWhiteAmount.getEditText().getText().toString(), whiteImageUri);
            Thread.sleep(200);
            saveVariant(Constant.SMALL, Constant.BLACK, smallBlackAmount.getEditText().getText().toString(), blackImageUri);
            Thread.sleep(200);
            saveVariant(Constant.LARGE, Constant.BLACK, largeBlackAmount.getEditText().getText().toString(), largeBlackUri);
            Thread.sleep(200);
            saveVariant(Constant.LARGE, Constant.WHITE, largeWhiteAmount.getEditText().getText().toString(), largeWhiteUri);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       /* Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    saveVariant(Constant.SMALL, Constant.WHITE, smallWhiteAmount.getEditText().getText().toString(), whiteImageUri);
                    Thread.sleep(200);
                    saveVariant(Constant.SMALL, Constant.BLACK, smallBlackAmount.getEditText().getText().toString(), blackImageUri);
                    Thread.sleep(200);
                    saveVariant(Constant.LARGE, Constant.BLACK, largeBlackAmount.getEditText().getText().toString(), blackImageUri);
                    Thread.sleep(200);
                    saveVariant(Constant.LARGE, Constant.WHITE, largeWhiteAmount.getEditText().getText().toString(), whiteImageUri);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();*/
    }




    private void saveVariant(String size, String color, String price, Uri uri){
        if (createId==null) return;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.VARIANT).child(createId);
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Uploading "+color +" "+ size);
        pd.setCancelable(false);
        pd.show();

        if(color.equals(Constant.WHITE)){
            if(urlWhiteImage!=null){
                HashMap<String , Object> map = new HashMap<>();
                map.put("imageUrl",urlWhiteImage);
                map.put("color",color);
                map.put("size",size);
                map.put("price",price);


                reference.push().setValue(map);
                pd.dismiss();

                return;
            }
        }else{
            if(urlBlackImage!=null){
                HashMap<String , Object> map = new HashMap<>();
                map.put("imageUrl",urlBlackImage);
                map.put("color",color);
                map.put("size",size);
                map.put("price",price);


                reference.push().setValue(map);
                pd.dismiss();

                return;
            }
        }



        if(uri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(uri));
            whiteTask = fileReference.putFile(uri);
            whiteTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        String mUri = downloadUri.toString();

                        if (color.equals(Constant.WHITE)){
                            urlWhiteImage = mUri;
                        }else{
                            urlBlackImage = mUri;
                        }

                        HashMap<String , Object> map = new HashMap<>();
                        map.put("imageUrl",mUri);
                        map.put("color",color);
                        map.put("size",size);
                        map.put("price",price);


                        reference.push().setValue(map);
                        pd.dismiss();

                    }else {
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
        }else Toast.makeText(MainActivity.this, "No file selected", Toast.LENGTH_SHORT).show();

    }


}