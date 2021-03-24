package sdk.chat.ui;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sdk.chat.ui.Admin.AdminCategoryActivity;
import sdk.chat.ui.Model.Users;

public class AdminLogin extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private Button mLogin_btn;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");


        mLoginProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Admins");


        mLoginEmail = (TextInputLayout) findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
        mLogin_btn = (Button) findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    Toast.makeText(AdminLogin.this,"Please write your email number...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(AdminLogin.this, "Please write your password...", Toast.LENGTH_SHORT).show();
                }

                else{

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(name, password);

                }

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    private void loginUser(final String name, final String password) {



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Admins").child(name).exists()) {
                    Users usersData = dataSnapshot.child("Admins").child(name).getValue(Users.class);
                    if (usersData.getPhone().equals(name))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(AdminLogin.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                            mLoginProgress.dismiss();

                            Intent intent = new Intent(AdminLogin.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            mLoginProgress.dismiss();
                            Toast.makeText(AdminLogin.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(AdminLogin.this, "Account with this " + name + "  do not exists.", Toast.LENGTH_SHORT).show();
                    mLoginProgress.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
