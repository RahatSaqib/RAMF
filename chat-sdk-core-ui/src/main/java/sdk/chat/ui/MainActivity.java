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

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import io.paperdb.Paper;
import sdk.chat.ui.Prevalent.Prevalent;
import sdk.chat.ui.activities.MainAppBarActivity;

public class MainActivity extends AppCompatActivity
{
    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase,mAdminrDatabase;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD");
        mAdminrDatabase = FirebaseDatabase.getInstance().getReference().child("Admins");


        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, MainAppBarActivity.class);
                startActivity(intent);
            }
        });


        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, RegisterActivitydoc.class);
                startActivity(intent);
            }
        });
Paper.book().destroy();

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);




     if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {

                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){updateUI(currentUser);}
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account!=null){updateUI2(account);}


    }


    private void updateUI2(GoogleSignInAccount account) {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        intent.putExtra("Admin","google");
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void AllowAccess(final String email, final String password)
    {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    loadingBar.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();


                        }
                    });




                } else {

                    loadingBar.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(MainActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
