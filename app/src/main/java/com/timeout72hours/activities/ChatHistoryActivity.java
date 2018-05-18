package com.timeout72hours.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.timeout72hours.R;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;

import java.util.Map;

public class ChatHistoryActivity extends AppCompatActivity {
    private Context mContext;
    private DatabaseReference mDatabase;

    private String roomID = "";
    private String friends_id = "97";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);


        initDatabase(friends_id);
    }

    private void initDatabase(String friends_id) {
        mContext = ChatHistoryActivity.this;

        if (Integer.parseInt(friends_id) > Integer.parseInt(Utility.getAppPrefString(mContext, Constant.USER_ID))) {
            roomID = Utility.getAppPrefString(mContext, Constant.USER_ID) + "_" + friends_id;
        } else {
            roomID = friends_id + "_" + Utility.getAppPrefString(mContext, Constant.USER_ID);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query lastQuery = mDatabase.child("timeout_72").child(roomID).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> value= (Map<String, String>) dataSnapshot.getValue();

                for (Map.Entry<String, String> entry : value.entrySet()) {
                    String key = entry.getKey();
                    // you code here
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
