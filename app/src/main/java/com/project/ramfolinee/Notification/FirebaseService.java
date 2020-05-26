//package com.example.rams.Notification;
//import android.util.Log;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessagingService;
//
//public class FirebaseService extends FirebaseMessagingService {
//
//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        String refresh= FirebaseInstanceId.getInstance().getToken();
//        if(user!= null){
//            updateToken(refresh);
//        }
//        Log.d("NEW_TOKEN",s);
//    }
//
//    private void updateToken(String refresh) {
//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token=new Token(refresh);
//        ref.child(user.getUid()).setValue(token);
//    }
//
//}
