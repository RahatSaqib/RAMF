package sdk.chat.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sdk.chat.ui.Interface.ItemClickListner;
import sdk.chat.ui.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;
    public ImageView cartImage;


    public CartViewHolder(View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        cartImage = itemView.findViewById(R.id.cartImage);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
