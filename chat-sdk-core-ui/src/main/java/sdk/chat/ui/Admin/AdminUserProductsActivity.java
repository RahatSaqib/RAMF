package sdk.chat.ui.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sdk.chat.ui.Model.Cart;
import sdk.chat.ui.R;
import sdk.chat.ui.ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity
{
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);


        userID = getIntent().getStringExtra("uid");


        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "৳");
                holder.txtProductName.setText(model.getPname());
                if(!model.getImage().toString().equals("default")){
                    holder.cartImage.setVisibility(View.VISIBLE);
                    Glide.with(AdminUserProductsActivity.this).load(model.getImage()).fitCenter().centerCrop().into(holder.cartImage);}
                else{
                    holder.cartImage.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}
