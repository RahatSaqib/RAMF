package sdk.chat.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import de.hdodenhof.circleimageview.CircleImageView;
import sdk.chat.ui.Interface.ItemClickListner;
import sdk.chat.ui.R;

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