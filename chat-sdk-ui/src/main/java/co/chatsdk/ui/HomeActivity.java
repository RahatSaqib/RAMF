package co.chatsdk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import co.chatsdk.core.session.ChatSDK;

import co.chatsdk.ui.profile.ProfileFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity

{
    private DatabaseReference mUserRef,mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private CircleImageView mDisplayImage;


    private static String type = "",type1="";
    MaterialSearchView materialSearchView;

    private ImageView searchbtn;
    private TextView mName;

    private boolean isVisible;
    GoogleSignInClient mGoogleSignInClient;
    private FrameLayout mainFrame;
    private tab1medicine med;
    private CartFragment cart;
    private ProfileFragment account;

    String[] list;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
////        ViewPager viewPager = findViewById(R.id.view_pager);
////        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);

        mainFrame=findViewById(R.id.main_frame);
//        ChatSDK.auth().authenticate().subscribe(() -> {
//
//        });
        mAuth = FirebaseAuth.getInstance();
        med= new tab1medicine();
        cart = new CartFragment();
        account = new ProfileFragment();
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
                    setFragment(med);

                    return true;
                } else if (itemId == R.id.action_cart) {//                        Intent intent = new Intent(HomeActivity.this,CartActivity.class);
//                        startActivity(intent);
                    setFragment(cart);
                    return true;
                } else if (itemId == R.id.action_nearby) {
                    setFragment(account);
                    return true;
                }
                return false;

            }
        });
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





//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//        View headerView = navigationView.getHeaderView(0);
//        mName = headerView.findViewById(R.id.user_profile_name);
//        mDisplayImage = headerView.findViewById(R.id.user_profile_image);

//        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        String current_uid = mCurrentUser.getUid();
//
//        if (!type.equals("Admin")) {
//
//
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(current_uid);
//        mUserDatabase.keepSynced(true);
//
//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String name = dataSnapshot.child("name").getValue().toString();
//                final String image = dataSnapshot.child("image").getValue().toString();
//
////                mName.setText(name);
//
//
////                if (!image.equals("default")) {
////
////                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
////
////                    Picasso.get().load(image).into(mDisplayImage, new Callback() {
////                        @Override
////                        public void onSuccess() {
////                        }
////
////                        @Override
////                        public void onError(Exception e) {
////                            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
////                        }
////
////                    });
////
////                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


        searchbtn=(ImageView) findViewById(R.id.mysearch) ;
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(HomeActivity.this,SearchProductsActivity.class);
                startActivity(intent1);
            }
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();


    }


//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//        public SectionsPagerAdapter(FragmentManager fm){ super(fm);}
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//                    tab1medicine tab1= new tab1medicine();
//                    return  tab1;
//                case 1:
//                    tab2consult tab2= new tab2consult();
//                    return tab2;
//                default:
//                    return null;
//            }
//
//        }
//        @Override
//        public int getCount(){
//            return 2;
//        }
//        @Override
//        public CharSequence getPageTitle(int position){
//            switch (position){
//                case 0:
//                    return "Medicine";
//                case 1:
//                    return "Consult Doctor";
//            }
//            return null;
//        }
//    }




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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(!type.equals("Admin")&& currentUser!=null){

            mUserRef.child("online").setValue("true");

        }

    }





    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null && !type.equals("Admin")) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }


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
