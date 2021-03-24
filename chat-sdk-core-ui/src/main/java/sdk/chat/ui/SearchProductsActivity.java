package sdk.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import sdk.chat.ui.Admin.AdminMaintainProductsActivity;
import sdk.chat.ui.Model.Products;
import sdk.chat.ui.ViewHolder.ProductViewHolder;
import sdk.chat.ui.icons.Icons;

public class SearchProductsActivity extends AppCompatActivity
{
    private Button SearchBtn;
    private MaterialSearchView inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private String type= HomeActivity.getMystring();
    private String text ="";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);


        inputText = findViewById(R.id.search_product_name);
//        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new GridLayoutManager(this,2));
        inputText.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                finish();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setIcon(Icons.get(this, Icons.choose().search, Icons.shared().actionBarIconColor));

        inputText.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }



    @Override
    protected void onStart()
    {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput).endAt(SearchInput + "\uf8ff"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText( model.getPrice() + "৳");
                        if(!model.getImage().toString().equals("default")){
                            Glide.with(SearchProductsActivity.this).load(model.getImage()).fitCenter().centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(SearchProductsActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
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

        searchList.setAdapter(adapter);
        inputText.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    text = query.trim();
                    SearchInput=text;

                    String str = SearchInput;
                    String[] strArray = str.split(" ");
                    StringBuilder builder = new StringBuilder();
                    for (String s : strArray) {
                        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                        builder.append(cap + " ");
                    }
                    SearchInput=builder.toString();
                    onStart();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 1) {
                    text = newText.trim();
                    SearchInput=text;

                    String str = SearchInput;

                    StringBuilder builder = new StringBuilder();
                    String s = "";
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    builder.append(cap + "");

                    SearchInput=builder.toString();
                    onStart();
                }
                return false;
            }
        });
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        inputText.showSearch(false);
    }
}
