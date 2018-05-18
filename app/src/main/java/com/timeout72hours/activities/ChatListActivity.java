package com.timeout72hours.activities;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timeout72hours.R;
import com.timeout72hours.adapter.ChatListAdapter;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static com.timeout72hours.attributes.Constant.NOTI_COUNT;

public class ChatListActivity extends AppCompatActivity {
    private Context mContext;
    private DatabaseReference mDatabase;

    private RecyclerView rvChatFriendList;
    private ImageView img_back;
    private LinearLayoutManager layoutManager;
    private ArrayList<ChatMessage> chatListDataArrayList;
    private ChatListAdapter chatListAdapter;

    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initUi();
//        getChatList();
    }

    private void initUi() {
        mContext = ChatListActivity.this;

        progressDialog = new CustomProgressDialog(mContext);

        chatListDataArrayList = new ArrayList<>();
        rvChatFriendList = findViewById(R.id.rv_chat_friend_list);

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvChatFriendList.setLayoutManager(layoutManager);

        chatListAdapter = new ChatListAdapter(mContext, chatListDataArrayList);
        rvChatFriendList.setAdapter(chatListAdapter);

        rvChatFriendList.setVisibility(View.GONE);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatListDataArrayList.clear();
        getChatList();
        Utility.writeSharedPreferencesInt(getApplicationContext(),NOTI_COUNT,0);

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nMgr != null) {
            nMgr.cancelAll();
        }
    }

    private void getChatList() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-timeout72.firebaseio.com/timeout_72/chat-list/" +
                Utility.getAppPrefString(mContext, Constant.USER_ID));

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();

                if (dataSnapshots != null) {
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();

                        ChatMessage chatListData = new ChatMessage();
                        chatListData = dataSnapshotChild.getValue(ChatMessage.class);

                        chatListDataArrayList.add(chatListData);
                    }

                    Collections.sort(chatListDataArrayList, new Comparator<ChatMessage>() {
                        @Override
                        public int compare(ChatMessage c1, ChatMessage c2) {
                            int date = c2.getDate().compareTo(c1.getDate());
                            if (date != 0) {
                                return date;
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                return Long.compare(Long.parseLong(c2.getDate()), Long.parseLong(c1.getDate()));
                            } else {
                                return 0;
                            }
                        }
                    });

                    chatListAdapter.notifyDataSetChanged();
                }

                rvChatFriendList.setVisibility(View.VISIBLE);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("type") != null &&
                getIntent().getStringExtra("type").equalsIgnoreCase("notification")) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
