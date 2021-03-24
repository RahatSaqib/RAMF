package sdk.chat.ui.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import de.hdodenhof.circleimageview.CircleImageView;
import sdk.chat.ui.Interface.ItemClickListner;
import sdk.chat.ui.R;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name,status;
    public CircleImageView userImageView;
    public ItemClickListner listner;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
       name = (TextView) itemView.findViewById(R.id.user_single_name);

       status = (TextView) itemView.findViewById(R.id.user_single_status);


        userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
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
