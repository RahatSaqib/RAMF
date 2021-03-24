package sdk.chat.ui;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class DoctorRegister extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mPhone;
    private Button mCreateBtn;


    private Toolbar mToolbar;

    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerdoc);

        //Toolbar Set
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRegProgress = new ProgressDialog(this);



        // Firebase Auth

        mAuth = FirebaseAuth.getInstance();


        // Android Fields

//        mDisplayName = (TextInputLayout) findViewById(R.id.register_display_name);
//        mEmail = (TextInputLayout) findViewById(R.id.register_email);
//        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
//        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);
//        mPhone=(TextInputLayout) findViewById(R.id.register_phone);


//        mCreateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String display_name = mDisplayName.getEditText().getText().toString();
//                String email = mEmail.getEditText().getText().toString();
//                String password = mPassword.getEditText().getText().toString();
//                String phone = mPhone.getEditText().getText().toString();
//
//                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
//
//                    mRegProgress.setTitle("Registering User");
//                    mRegProgress.setMessage("Please wait while we create your account !");
//                    mRegProgress.setCanceledOnTouchOutside(false);
//                    mRegProgress.show();
//
//                    register_user(display_name, email, phone,password);
//
//                }
//
//
//
//            }
//        });


    }

    private void register_user(final String display_name, final String email, final String phone,final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = current_user.getUid();

                                mDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(uid);

                                String device_token = FirebaseInstanceId.getInstance().getToken();

                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("name", display_name);
                                userMap.put("status", "Hi there I'm using RAMF Chat App.");
                                userMap.put("phone", phone);
                                userMap.put("email", email);
                                userMap.put("password", password);
                                userMap.put("image", "default");
                                userMap.put("type", "Doctor");
                                userMap.put("thumb_image", "default");
                                userMap.put("address", "default");
                                userMap.put("device_token", device_token);

                                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            mRegProgress.dismiss();
                                            Toast.makeText(DoctorRegister.this, "Registered successfully please check your email", Toast.LENGTH_LONG).show();
//                                            Intent mainIntent = new Intent(RegisterActivitydoc.this, HomeActivity.class);
//                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            startActivity(mainIntent);
//                                            finish();


                                        }

                                    }
                                });

                }});
                            }







                else {

                    mRegProgress.hide();
                    Toast.makeText(DoctorRegister.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
