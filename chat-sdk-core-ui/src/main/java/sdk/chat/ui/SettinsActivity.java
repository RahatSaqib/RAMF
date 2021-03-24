package sdk.chat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettinsActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText,addstatus;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private Button securityQuestionBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef,storageProfilePrictureRef1;
    private String checker = "";
    private static final int GalleryPick = 1;
    private FirebaseUser mCurrentUser;
    private String current_uid ;
    private DatabaseReference UsersRef;
    private String thumb_downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settins);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        storageProfilePrictureRef1 = FirebaseStorage.getInstance().getReference().child("Thumb pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);
        securityQuestionBtn = findViewById(R.id.security_questions_btn);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        addstatus=findViewById(R.id.status);
        current_uid= mCurrentUser.getUid();

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText,addstatus);

profileImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            checker = "clicked";

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(SettinsActivity.this);
        }
    });



        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettinsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });


        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettinsActivity.this);
            }
        });
    }







    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UsersD");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phoneOrder", userPhoneEditText.getText().toString());
        userMap. put("status", addstatus.getText().toString());

        ref.child(current_uid).updateChildren(userMap);

        startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
        Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK){

           imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
           if( resultCode==RESULT_OK){
               imageUri = result.getUri();

               profileImageView.setImageURI(imageUri);}



        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettinsActivity.this, SettinsActivity.class));
            finish();
        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }



    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(current_uid + ".jpg");
            final StorageReference thumb_filepath= storageProfilePrictureRef1.child(current_uid+".jpg");

            uploadTask = fileRef.putFile(imageUri);


            File thumb_filePath = new File(Objects.requireNonNull(imageUri.getPath()));

            Bitmap thumb_bitmap = null;
            try {
                thumb_bitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(75).compressToBitmap(thumb_filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert thumb_bitmap != null;
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumb_byte = baos.toByteArray();




            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        final UploadTask uploadTask1 = thumb_filepath.putBytes(thumb_byte);


                        uploadTask1.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> thumb_task) {
                                Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) throws Exception {
                                        if (!thumb_task.isSuccessful()) {
                                            throw thumb_task.getException();
                                        }

                                        return thumb_filepath.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task thumb_task) {
                                        if (thumb_task.isSuccessful()){
                                            thumb_downloadUrl = thumb_task.getResult().toString();
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UsersD");

                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap. put("name", fullNameEditText.getText().toString());
                                            userMap. put("address", addressEditText.getText().toString());
                                            userMap. put("phoneOrder", userPhoneEditText.getText().toString());
                                            userMap. put("status", addstatus.getText().toString());
                                            userMap. put("image", myUrl);

                                            userMap. put("thumb_image", thumb_downloadUrl);

                                            ref.child(current_uid).updateChildren(userMap);

                                            progressDialog.dismiss();

                                            startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
                                            Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }
                                        else {

                                            Toast.makeText(SettinsActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();

                                        }
                                    }
                                });
                            }
                        });




                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettinsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText,final EditText addstatus)
    {
         UsersRef = FirebaseDatabase.getInstance().getReference().child("UsersD").child(current_uid);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String status= dataSnapshot.child("status").getValue().toString();

                        Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                        addstatus.setText(status);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
