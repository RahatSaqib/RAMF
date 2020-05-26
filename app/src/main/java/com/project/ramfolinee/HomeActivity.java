package com.project.ramfolinee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.ramfolinee.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;


import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import co.chatsdk.core.interfaces.InterfaceAdapter;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseNetworkAdapter;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.profile.pictures.ProfilePicturesModule;
import co.chatsdk.ui.login.LoginActivity;
import co.chatsdk.ui.login.SplashScreenActivity;
import co.chatsdk.ui.manager.BaseInterfaceAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity

{
    private DatabaseReference mUserRef,mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private CircleImageView mDisplayImage;
    private InterstitialAd mInterstitialAd;

    private static String type = "",type1="";
    MaterialSearchView materialSearchView;

    private ImageView searchbtn;
    private TextView mName;
    private ScheduledExecutorService scheduler;
    private boolean isVisible;
    GoogleSignInClient mGoogleSignInClient;

    String[] list;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        ChatSDK.auth().authenticate().subscribe(() -> {

        });
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        if (!type.equals("Admins")) {
                            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.action_favorites:
                        Intent intent = new Intent(HomeActivity.this,SplashScreenActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(HomeActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });






        prepareAd();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Admin")).toString();



        }
        else if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("UsersD").child(mAuth.getCurrentUser().getUid());

        }


        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//        View headerView = navigationView.getHeaderView(0);
//        mName = headerView.findViewById(R.id.user_profile_name);
//        mDisplayImage = headerView.findViewById(R.id.user_profile_image);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        if (!type.equals("Admin")) {



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

//                mName.setText(name);


//                if (!image.equals("default")) {
//
//                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
//
//                    Picasso.get().load(image).into(mDisplayImage, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
//                        }
//
//                    });
//
//                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


        searchbtn=(ImageView) findViewById(R.id.mysearch) ;
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(HomeActivity.this,SearchProductsActivity.class);
                startActivity(intent1);
            }
        });


    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm){ super(fm);}

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    tab1medicine tab1= new tab1medicine();
                    return  tab1;
                case 1:
                    tab2consult tab2= new tab2consult();
                    return tab2;
                default:
                    return null;
            }

        }
        @Override
        public int getCount(){
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Medicine";
                case 1:
                    return "Consult Doctor";
            }
            return null;
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(type.equals("Admin")){
            super.onBackPressed();

        }
        else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        } else if(!type.equals("Admin")){

            mUserRef.child("online").setValue("true");

        }
        isVisible = true;
            if(scheduler==null){
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() { runOnUiThread(new Runnable() {
                        public void run() {
                            if (mInterstitialAd.isLoaded() && isVisible) {
                                mInterstitialAd.show();
                            }
                            prepareAd();

                        }
                    });
                }
            }, 20, 1, TimeUnit.SECONDS);
            }







    }


    private void prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7052071124298836/9993366630");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null && !type.equals("Admin")) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }
        scheduler.shutdownNow();
        scheduler = null;
        isVisible =false;

    }

    private void sendToStart() {

        Intent startIntent = new Intent(HomeActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item)
//    {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_cart)
//        {
//            if (!type.equals("Admins"))
//            {
//                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
//                startActivity(intent);
//            }
//        }
//        else if (id == R.id.nav_orders)
//        {
//            if (!type.equals("Users"))
//            {
//                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
//                startActivity(intent);
//            }
//        }
//
//        else if (id == R.id.nav_settings)
//        {
//            if (!type.equals("Admins"))
//            {
//                Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
//                startActivity(intent);
//            }
//        }
//
//        else if (id == R.id.nav_about)
//        {
//            if (!type.equals("Admins"))
//            {
//                Intent intent = new Intent(HomeActivity.this, AboutUs.class);
//                startActivity(intent);
//            }
//        }
//
//
//
//        else if (id == R.id.nav_logout)
//        {
//            if (!type.equals("Admins"))
//            {
//                Paper.book().destroy();
//                mAuth.signOut();
//                LoginManager.getInstance().logOut();
//                if(type.equals("google")){
//                    signOut();
//                }
//
//
//                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
    public static String getMystring(){
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
