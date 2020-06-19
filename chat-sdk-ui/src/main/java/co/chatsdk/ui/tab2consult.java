package co.chatsdk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class tab2consult extends Fragment {
    private Button consult,consultant;
    private FirebaseUser mAuth;
    private DatabaseReference rootref;
    private String uid;
    private RewardedVideoAd mRewardedVideoAd;

    private String  type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.tab2consult,container,false);

        consult=(Button) rootView.findViewById(R.id.consult);
        consultant=(Button) rootView.findViewById(R.id.conusultant);
        mAuth= FirebaseAuth.getInstance().getCurrentUser();
        rootref= FirebaseDatabase.getInstance().getReference();
        uid=mAuth.getUid();



        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootref.child("UsersD").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       type = dataSnapshot.child("type").getValue().toString();
                        if (type.equals("Doctor")) {

                            try
                            {
                                Intent intent = new Intent(getContext(), MainActivitydoc1.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }



                        } else {
                            try {
                                Toast.makeText(getContext(), "You are not a doctor", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        consultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootref.child("UsersD").child(uid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String type = dataSnapshot.child("type").getValue().toString();
                                if (!type.equals("Doctor")) {
                                    try
                                    {
                                        Intent intent = new Intent(getContext(), MainActivitydoc1.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                                else{
                                    try {
                                        Toast.makeText(getContext(), "You are a doctor", Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        return rootView;
    }



}