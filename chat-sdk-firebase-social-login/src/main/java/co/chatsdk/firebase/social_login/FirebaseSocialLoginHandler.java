package co.chatsdk.firebase.social_login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.chatsdk.core.handlers.SocialLoginHandler;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.firebase.FirebaseAuthenticationHandler;
import co.chatsdk.firebase.FirebaseCoreHandler;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by ben on 9/4/17.
 */

public class FirebaseSocialLoginHandler implements SocialLoginHandler {

    // Facebook
    private CallbackManager facebookCallbackManager;
    private String display_name,image,email,id,password;

    // Google
    private GoogleSignInOptions gso;
    private GoogleApiClient googleClient;
    private GoogleSignInCompleteListener googleSignInCompleteListener;

    // Twitter
    TwitterLoginButton twitterButton;

    private static int RC_GOOGLE_SIGN_IN = 200;

    public FirebaseSocialLoginHandler (Context context) {

        if(accountTypeEnabled(AccountDetails.Type.Google)) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(ChatSDK.config().googleWebClientKey)
                    .requestEmail()
                    .build();
        }
        if(accountTypeEnabled(AccountDetails.Type.Twitter)) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(ChatSDK.config().twitterKey, ChatSDK.config().twitterSecret);
            TwitterConfig config = new TwitterConfig.Builder(context).twitterAuthConfig(authConfig).build();
            Twitter.initialize(config);
        }
    }

    interface GoogleSignInCompleteListener {
        void complete (GoogleSignInResult result);
    }

    @Override
    public Completable loginWithFacebook(final Activity activity) {
        return Single.create((SingleOnSubscribe<AuthCredential>) e -> {

            LoginButton button = new LoginButton(activity);
            facebookCallbackManager = CallbackManager.Factory.create();
            button.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {


                    e.onSuccess(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()));

                    loadUserProfile(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    e.onError(null);
                }

                @Override
                public void onError(FacebookException error) {
                    e.onError(error);
                }
            });

            button.callOnClick();

        }).flatMapCompletable(authCredential -> signInWithCredential(activity, authCredential));
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
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();



                    final FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();

                   DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(uid);

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


    @Override
    public Completable loginWithTwitter(final Activity activity) {
        return Single.create((SingleOnSubscribe<AuthCredential>) e -> {

            twitterButton = new TwitterLoginButton(activity);
            twitterButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    e.onSuccess(TwitterAuthProvider.getCredential(result.data.getAuthToken().token, result.data.getAuthToken().secret));

                }

                @Override
                public void failure(TwitterException exception) {
                    e.onError(exception);
                }
            });
            twitterButton.callOnClick();

        }).flatMapCompletable(authCredential -> signInWithCredential(activity, authCredential));
    }

    @Override
    public Completable loginWithGoogle(final Activity activity) {
        return Single.create((SingleOnSubscribe<AuthCredential>) e -> {

            googleClient = new GoogleApiClient.Builder(activity)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient);
            activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);

            googleSignInCompleteListener = result -> {
                if(result.isSuccess()) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(result.getSignInAccount().getIdToken(), null);
                    e.onSuccess(credential);
                }
                else {
                    e.onError(new Exception(result.getStatus().toString()));
                }
            };

        }).flatMapCompletable(authCredential -> signInWithCredential(activity, authCredential));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(facebookCallbackManager != null) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInCompleteListener != null) {
                googleSignInCompleteListener.complete(result);
            }
        }

        if(twitterButton != null) {
            twitterButton.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
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
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    public Completable signInWithCredential (final Activity activity, final AuthCredential credential) {
        return Completable.create(e -> FirebaseCoreHandler.auth().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful() && task.isComplete()) {
                        FirebaseUser user = FirebaseCoreHandler.auth().getCurrentUser();

                        FirebaseAuthenticationHandler handler = (FirebaseAuthenticationHandler) ChatSDK.auth();

                        handler.authenticateWithUser(user).subscribe(e::onComplete, e::onError);
                    }
                    else {
                        e.onError(task.getException());
                    }
                }));
    }



}
