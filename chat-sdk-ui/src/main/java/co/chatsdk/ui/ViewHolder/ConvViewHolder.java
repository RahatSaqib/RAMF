package co.chatsdk.ui.ViewHolder;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import co.chatsdk.ui.Interface.ItemClickListner;
import co.chatsdk.ui.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListner listner;
    public TextView userNameView,userStatusView;
    public CircleImageView userImageView;
    public   ImageView userOnlineView;


    public ConvViewHolder(@NonNull View itemView) {
        super(itemView);

        userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
        userStatusView = (TextView) itemView.findViewById(R.id.user_single_status);
        userOnlineView = (ImageView) itemView.findViewById(R.id.user_single_online_icon);
        userNameView = (TextView) itemView.findViewById(R.id.user_single_name);
    }


    public  void setMessage(String message, boolean isSeen){
        userStatusView.setText(message);
        if(!isSeen){
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
        } else {
            userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
        }

    }
    public  void setName(String name){


        userNameView.setText(name);

    }



    public void setUserOnline(String online_status) {



        if(online_status.equals("true")){

            userOnlineView.setVisibility(View.VISIBLE);

        } else {

            userOnlineView.setVisibility(View.INVISIBLE);

        }

    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
