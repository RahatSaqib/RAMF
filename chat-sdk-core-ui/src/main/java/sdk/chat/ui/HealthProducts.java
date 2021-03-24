package sdk.chat.ui;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class HealthProducts extends AppCompatActivity {
    private Button corona,homeo,health,personal,fit,ayurveda,diabetes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Health Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        corona=(Button) findViewById(R.id.corona);
        homeo=(Button) findViewById(R.id.homeo);
        health=(Button) findViewById(R.id.health);
        personal=(Button) findViewById(R.id.personal);
        fit=(Button) findViewById(R.id.fit);

        diabetes=(Button) findViewById(R.id.diabetes);

        corona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this, categories.class);
                intent.putExtra("category","Surgical Equipment");
                startActivity(intent);
            }
        });
        homeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this,categories.class);
                intent.putExtra("category","Personal Equipment");
                startActivity(intent);
            }
        });
        fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this,categories.class);
                intent.putExtra("category","Capsule");
                startActivity(intent);
            }
        });
        diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this,categories.class);
                intent.putExtra("category","Tablet");
                startActivity(intent);
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this,categories.class);
                intent.putExtra("category","Syrup");
                startActivity(intent);
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HealthProducts.this,categories.class);
                intent.putExtra("category","Oinment");
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
