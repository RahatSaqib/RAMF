package co.chatsdk.ui.threads;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.chatsdk.core.dao.Keys;
import co.chatsdk.core.dao.Thread;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.R;
import co.chatsdk.ui.contacts.SelectContactActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateThreadActivity extends SelectContactActivity {

    protected String threadEntityID = "";
    protected Thread thread;
    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private Query mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(R.string.new_chat);
        setMultiSelectEnabled(ChatSDK.config().groupsEnabled);
    }

    @Override
    protected void getDataFromBundle(Bundle bundle) {
        super.getDataFromBundle(bundle);
        threadEntityID = bundle.getString(Keys.IntentKeyThreadEntityID, threadEntityID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Keys.IntentKeyThreadEntityID, threadEntityID);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    protected void userSelectionChanged (List<User> users) {
        super.userSelectionChanged(users);
    }

    @Override
    protected void doneButtonPressed(List<User> users) {
        if (adapter.getSelectedCount() == 0) {
            showSnackbar(getString(R.string.pick_friends_activity_no_users_selected_toast));
            return;
        }

        // If there are more than 2 users then show a dialog to enter the name
        if(users.size() > 1) {
            ArrayList<String> userEntityIDs = new ArrayList<>();
            for (User u : users) {
                userEntityIDs.add(u.getEntityID());
            }
            ChatSDK.ui().startThreadEditDetailsActivity(this, null, userEntityIDs);
        }
        else {
            createAndOpenThread("", users);
        }
    }

    protected void createAndOpenThread (String name, List<User> users) {
        showProgressDialog(getString(R.string.pick_friends_activity_prog_dialog_open_new_convo_message));
        disposableList.add(ChatSDK.thread()
                .createThread(name, users)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((thread, throwable) -> dismissProgressDialog())
                .subscribe(thread -> {
                    ChatSDK.ui().startChatActivityForID(this, thread.getEntityID());
                    finish();
                }, toastOnErrorConsumer()));
    }



}
