package co.chatsdk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import co.chatsdk.ui.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import co.chatsdk.ui.profile.ProfileActivity;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD");

        mLayoutManager = new LinearLayoutManager(this);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<UsersD> options =
                new FirebaseRecyclerOptions.Builder<UsersD>()
                        .setQuery(mUsersDatabase, UsersD.class)
                        .build();

        FirebaseRecyclerAdapter<UsersD, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersD, UsersViewHolder>(options) {

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int i, @NonNull final UsersD model) {
                holder.name.setText(model.getName());
                holder.status.setText(model.getStatus());
                Picasso.get().load(model.getThumb_image()).placeholder(R.drawable.default_avatar).into(holder.userImageView);

                final String user_id = getRef(i).getKey();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
                UsersViewHolder holder = new UsersViewHolder(view);
                return holder;

            }
            };


        mUsersList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }




}
