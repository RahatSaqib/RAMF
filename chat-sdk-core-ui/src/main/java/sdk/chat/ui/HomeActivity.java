package sdk.chat.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import io.reactivex.Single;
import sdk.chat.core.dao.Keys;
import sdk.chat.core.dao.User;
import sdk.chat.core.notifications.NotificationBuilder;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.chat.firebase.adapter.FirebasePaths;
import sdk.chat.firebase.adapter.wrappers.UserWrapper;


import sdk.chat.ui.module.UIModule;
import sdk.chat.ui.utils.ToastHelper;
import sdk.guru.common.RX;


public class HomeActivity extends AppCompatActivity

{
    private DatabaseReference mUserRef,mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private CircleImageView mDisplayImage;


    private static String type = "",type1="";
    MaterialSearchView materialSearchView;


    private boolean isVisible;
    GoogleSignInClient mGoogleSignInClient;
    private FrameLayout mainFrame;
    private View profile;
    private tab1medicine med;
    private CartFragment cart;


    String[] list;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();


        }
        NotificationBuilder builder=new NotificationBuilder(this);
        Uri defSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSmallIcon(R.drawable.logoooo);
        builder.setSoundUri(defSoundUri);
        ChatSDK.config().setLogoDrawableResourceID(R.mipmap.ic_launcher);


        ChatSDK.ui().setMainActivity(LoginActivity.class);
        ChatSDK.ui().setLoginIntent(new Intent(this,HomeActivity.class));


        ChatSDK.auth().authenticate().subscribe(() -> {

        });
        getAllRegisteredUsers();
        UIModule.config().setPublicRoomsEnabled(false);


        mainFrame=findViewById(R.id.main_frame);
        profile=findViewById(R.id.profileFragment1);

        mAuth = FirebaseAuth.getInstance();
        med= new tab1medicine();
        cart = new CartFragment();
//        account = new ProfileFragment();
        profile.setVisibility(View.GONE);
        mainFrame.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,med).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(savedInstanceState==null){
//                    bottomNavigationView.setSelectedItemId(R.id.action_recents);
//                }
                int itemId = item.getItemId();
                if (itemId == R.id.action_recents) {
                    profile.setVisibility(View.GONE);
                    mainFrame.setVisibility(View.VISIBLE);
                    setFragment(med);


                    return true;
                } else if (itemId == R.id.action_cart) {
                    profile.setVisibility(View.GONE);
                    mainFrame.setVisibility(View.VISIBLE);

                    setFragment(cart);

                    return true;
                } else if (itemId == R.id.action_nearby) {
                    if(ChatSDK.currentUserID()==null){
                        Intent intent =new Intent(HomeActivity.this,RegisterActivitydoc.class);
                        startActivity(intent);
                        finish();

                    }
                    else {

                        mainFrame.setVisibility(View.GONE);
                        profile.setVisibility(View.VISIBLE);

                        String userEntityID = ChatSDK.currentUserID();
                        if (userEntityID != null && !userEntityID.isEmpty()) {
                            ChatSDK.db().fetchUserWithEntityIDAsync(userEntityID).observeOn(RX.main()).doOnSuccess(user -> {
                                if (user != null) {
                                    sdk.chat.ui.fragments.ProfileFragment fragment = (sdk.chat.ui.fragments.ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profileFragment1);
                                    fragment.setUser(user);
                                } else {
                                    ToastHelper.show(HomeActivity.this, R.string.user_entity_id_not_set);
                                    finish();
                                }
                            }).ignoreElement().subscribe();

                        }
                    }

                    return true;
                }
                return false;

            }
        });



        Paper.init(this);

    }
    public void getAllRegisteredUsers() {

        DatabaseReference ref = FirebasePaths.usersRef();
        final List<User> existingContacts = ChatSDK.contact().contacts();
        Query zone = ref.orderByChild("type").equalTo("Doctor");
        zone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        UserWrapper uw = new UserWrapper(child);
                        if (!existingContacts.contains(uw.getModel()) && !uw.getModel().isMe()) {
                            ChatSDK.contact().addContactLocal(uw.getModel(), ConnectionType.Contact).subscribe();
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();


    }


    @Override
    public void onBackPressed() {
        if(type.equals("Admin")){
            super.onBackPressed();

        }
        else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }
    }
    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(!type.equals("Admin")&& currentUser!=null){
//
//            mUserRef.child("online").setValue("true");
//
//        }



    }





    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

//        if(currentUser != null && !type.equals("Admin")) {
//
//            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
//
//        }


    }


    public static String getMystring()
    {
        return type;
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }




}
