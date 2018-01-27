package com.siva.needred;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mAddress;
    private EditText mBlood;
    private EditText mMobile;

    private Button mCreateBtn;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;

    //progress dialog
    private ProgressDialog mRegProgress;
    private DatabaseReference mDatabase;

    String display_name;
    String email;
    String mobile;
    String password;
    String address;
    String blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mRegProgress=new ProgressDialog(this);

        mDisplayName=(EditText)findViewById(R.id.reg_username);
        mEmail=(EditText)findViewById(R.id.reg_email);
        mPassword=(EditText)findViewById(R.id.reg_pwd);
        mBlood=(EditText)findViewById(R.id.reg_blood);
        mMobile=(EditText)findViewById(R.id.reg_mobile);
        mAddress=(EditText)findViewById(R.id.reg_address);

        mCreateBtn=(Button)findViewById(R.id.register);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_name=mDisplayName.getText().toString();
                email=mEmail.getText().toString();
                password=mPassword.getText().toString();
                blood=mBlood.getText().toString();
                mobile=mMobile.getText().toString();
                address=mAddress.getText().toString();

                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||
                        !TextUtils.isEmpty(blood)||!TextUtils.isEmpty(mobile)||!TextUtils.isEmpty(address)){
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(display_name,email,password);
                }

            }
        });
    }

    private void register_user(final String display_name, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                    String uid=current_user.getUid();
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    //user.sendEmailVerification();
                    //mEmailverification.setTitle("Check your email and verify it");
                    //mEmailverification.setMessage("Verifying...");
                    // mEmailverification.show();
                    Boolean emailVerfied=user.isEmailVerified();
                    Log.e("Success", String.valueOf(emailVerfied));


                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("email", email);
                    userMap.put("password",password);
                    userMap.put("blood_group",blood);
                    userMap.put("mobile",mobile);
                    userMap.put("place",address);
                    userMap.put("device_token", device_token);
                    Toast.makeText(getApplicationContext(),"Registered Successfully..!",Toast.LENGTH_LONG).show();
                    mRegProgress.dismiss();
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //  mRegProgress.dismiss();
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        }

                    });

                }
                else{
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this,"Authentication failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
