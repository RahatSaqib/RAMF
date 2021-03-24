package sdk.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;

import sdk.chat.core.Tab;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.chat.firebase.adapter.FirebasePaths;
import sdk.chat.firebase.adapter.wrappers.UserWrapper;
import sdk.chat.ui.HomeActivity;
import sdk.chat.ui.R;

import sdk.chat.ui.activities.MainActivity;
import sdk.chat.ui.adapters.PagerAdapterTabs;
import sdk.chat.ui.fragments.BaseFragment;
import sdk.chat.ui.icons.Icons;
import sdk.chat.ui.interfaces.SearchSupported;

public class LoginActivity extends MainActivity {

    protected PagerAdapterTabs adapter;

    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected RelativeLayout content;
    protected MaterialSearchView searchView;
    protected FrameLayout root;

    @Override
    protected @LayoutRes
    int getLayout() {
        return R.layout.activity_logindoc;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllRegisteredUsers();

        initViews();
    }

    @Override
    protected boolean searchEnabled() {
        return currentTab().fragment instanceof SearchSupported;
    }

    @Override
    protected void search(String text) {
        Fragment fragment = currentTab().fragment;
        if (fragment instanceof SearchSupported) {
            ((SearchSupported) fragment).filter(text);
        }
    }
    public static void getAllRegisteredUsers() {

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
                        ChatSDK.currentUser().addContact(uw.getModel(), ConnectionType.Contact);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected MaterialSearchView searchView() {
        return searchView;
    }

    protected void initViews() {
        super.initViews();

        toolbar=findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        content=findViewById(R.id.content);
        searchView=findViewById(R.id.searchView);
        root=findViewById(R.id.root);


        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Only creates the adapter if it wasn't initiated already
        if (adapter == null) {
            adapter = new PagerAdapterTabs(getSupportFragmentManager());
        }

        final List<Tab> tabs = adapter.getTabs();
        for (Tab tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.title));
        }

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOffscreenPageLimit(3);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
            tabSelected(tab);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(get(this, Icons.choose().user, Icons.shared().actionBarIconColor));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
    public Drawable get(Context context, IconicsDrawable icon, @ColorRes int colorRes) {
        return get(context, icon, colorRes, 0, 0);
    }

    private  Drawable get(Context context, IconicsDrawable icon, int colorRes, int width, int height) {
        int color = ContextCompat.getColor(context, colorRes);
        icon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER));

        if (width > 0) {
            icon.setSizeXPx(width);
        }
        if (height > 0) {
            icon.setSizeYPx(height);
        }

        return new BitmapDrawable(context.getResources(), icon.toBitmap());
    }

    public void tabSelected(TabLayout.Tab tab) {

        int index = tab.getPosition();

        viewPager.setCurrentItem(index);

        final List<Tab> tabs = adapter.getTabs();

//        Fragment currentFragment = adapter.getTabs().get(index).fragment;
//        if (getSupportActionBar() != null) {
//            if (currentFragment instanceof HasAppbar) {
//                getSupportActionBar().hide();
//            } else {
//                getSupportActionBar().show();
//            }
//        }

        updateLocalNotificationsForTab();

        // We mark the tab as visible. This lets us be more efficient with updates
        // because we only
        for (int i = 0; i < tabs.size(); i++) {
            Fragment fragment = tabs.get(i).fragment;
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) tabs.get(i).fragment).setTabVisibility(i == tab.getPosition());
            }
        }

        searchView.closeSearch();
    }

    public Tab currentTab() {
        return adapter.getTabs().get(viewPager.getCurrentItem());
    }

    public void updateLocalNotificationsForTab() {
        Tab tab = adapter.getTabs().get(tabLayout.getSelectedTabPosition());
        ChatSDK.ui().setLocalNotificationHandler(thread -> showLocalNotificationsForTab(tab.fragment, thread));
    }

    public void clearData() {
        for (Tab t : adapter.getTabs()) {
            if (t.fragment instanceof BaseFragment) {
                ((BaseFragment) t.fragment).clearData();
            }
        }
    }

    public void reloadData() {
        for (Tab t : adapter.getTabs()) {
            if (t.fragment instanceof BaseFragment) {
                ((BaseFragment) t.fragment).safeReloadData();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ChatSDK.ui().startProfileActivity(this, ChatSDK.currentUserID());
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {


            startActivity(new Intent(this,HomeActivity.class));
            finish();



    }
}
