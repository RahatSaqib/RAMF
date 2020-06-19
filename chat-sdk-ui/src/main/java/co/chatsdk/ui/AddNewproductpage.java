package co.chatsdk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.chatsdk.ui.Admin.AdminAddNewProductActivity;


public class AddNewproductpage extends AppCompatActivity {
    private Button corona,homeo,health,personal,fit,ayurveda,diabetes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newproductpage);
        corona=(Button) findViewById(R.id.corona);
        homeo=(Button) findViewById(R.id.homeo);
        health=(Button) findViewById(R.id.health);
        personal=(Button) findViewById(R.id.personal);
        fit=(Button) findViewById(R.id.fit);

        diabetes=(Button) findViewById(R.id.diabetes);

        corona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","Surgical Equipment");
                startActivity(intent);
            }
        });
        homeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Personal Equipment");
                startActivity(intent);
            }
        });
        fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Capsule");
                startActivity(intent);
            }
        });
        diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Tablet");
                startActivity(intent);
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Syrup");
                startActivity(intent);
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddNewproductpage.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Oinment");
                startActivity(intent);
            }
        });
    }
}
