package sdk.chat.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.chat.firebase.adapter.FirebasePaths;
import sdk.chat.firebase.adapter.wrappers.UserWrapper;
import sdk.chat.ui.Admin.AdminMaintainProductsActivity;
import sdk.chat.ui.Model.Products;
import sdk.chat.ui.ViewHolder.ProductViewHolder;
import sdk.chat.ui.activities.MainAppBarActivity;


public class tab1medicine extends Fragment {

    private Query ProductsRef,ProductsRef1,ProductsRef2,ProductsRef3,ProductsRef4,ProductsRef5,ProductsRef6,ProductsRef7;
        private RecyclerView recyclerView;
    private RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5,recyclerView6,recyclerView7;

    RecyclerView.LayoutManager layoutManager1,layoutManager,layoutManager2,layoutManager3,layoutManager4,layoutManager5,layoutManager6,layoutManager7;
    private ImageView allmed;
    private ImageView button2,searchbtn;
    private TextView mName;

    private ImageView consult;
    private AdView mAdView;
    private ImageView admin;
    private String type= HomeActivity.getMystring();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.tab1medicine,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_menu);
//        recyclerView1= (RecyclerView) rootView.findViewById(R.id.recycler_menu2);
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recycler_menu3);
//        recyclerView3 = (RecyclerView) rootView.findViewById(R.id.recycler_menu4);
        recyclerView4 = (RecyclerView) rootView.findViewById(R.id.recycler_menu5);
        recyclerView5 = (RecyclerView) rootView.findViewById(R.id.recycler_menu6);
        recyclerView6 = (RecyclerView) rootView.findViewById(R.id.recycler_menu7);

        recyclerView7 = (RecyclerView) rootView.findViewById(R.id.recycler_menu8);
        recyclerView.setHasFixedSize(true);
//        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
//        recyclerView3.setHasFixedSize(true);
        recyclerView4.setHasFixedSize(true);
        recyclerView5.setHasFixedSize(true);
        recyclerView6.setHasFixedSize(true);
        recyclerView7.setHasFixedSize(true);
//        getAllRegisteredUsers();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(getActivity().getColor(R.color.transparent));
//        }
        searchbtn=(ImageView) rootView.findViewById(R.id.mysearch) ;
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(getActivity(),SearchProductsActivity.class);
                startActivity(intent1);
            }
        });
        admin=rootView.findViewById(R.id.admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin")){
                    startActivity(new Intent(getContext(),AdminLogin.class));
                }
                else{
                    Toast.makeText(getActivity(),"Already You are in Admin section",Toast.LENGTH_LONG).show();
                }

            }
        });





//         <item>Covid 19</item>
//        <item>Daily Essentials</item>
//        <item>Medications</item>
//
//        <item>Baby Needs</item>
//        <item>Health &amp; Nutrition</item>
//        <item>Diabetic Needs</item>
//        <item>Household needs</item>
//        <item>Personal Care</item>




            ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Covid 19");
        ProductsRef1 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Daily Essentials");
        ProductsRef2 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Medications");
        ProductsRef3 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Baby Needs");
        ProductsRef4 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Health & Nutrition");
        ProductsRef5 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Diabetic Needs");
        ProductsRef6 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Household Needs");
        ProductsRef7 = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("category1").equalTo("Personal Care");
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

//        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView1.setLayoutManager(layoutManager1);
        layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(layoutManager2);
//        layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerView3.setLayoutManager(layoutManager3);
        layoutManager4 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView4.setLayoutManager(layoutManager4);
        layoutManager5 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView5.setLayoutManager(layoutManager5);
        layoutManager6 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView6.setLayoutManager(layoutManager6);
        layoutManager7 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView7.setLayoutManager(layoutManager7);


        allmed= rootView.findViewById(R.id.allmedicine);
        allmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),AllMedicine.class);
                startActivity(intent);
            }
        });
        button2= rootView.findViewById(R.id.allhealth);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),HealthProducts.class);
                startActivity(intent);
            }
        });
        consult=rootView.findViewById(R.id.consultD);
        consult.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                if(ChatSDK.currentUserID() ==null){
                    Intent intent = new Intent(getActivity(), RegisterActivitydoc.class);
                    startActivity(intent);
                }
                else{

                    ChatSDK.ui().startMainActivity(getActivity());

                    }

            }
        });



        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7052071124298836/6330627257");

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        return rootView;

    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);



        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();
//        FirebaseRecyclerOptions<Products> options1 =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef1, Products.class)
//                        .build();
        FirebaseRecyclerOptions<Products> options2 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef2, Products.class)
                        .build();
//        FirebaseRecyclerOptions<Products> options3 =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef3, Products.class)
//                        .build();
        FirebaseRecyclerOptions<Products> options4 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef4, Products.class)
                        .build();
        FirebaseRecyclerOptions<Products> options5 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef5, Products.class)
                        .build();
        FirebaseRecyclerOptions<Products> options6 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef6, Products.class)
                        .build();
        FirebaseRecyclerOptions<Products> options7 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef7,Products.class)
                        .build();



        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };

//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter1 =
//            new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options1) {
//                @SuppressLint("SetTextI18n")
//                @Override
//                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                {
//                    holder.txtProductName.setText(model.getPname());
//                    holder.txtProductDescription.setText(model.getDescription());
//                    holder.txtProductPrice.setText(model.getPrice() +"৳");
////                    Picasso.get().load(model.getImage()).into(holder.imageView);
//
//
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view)
//                        {
//                            if (type.equals("Admin"))
//                            {
//                                Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
//                                intent.putExtra("pid", model.getPid());
//                                startActivity(intent);
//                            }
//                            else
//                            {
//                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
//                                intent.putExtra("pid", model.getPid());
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }
//
//                @NonNull
//                @Override
//                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                {
//                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                    ProductViewHolder holder = new ProductViewHolder(view);
//                    return holder;
//                }
//
//            };


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter2 =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options2) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter3 =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options3) {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText(model.getPrice() +"৳");
////                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                if (type.equals("Admins"))
//                                {
//                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
//                                    intent.putExtra("pid", model.getPid());
//                                    startActivity(intent);
//                                }
//                                else
//                                {
//                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
//                                    intent.putExtra("pid", model.getPid());
//                                    startActivity(intent);
//                                }
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//
//                };
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter4 =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options4) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Glide.with(getContext())
                                    .load(model.getImage()).fitCenter()
                                    .into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter5 =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options5) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter6=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options6) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter7=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options7){
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() +"৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).fit().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                };





//        recyclerView1.setAdapter(adapter1);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
//        adapter1.startListening();
        recyclerView2.setAdapter(adapter2);
        adapter2.startListening();
//        recyclerView3.setAdapter(adapter3);
//        adapter3.startListening();
        recyclerView4.setAdapter(adapter4);
        adapter4.startListening();
        recyclerView5.setAdapter(adapter5);
        adapter5.startListening();
        adapter6.startListening();
        recyclerView6.setAdapter(adapter6);
        adapter7.startListening();
        recyclerView7.setAdapter(adapter7);
    }
    }

