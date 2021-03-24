package sdk.chat.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import sdk.chat.ui.Model.Products;
import sdk.chat.ui.ViewHolder.ProductViewHolder;
import sdk.chat.ui.icons.Icons;

public class AllMedicine extends AppCompatActivity
{
    private Button SearchBtn;
    private MaterialSearchView inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private Button A,B,C,D,E,F,G,H,I,J,K,L,M;
    private Button N,O,P,Q,Re,S,T,U,V,W,X,Y,Z;
    private String text ="";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medicine);


        inputText = findViewById(R.id.search_product_name);


       A = (Button) findViewById(R.id.A);
       B = (Button) findViewById(R.id.B);
       C = (Button) findViewById(R.id.C);
       D = (Button) findViewById(R.id.D);
       E = (Button) findViewById(R.id.E);
       F = (Button) findViewById(R.id.F);
       G = (Button) findViewById(R.id.G);
       H = (Button) findViewById(R.id.H);
       I = (Button) findViewById(R.id.I);
       J = (Button) findViewById(R.id.J);
       K = (Button) findViewById(R.id.K);
        L= (Button) findViewById(R.id.L);
       M = (Button) findViewById(R.id.M);
       N = (Button) findViewById(R.id.N);
       O = (Button) findViewById(R.id.O);
       P = (Button) findViewById(R.id.P);
       Q = (Button) findViewById(R.id.Q);
       Re = (Button) findViewById(R.id.Re);
       S = (Button) findViewById(R.id.S);
       T = (Button) findViewById(R.id.T);
       U = (Button) findViewById(R.id.U);
       V = (Button) findViewById(R.id.V);
        W= (Button) findViewById(R.id.W);
        X= (Button) findViewById(R.id.X);
        Y= (Button) findViewById(R.id.Y);
        Z= (Button) findViewById(R.id.Z);



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
        A.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view)
            {
//                A.setTextColor(R.color.colorAccent);
                SearchInput = "A";

                onStart();
            }
        });
        B.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
//            B.setBackgroundDrawable(getResources().getDrawable(R.drawable.broder));
            SearchInput = "B";

            onStart();
        }
    });
        C.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "C";

            onStart();
        }
    });
        D.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "D";

            onStart();
        }
    });
        E.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "E";

            onStart();
        }
    });
        F.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "F";

            onStart();
        }
    });
        G.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "G";

            onStart();
        }
    });
       H.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "H";

            onStart();
        }
    });
        I.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "I";

            onStart();
        }
    });
        J.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "J";

            onStart();
        }
    });
        K.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "K";

            onStart();
        }
    });
        L.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "L";

            onStart();
        }
    });
        M.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "M";

            onStart();
        }
    });
        N.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "N";

            onStart();
        }
    });
        O.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "O";

            onStart();
        }
    });
        P.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "P";

            onStart();
        }
    });
        Q.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "Q";

            onStart();
        }
    });
       Re.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "R";

            onStart();
        }
    });
        S.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "S";

            onStart();
        }
    });
        T.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "T";

            onStart();
        }
    });
        U.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "U";

            onStart();
        }
    });
        V.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "V";

            onStart();
        }
    });
        W.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            SearchInput = "W";

            onStart();
        }
    });
        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SearchInput = "X";

                onStart();
            }
        });
        Y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SearchInput = "Y";

                onStart();
            }
        });
        Z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SearchInput = "Z";

                onStart();
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
    protected void onStart()
    {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Medicine");

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
                        holder.txtProductPrice.setText(model.getPrice() + "৳");
                        if(!model.getImage().toString().equals("default")){
                            Picasso.get().load(model.getImage()).centerCrop().into(holder.imageView);}
                        else{
                            holder.imageView.setVisibility(View.GONE);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(AllMedicine.this, ProductDetailsActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        inputText.showSearch(false);
    }
}
