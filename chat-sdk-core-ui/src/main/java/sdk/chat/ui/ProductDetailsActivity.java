package sdk.chat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import sdk.chat.core.session.ChatSDK;
import sdk.chat.ui.Model.Products;

public class ProductDetailsActivity extends AppCompatActivity
{
    private Button addToCartButton;
    private ImageView Image;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    private FirebaseUser mCurrentUser;
    private String current_uid ;
    private String med,imageuri;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        Image = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser!=null){
            current_uid= mCurrentUser.getUid();

        }
        getProductDetails(productID);


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);







        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                }
                else if(ChatSDK.currentUserID() ==null){
                    Intent intent =new Intent(ProductDetailsActivity.this,RegisterActivitydoc.class);
                    startActivity(intent);
                }

                else
                {
                    progress.show();
                    addingToCartList();
                }
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if(mCurrentUser!=null){CheckOrderState();}


    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("image",imageuri);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        cartMap.put("category",med);

        cartListRef.child("User View").child(current_uid)
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(current_uid)
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();
                                                progress.dismiss();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void getProductDetails(String productID)
    {
        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    med= dataSnapshot.child("category").getValue().toString();

                    if(med.equals("Capsule") || med.equals("Tablet"))
                    {
                        Products products = dataSnapshot.getValue(Products.class);

                        productName.setText(products.getPname());
                        productPrice.setText(products.getPrice());
                        productDescription.setText(products.getDescription());
                        if(!products.getImage().toString().equals("default")){
                            Glide.with(ProductDetailsActivity.this).load(products.getImage()).fitCenter().centerCrop().into(Image);}
                        else{
                            Image.setVisibility(View.GONE);
                        }
                        imageuri=products.getImage();

                        numberButton.setRange(10,100);


                    }
                    else{Products products = dataSnapshot.getValue(Products.class);

                        productName.setText(products.getPname());
                        productPrice.setText(products.getPrice());
                        productDescription.setText(products.getDescription());
                        if(!products.getImage().toString().equals("default")){
                            Image.setVisibility(View.VISIBLE);
                            Glide.with(ProductDetailsActivity.this).load(products.getImage()).fitCenter().centerCrop().into(Image);}
                        else{
                            Image.setVisibility(View.GONE);
                        }
                        imageuri=products.getImage();
                        numberButton.setRange(1,100);}

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(current_uid);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
