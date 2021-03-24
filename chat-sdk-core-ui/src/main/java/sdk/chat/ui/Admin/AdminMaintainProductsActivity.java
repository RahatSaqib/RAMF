package sdk.chat.ui.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import sdk.chat.ui.R;


public class AdminMaintainProductsActivity extends AppCompatActivity
{
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productID = "";
    private DatabaseReference productsRef;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,ProductsRef1;
    private String pName,pPrice,pDescription;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");


        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            OpenGallery();
        }
    });


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
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }





    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void applyChanges()
    {
       pName = name.getText().toString();
       pPrice = price.getText().toString();
        pDescription = description.getText().toString();

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
        if(ImageUri==null){
            downloadImageUrl="default";
            SaveProductInfoToDatabase();
        }

        else
        {
            loadingBar= new ProgressDialog(this);
            loadingBar.setTitle("Add New Product");
            loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(ImageUri);


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminMaintainProductsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AdminMaintainProductsActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl = task.getResult().toString();

                                Toast.makeText(AdminMaintainProductsActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                SaveProductInfoToDatabase();
                            }
                        }
                    });
                }
            });

        }

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productID);
        productMap.put("description", pDescription);
        productMap.put("price", pPrice);
        productMap.put("pname", pName);
        productMap.put("image", downloadImageUrl);

        productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void displaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();


                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Glide.with(AdminMaintainProductsActivity.this).load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
