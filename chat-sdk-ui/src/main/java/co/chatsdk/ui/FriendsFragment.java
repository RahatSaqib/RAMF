package co.chatsdk.ui;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import co.chatsdk.ui.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.profile.ProfileActivity;


public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private Query mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").orderByChild("type").equalTo("Doctor");
        mFriendsDatabase=FirebaseDatabase.getInstance().getReference().child("UsersD");
        mUsersDatabase.keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<UsersD> options =
                new FirebaseRecyclerOptions.Builder<UsersD>()
                        .setQuery(mUsersDatabase, UsersD.class)
                        .build();


        FirebaseRecyclerAdapter<UsersD, UsersViewHolder> adapter =
                new FirebaseRecyclerAdapter<UsersD, UsersViewHolder>(options) {


                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int i, @NonNull final UsersD model)
                    {
                        holder.name.setText(model.getName());
                        holder.status.setText(model.getStatus());
                        final String list_user_id = getRef(i).getKey();


                        mFriendsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final String userName = dataSnapshot.child("name").getValue().toString();
                                String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                                if(dataSnapshot.hasChild("online")) {

                                    String userOnline = dataSnapshot.child("online").getValue().toString();


                                }

                                holder.name.setText(userName);

                                Picasso.get().load(userThumb).placeholder(R.drawable.default_avatar).into(holder.userImageView);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                        builder.setTitle("Select Options");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //Click Event for each item.
                                                if(i == 0){

                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id);
                                                    startActivity(profileIntent);

                                                }

                                                if(i == 1){

                                                    ChatSDK.ui().startChatActivityForID(getContext(), list_user_id);

//                                                    chatIntent.putExtra("user_id", list_user_id);
//                                                    chatIntent.putExtra("user_name", userName);
//                                                    startActivity(chatIntent);

                                                }

                                            }
                                        });

                                        builder.show();

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                        UsersViewHolder holder = new UsersViewHolder(view);
                        return holder;
                    }

                };

        mFriendsList.setAdapter(adapter);
        adapter.startListening();

    }





}
