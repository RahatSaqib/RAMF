package sdk.chat.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import sdk.chat.core.session.ChatSDK;
import sdk.chat.ui.Model.Cart;
import sdk.chat.ui.ViewHolder.CartViewHolder;


public class CartFragment extends Fragment{


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;

    private double overTotalPrice = 0;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    String type = HomeActivity.getMystring();



    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = rootview.findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        int statusHeight = Utils.getStatusBarHeight(getActivity());
//        //动态的设置隐藏布局的高度
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bindingView.mainl.getLayoutParams();
//        params.height = statusHeight;
//        bindingView.mainl.setLayoutParams(params);

        NextProcessBtn = (Button)rootview.findViewById(R.id.next_btn);
        txtTotalAmount = (TextView) rootview.findViewById(R.id.total_price);
        txtMsg1 = (TextView) rootview.findViewById(R.id.msg1);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mCurrentUser!=null){current_uid = mCurrentUser.getUid();}



        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));

                startActivity(intent);

            }
        });
        // Inflate the layout for this fragment
        return rootview;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (!type.equals("Admin") && mCurrentUser!=null) {


            CheckOrderState();


            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

            FirebaseRecyclerOptions<Cart> options =
                    new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(cartListRef.child("User View")
                                    .child(current_uid)
                                    .child("Products"), Cart.class)
                            .build();

            FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                    = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                @SuppressLint("SetTextI18n")
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                    holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());

                    holder.txtProductName.setText(model.getPname());
                    if(!model.getImage().toString().equals("default")){
                        holder.cartImage.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(model.getImage()).fitCenter().centerCrop().into(holder.cartImage);}
                    else{
                        holder.cartImage.setVisibility(View.GONE);
                    }
                    if(model.getCategory().equals("Tablet")||model.getCategory().equals("Capsule")){
                        double mainvalue= ((Integer.parseInt(model.getPrice()))) /10.0;


                        double oneTypeProductTPrice = mainvalue * Integer.parseInt(model.getQuantity());
                        overTotalPrice = overTotalPrice + oneTypeProductTPrice;
                        txtTotalAmount.setText("Overall Price = " + overTotalPrice+"৳");
                        holder.txtProductPrice.setText(oneTypeProductTPrice+ "৳");
                    }
                    else{
                        double oneTypeProductTPrice = Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity());
                        overTotalPrice = overTotalPrice + oneTypeProductTPrice;
                        txtTotalAmount.setText("Overall Price = " + overTotalPrice+"৳");
                        holder.txtProductPrice.setText(oneTypeProductTPrice+ "৳");
                    }


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CharSequence options[] = new CharSequence[]
                                    {
                                            "Edit",
                                            "Remove"
                                    };
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Cart Options:");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i == 0) {
                                        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                                        intent.putExtra("pid", model.getPid());
                                        startActivity(intent);
                                    }
                                    if (i == 1) {
                                        cartListRef.child("User View")
                                                .child(current_uid)
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Item removed successfully.", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(getContext(), HomeActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }

                @NonNull
                @Override
                public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                    CartViewHolder holder = new CartViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
        else{
            if(ChatSDK.currentUserID()==null){
                txtMsg1.setText("Yor are not registered yet.");
            }
            else{
                txtMsg1.setText("You are an admin. You can't purchase anything.Use your user id for purchasing.Thank You");
            }

        }

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
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {

                        txtTotalAmount.setVisibility(View.INVISIBLE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will received your order at your door step.");
                        NextProcessBtn.setVisibility(View.GONE);

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("You have already ordered!!");
                        alert.setMessage("you can purchase more products, once you received your first final order.");



                        alert.setNegativeButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                });
                        try{alert.show();}
                        catch (WindowManager.BadTokenException e){}


                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        txtTotalAmount.setVisibility(View.INVISIBLE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("You have already ordered!!");
                        alert.setMessage("you can purchase more products, once you received your first final order.");



                        alert.setNegativeButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent mainIntent = new Intent(getContext(), HomeActivity.class);

                                        startActivity(mainIntent);
                                       
                                    }
                                });

                        try{alert.show();}
                        catch (WindowManager.BadTokenException e){}
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}