package co.chatsdk.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import co.chatsdk.ui.Interface.ItemClickListner;
import co.chatsdk.ui.R;
import de.hdodenhof.circleimageview.CircleImageView;

public  class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


   public TextView userStatusView,userNameView;
   public CircleImageView userImageView;
   public ImageView userOnlineView;
    public ItemClickListner listner;


    public FriendsViewHolder(View itemView) {
        super(itemView);
         userStatusView = (TextView) itemView.findViewById(R.id.user_single_status);
         userNameView = (TextView) itemView.findViewById(R.id.user_single_name);
         userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);

    }




    public  void setUserOnline(String online_status) {

         userOnlineView = (ImageView) itemView.findViewById(R.id.user_single_online_icon);

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