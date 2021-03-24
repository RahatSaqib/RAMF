package sdk.chat.ui.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sdk.chat.ui.AddNewproductpage;
import sdk.chat.ui.DoctorRegister;
import sdk.chat.ui.DoctorSearch;
import sdk.chat.ui.HomeActivity;
import sdk.chat.ui.MainActivity;
import sdk.chat.ui.R;


public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;
    private TextView paracetamol,sanitizer;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn,docregister,users,ADD;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        users = (Button) findViewById(R.id.Users);
        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);
        docregister=(Button) findViewById(R.id.doc_register);
        ADD= findViewById(R.id.add_new_product);
//        users.setVisibility(View.GONE);
//        docregister.setVisibility(View.GONE);
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AddNewproductpage.class);

                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });
//        LogoutBtn.setVisibility(View.GONE);


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, DoctorSearch.class);

                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        docregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                Intent intent = new Intent(AdminCategoryActivity.this, DoctorRegister.class);
//
//                intent.putExtra("Admin", "Admin");
//                startActivity(intent);
            }
        });


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);

                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("User","");
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminCategoryActivity.this,HomeActivity.class);
        intent.putExtra("Admin","User");
        startActivity(intent);

    }
}
