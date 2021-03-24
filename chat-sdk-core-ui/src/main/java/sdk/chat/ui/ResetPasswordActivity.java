package sdk.chat.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity
{
    private Button verify;
    private EditText email;
    private String check;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth= FirebaseAuth.getInstance();

        verify=findViewById(R.id.verify_btn);
        email=findViewById(R.id.find_phone_number);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=verify.getText().toString();
                if(TextUtils.isEmpty(check)){
                    Toast.makeText(ResetPasswordActivity.this,"Please insert email",Toast.LENGTH_LONG).show();
                }
                else{

                    mAuth.sendPasswordResetEmail(check).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                AlertDialog.Builder alert = new AlertDialog.Builder(ResetPasswordActivity.this);
                                alert.setTitle("Reset Password!!");
                                alert.setMessage("Please check your email : "+email+"and click on the link to reset your password.");



                                alert.setNegativeButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });

                                alert.show();
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(ResetPasswordActivity.this);
                                alert.setTitle("Error!!");
                                alert.setMessage(task.getException().getMessage());



                                alert.setNegativeButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });

                                alert.show();
                            }

                        }
                    });
                }
            }
        });
    }




}
