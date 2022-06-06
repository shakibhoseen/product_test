package com.example.producttest.account;

import static com.example.producttest.account.Constant.SHARED_PREFS;
import static com.example.producttest.account.Constant.STATUS;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.producttest.MainActivity;
import com.example.producttest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogInActivity extends AppCompatActivity {
    MaterialEditText email, password;
    TextView goToRegisterPage, resetPass;
   public List<TeacherModel> teacherModelList;
    List<StudentModel> studentModelList;
    FirebaseAuth auth;
    DatabaseReference reference;

    private ProgressDialog progressDialog;



  private Button btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog =new ProgressDialog(LogInActivity.this);
        progressDialog.setMessage("Loging in");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        resetPass = findViewById(R.id.reset_password);

        btnLogIn = findViewById(R.id.btn_login);
        goToRegisterPage = findViewById(R.id.txtGoRegstrPageId);
        auth = FirebaseAuth.getInstance();

        teacherModelList = new ArrayList<>();
        studentModelList = new ArrayList<>();





        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();


                if( TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LogInActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                   // btnLogIn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        varify();



                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        btnLogIn.setClickable(true);
                                    }
                                }
                            });
                }




            }
        });



        goToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterPage.setTextColor(getResources().getColor(R.color.white));
                startActivity(new Intent(LogInActivity.this,RegisterActivity.class));
                finish();

            }
        });


        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass.setTextColor(getResources().getColor(R.color.white));
                String txt_email=email.getText().toString();
                if ( !TextUtils.isEmpty(txt_email)) {

                    auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Password Reset link sent successfully to your email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(LogInActivity.this, "please fill email address", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }










    public void varify(){


        String userid = FirebaseAuth.getInstance().getUid();
        if(userid==null){
            Toast.makeText(this, "user id null", Toast.LENGTH_SHORT).show();
            return ;
        }

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel model = snapshot.getValue(StudentModel.class);
                provideDecision(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void provideDecision(StudentModel model){
        if("admin".equals(model.getStatus())){
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STATUS, "admin");
            editor.apply();

        }else{
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(STATUS, "buyer");
            editor.apply();

        }

        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        progressDialog.dismiss();
    }


}








