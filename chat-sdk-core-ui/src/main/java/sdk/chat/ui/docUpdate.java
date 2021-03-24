package sdk.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import sdk.chat.ui.Admin.AdminCategoryActivity;

public class docUpdate extends AppCompatActivity
{
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productsRef,productsRef1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_update);


        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("firebase").child(productID).child("meta");
        productsRef1 = FirebaseDatabase.getInstance().getReference().child("firebase").child(productID);



        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);


        displaySpecificProductInfo();



        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                applyChanges();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteThisProduct();
            }
        });
    }





    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(docUpdate.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(docUpdate.this, "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void applyChanges()
    {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals(""))
        {
            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals(""))
        {
            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals(""))
        {
            Toast.makeText(this, "Write down Product Description.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("type", pDescription);
            productMap.put("phone", pPrice);
            productMap.put("name", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(docUpdate.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(docUpdate.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }





    private void displaySpecificProductInfo()
    {
        productsRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pDescription = "";
                    String pName = dataSnapshot.child("meta").child("name").getValue().toString();
                    String pPrice = dataSnapshot.child("meta").child("email").getValue().toString();
                    if(dataSnapshot.child("type").exists()){
                        pDescription = dataSnapshot.child("type").getValue().toString();
                    }
                   
                    String pImage = dataSnapshot.child("meta").child("pictureURL").getValue().toString();


                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Glide.with(docUpdate.this).load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
