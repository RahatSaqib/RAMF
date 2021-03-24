package sdk.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.EditText;

import sdk.chat.ui.ViewHolder.ProductViewHolder;

public class DoctorSearch extends AppCompatActivity
{
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private String type= HomeActivity.getMystring();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);


        inputText = findViewById(R.id.search_product_name);
//        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(DoctorSearch.this));


        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SearchInput = inputText.getText().toString();

                onStart();
            }
        });
    }



    @Override
    protected void onStart()
    {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("firebase");

        FirebaseRecyclerOptions<UsersD> options =
                new FirebaseRecyclerOptions.Builder<UsersD>()
                        .setQuery(reference.orderByChild("name").startAt(SearchInput).endAt(SearchInput + "\uf8ff"), UsersD.class)
                        .build();

        FirebaseRecyclerAdapter<UsersD, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<UsersD, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final UsersD model)
                    {
                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getEmail());
                        holder.txtProductPrice.setText( model.getPhone());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(DoctorSearch.this, docUpdate.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

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
        adapter.startListening();
    }
}
