package com.project.ramfolinee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.ramfolinee.Prevalent.Prevalent;
import com.project.ramfolinee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.core.utils.DisposableList;
import co.chatsdk.ui.login.LoginActivity;
import co.chatsdk.ui.main.BaseActivity;
import io.paperdb.Paper;
import io.reactivex.disposables.Disposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RegisterActivitydoc extends  BaseActivity implements View.OnClickListener {

    private TextInputLayout mDisplayName;
    private static final String TAG = "FACELOG";
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
    private String display_name,image,email,id,password;
    private LoginButton loginButton,fb;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private int RC_SIGN_IN=0;


    private DatabaseReference mUserDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerdoc);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD");
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("60858819438-h7ojmutag8ilh8h5cr116bb3ql243itp.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        fb=findViewById(R.id.fb);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);





        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:

                       signIn();
                        break;
                    // ...
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //Toolbar Set
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRegProgress = new ProgressDialog(this);



        // Firebase Auth

        mAuth = FirebaseAuth.getInstance();


        // Android Fields

        mDisplayName = (TextInputLayout) findViewById(R.id.register_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.register_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);
        mPhone=(TextInputLayout) findViewById(R.id.register_phone);


        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String phone = mPhone.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(display_name, email, phone,password);

                }



            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    private void updateUI2(GoogleSignInAccount account) {
        Intent intent=new Intent(RegisterActivitydoc.this,HomeActivity.class);
        intent.putExtra("Admin","google");
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent=new Intent(RegisterActivitydoc.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    protected void afterLogin() {
        // We pass the extras in case this activity was launched by a push. In that case
        // we can load up the thread the text belongs to
       Intent intent=new Intent(RegisterActivitydoc.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadUserProfile(token);

                        } else {

                            Toast.makeText(RegisterActivitydoc.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser user = mAuth.getCurrentUser();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegisterActivitydoc.this);

                            if (acct != null) {
                                display_name = acct.getDisplayName();
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();
                               email = acct.getEmail();
                                password = acct.getId();
                                Uri personPhoto = acct.getPhotoUrl();
                               image=personPhoto.toString();

                            }
                            String uid = user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(uid);

                            String device_token = FirebaseInstanceId.getInstance().getToken();

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", display_name);
                            userMap.put("status", "Hi there I'm using RAMF Chat App.");
                            userMap.put("phone", "0");
                            userMap.put("email", email);
                            userMap.put("password", password);
                            userMap.put("image", image);
                            userMap.put("type", "default");
                            userMap.put("pid", uid);
                            userMap.put("thumb_image", "default");
                            userMap.put("address", "default");
                            userMap.put("device_token", device_token);

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        updateUI(user);

                                    }

                                }
                            });




                        } else {

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String idToken= account.getIdToken();
            firebaseAuthWithGoogle(idToken);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI2(null);
        }
    }


    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @SuppressLint("CheckResult")
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    display_name= object.getString("first_name");
                   email= object.getString("email");
                    String last_name= object.getString("last_name");
                     id= object.getString("id");


                    image= "https://graph.facebook.com/"+id+"/picture?type=normal";
                    password = object.getString("id");



                    final FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there I'm using RAMF Chat App.");
                    userMap.put("phone", "0");
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("image", image);
                    userMap.put("type", "default");
                    userMap.put("pid", uid);
                    userMap.put("thumb_image", "default");
                    userMap.put("address", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                updateUI(user);

                            }

                        }
                    });





                    RequestOptions requestOptions= new RequestOptions();
                    requestOptions.dontAnimate();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void register_user(final String display_name, final String email, final String phone,final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

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
                                userMap.put("type", "default");
                                userMap.put("pid", uid);
                                userMap.put("thumb_image", "default");
                                userMap.put("address", "default");
                                userMap.put("device_token", device_token);

                                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            mRegProgress.dismiss();

                                                            Intent mainIntent = new Intent(RegisterActivitydoc.this, LoginActivitydoc.class);

                                                            startActivity(mainIntent);
                                                            finish();
                                                        }






                                    }
                                });






                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivitydoc.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public boolean accountTypeEnabled(AccountDetails.Type type) {
        switch (type) {
            case Facebook:
                return ChatSDK.config().facebookLoginEnabled();
            case Twitter:
                return ChatSDK.config().twitterLoginEnabled();
            case Google:
                return ChatSDK.config().googleLoginEnabled();
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Action completion = this::afterLogin;

        Consumer<Throwable> error = throwable -> {
            ChatSDK.logError(throwable);
            Toast.makeText(RegisterActivitydoc.this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        };

        Action doFinally = this::dismissProgressDialog;
        if(i==R.id.fb){
            if(ChatSDK.socialLogin() != null) {
                disposableList.add(ChatSDK.socialLogin().loginWithFacebook(this).doOnError(error)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(doFinally)
                        .subscribe(completion, error));
            }
        }

    }
}
