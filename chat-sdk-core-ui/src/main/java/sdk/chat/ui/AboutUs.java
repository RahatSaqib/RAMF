package sdk.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class AboutUs extends AppCompatActivity {
    private RelativeLayout policyurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        policyurl=findViewById(R.id.policyurl);
        policyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://ramf-online.flycricket.io/privacy.html"));
                startActivity(viewIntent);
            }
        });

    }
}
