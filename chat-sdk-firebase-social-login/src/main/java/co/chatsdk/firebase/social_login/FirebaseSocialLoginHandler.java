package co.chatsdk.firebase.social_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;


import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import sdk.chat.core.handlers.SocialLoginHandler;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.chat.firebase.adapter.FirebaseAuthenticationHandler;
import sdk.chat.firebase.adapter.FirebaseCoreHandler;

/**
 * Created by ben on 9/4/17.
 */

public class FirebaseSocialLoginHandler implements SocialLoginHandler {

    // Facebook
    private CallbackManager facebookCallbackManager;

    // Google
    private GoogleSignInOptions gso;
    private GoogleApiClient googleClient;
    private GoogleSignInCompleteListener googleSignInCompleteListener;

    // Twitter


    private static int RC_GOOGLE_SIGN_IN = 200;

    public FirebaseSocialLoginHandler (Context context) {


            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("60858819438-h7ojmutag8ilh8h5cr116bb3ql243itp.apps.googleusercontent.com")
                    .requestEmail()
                    .build();


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



    }

    @Override
    public boolean accountTypeEnabled(AccountDetails.Type type) {
        switch (type) {
            case Facebook:
                return ChatSDK.config().facebookLoginEnabled();
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
