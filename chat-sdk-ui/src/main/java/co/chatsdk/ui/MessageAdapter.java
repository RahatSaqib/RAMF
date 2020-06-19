package co.chatsdk.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    public static final int left=0;
    public static final int right=1;
    FirebaseUser fuser,fuser2;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int ItemViewType) {
        if (ItemViewType == right) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_next_layout, parent, false);

            return new MessageViewHolder(v);
        }
        else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_single_layout, parent, false);

            return new MessageViewHolder(v);
        }

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,timelayout;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            timelayout =(TextView)view.findViewById(R.id.time_text_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int i) {

        final Messages c = mMessageList.get(i);
        fuser2= FirebaseAuth.getInstance().getCurrentUser();

        final String from_user = c.getFrom();
        String message_type = c.getType();
        viewHolder.messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),FullscreenActivity.class);
                intent.putExtra("Message",c.getMessage());
                v.getContext().startActivity(intent);
            }
        });




        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("UsersD").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);

                if(!from_user.equals(fuser2.getUid())){
                    Picasso.get().load(image)
                            .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);}




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setText(c.getMessage());
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
            String dateString = formatter.format(new Date(c.getTime()));
            viewHolder.timelayout.setText(dateString);



        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);

//            Picasso.get().load(c.getMessage())
//                    .placeholder(R.drawable.default_avatar).centerCrop().resize(600,200).into(viewHolder.messageImage);
            Glide.with(viewHolder.messageImage.getContext()).load(c.getMessage()).override(600, 200).fitCenter().centerCrop().into(viewHolder.messageImage);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
            String dateString = formatter.format(new Date(c.getTime()));
            viewHolder.timelayout.setText(dateString);




        }

    }

    private void onClickImage(String message) {


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    @Override
    public int getItemViewType(int position){
       fuser= FirebaseAuth.getInstance().getCurrentUser();
       if(mMessageList.get(position).getFrom().equals(fuser.getUid())){
           return right;
       }
       else{
           return left;
       }
    }



}
