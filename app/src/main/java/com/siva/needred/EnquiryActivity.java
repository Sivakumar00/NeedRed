package com.siva.needred;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EnquiryActivity extends AppCompatActivity {

    //UI
    Button btnRequest;
    EditText edtName,edtBlood,edtPlace,edtMobile;
    //DB
    DatabaseReference mHelper;
    FirebaseAuth mAuth;
    //progress
    ProgressDialog mProgress;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        //initialisation
        edtBlood=(EditText)findViewById(R.id.enq_blood);
        edtMobile=(EditText)findViewById(R.id.enq_mobile);
        edtName=(EditText)findViewById(R.id.enq_name);
        edtPlace=(EditText)findViewById(R.id.enq_place);
        btnRequest=(Button)findViewById(R.id.button2);
        //firebase
        mHelper= FirebaseDatabase.getInstance().getReference();
        final String mCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mAuth=FirebaseAuth.getInstance();
        //progress
        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait..");

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.show();
                String blood=edtBlood.getText().toString();
                String name=edtName.getText().toString();
                String mobile=edtMobile.getText().toString();
                String  place=edtPlace.getText().toString();
                String temp=blood.toUpperCase();
                if(!TextUtils.isEmpty(blood)||!TextUtils.isEmpty(name)||!TextUtils.isEmpty(mobile)||
                        !TextUtils.isEmpty(place)){

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("blood_group", blood);
                        userMap.put("mobile", mobile);
                        userMap.put("place", place);

                        mHelper.child("Help").child(mCurrentUser).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Registered Successfully..!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });

            }else{
                    Toast.makeText(getApplicationContext(),"Please enter the details in all fields",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}

