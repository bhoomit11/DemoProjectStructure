package com.timeout72hours.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.adapter.ChatAdapter;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.FCM.FcmNotificationBuilder;
import com.timeout72hours.attributes.FCM.FirebaseChatMainApp;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.timeout72hours.attributes.Constant.NOTI_COUNT;

/**
 * Created by hardip on 8/12/17.
 */

public class ChatActivity extends AppCompatActivity {

    private Context mContext;
    private DatabaseReference mMessageDatabase;

    private DatabaseReference ListSender;
    private DatabaseReference ListReciever;

    private RecyclerView rv_chat_list;
    private LinearLayoutManager layoutManager;
    private ChatAdapter chatAdapter;
    //    private ChildEventListener childEventListener;
    private ArrayList<ChatMessage> chatMessages;

    private CustomProgressDialog progressDialog;

    private EditText et_comment;
    private TextView tv_user;
    private ImageView iv_user_image, img_back;
    private LinearLayout btn_send;

    private String roomID = "", userName = "", uid = "", userImage = "";
    private String token = "";
    String lastChildKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {
        mContext = ChatActivity.this;
        progressDialog = new CustomProgressDialog(mContext);

        chatMessages = new ArrayList<>();
        rv_chat_list = (RecyclerView) findViewById(R.id.rv_chat_list);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        rv_chat_list.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(chatMessages, mContext);
        rv_chat_list.setAdapter(chatAdapter);

        tv_user = findViewById(R.id.tv_username);
        iv_user_image = findViewById(R.id.iv_user_image);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_comment = findViewById(R.id.et_comment);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_comment.getText().toString().trim().length() != 0) {

                    mMessageDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
//                            if (map == null || !map.containsKey(roomID)) {
                            Map<String, Object> postValues = new HashMap();

                            postValues.put("sender_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
                            postValues.put("recever_id", uid);
                            postValues.put("recevier_name", userName);
                            postValues.put("sender_name", Utility.getAppPrefString(mContext, Constant.NAME));
                            postValues.put("text", et_comment.getText().toString());
                            postValues.put("text_id", new Random().nextInt() + "");
                            postValues.put("date", System.currentTimeMillis() + "");
                            postValues.put("userImage", userImage);
                            postValues.put("devide_token", token);
                            postValues.put("display_name", userName);
                            postValues.put("display_id", uid);
                            postValues.put("isRead", "false");

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/" + uid, postValues);
                            ListSender.updateChildren(childUpdates);

                            childUpdates = new HashMap<>();
                            postValues.put("display_name", Utility.getAppPrefString(mContext, Constant.NAME));
                            postValues.put("display_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
                            postValues.put("userImage", Utility.getAppPrefString(mContext, Constant.IMAGE));
                            postValues.put("devide_token", Utility.getAppPrefString(mContext, Constant.ARG_FIREBASE_TOKEN));
                            childUpdates.put("/" + Utility.getAppPrefString(mContext, Constant.USER_ID), postValues);
                            ListReciever.updateChildren(childUpdates);

                            sendChat(et_comment.getText().toString(),
                                    Utility.getAppPrefString(mContext, Constant.NAME),
                                    Utility.getAppPrefString(mContext, Constant.IMAGE),
                                    Utility.getAppPrefString(mContext, Constant.USER_ID),
                                    System.currentTimeMillis() + "");

                            et_comment.setText("");
//                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            sendChat(et_comment.getText().toString()
                                    , Utility.getAppPrefString(mContext, Constant.NAME),
                                    Utility.getAppPrefString(mContext, Constant.USER_ID),
                                    Utility.getAppPrefString(mContext, Constant.IMAGE),
                                    System.currentTimeMillis() + "");

                            et_comment.setText("");
                        }
                    });

//                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
//
////                            if (map == null || !map.containsKey(roomID)) {
////                                String key = mMessageDatabase.child("timeout_72").push().getKey();
//
//                            Map<String, Object> postValues = new HashMap();
//                            postValues.put(Constant.RECENT_USER, userName);
//                            postValues.put(Constant.RECENT_MESSAGE, et_comment.getText().toString());
//                            postValues.put(Constant.RECIEVER_UID, uid);
//                            postValues.put(Constant.RECENT_TIME, new Date().toString());
//
//                            Map<String, Object> childUpdates = new HashMap<>();
//                            childUpdates.put("/timeout_72/recent/" + roomID, postValues);
//
//                            mMessageDatabase.updateChildren(childUpdates);
                }
            }
        });

        if (getIntent().getStringExtra("type") != null) {
            {
                token = getIntent().getStringExtra("token");
                uid = getIntent().getStringExtra("id");
                userName = getIntent().getStringExtra("name");
                userImage = getIntent().getStringExtra("image");

                if (Integer.parseInt(uid) > Integer.parseInt(Utility.getAppPrefString(mContext, Constant.USER_ID))) {
                    roomID = Utility.getAppPrefString(mContext, Constant.USER_ID) + "_" + uid;
                } else {
                    roomID = uid + "_" + Utility.getAppPrefString(mContext, Constant.USER_ID);
                }
            }

            tv_user.setText(userName);
            Picasso.with(mContext).load(userImage).error(R.drawable.user_default).transform(new CircleTransform()).into(iv_user_image);
        }

        initFirebaseDB();
    }

    public void initFirebaseDB() {
        mMessageDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-timeout72.firebaseio.com/timeout_72/message/" + roomID);

        ListSender = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-timeout72.firebaseio.com/timeout_72/chat-list/" +
                Utility.getAppPrefString(mContext, Constant.USER_ID));

        ListReciever = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-timeout72.firebaseio.com/timeout_72/chat-list/" +
                uid);


        ListSender.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Map map1 = (Map) dataSnapshot.getValue();
                    if (map1 != null) {
                        String rec_id = (String) map1.get("recever_id");
                        if (rec_id.equals(Utility.getAppPrefString(mContext, Constant.USER_ID))) {
                            ListSender.child(uid).child("isRead").setValue("true");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
//                chatAdapter.update(chatMessage);
//                Log.e("Data",dataSnapshot.getValue()+"");
//                rv_chat_list.smoothScrollToPosition(chatAdapter.getItemCount());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(Constant.TAG, "onChildChanged is: " + dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(Constant.TAG, "onChildRemoved is: " + dataSnapshot);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Log.d(Constant.TAG, "onChildMoved is: " + dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(Constant.TAG, "onCancelled is: " + databaseError);
//            }
//        };

//        if (!progressDialog.isShowing()) {
//            progressDialog.show();
//        }

//        mMessageDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("Size", "" + dataSnapshot.getChildrenCount());
//                 Result will be holded Here
//
//                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                    chatMessages.add(dsp.getValue(ChatMessage.class)); //add result into array list
//                }
//
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//
//                chatAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        });

//
//        mMessageDatabase.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
//            public void onChildAdded(DataSnapshot snapshot, String s) {
//                lastChildKey = snapshot.getKey();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        Query newItems = mMessageDatabase.orderByKey().startAt(lastChildKey);
        mMessageDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.update(chatMessage);
                Log.e("Data", dataSnapshot.getValue() + "");
                rv_chat_list.smoothScrollToPosition(chatAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
//        chatAdapter.update(chatMessage);
//        Log.e("Data",dataSnapshot.getValue()+"");
//        rv_chat_list.smoothScrollToPosition(chatAdapter.getItemCount());
    }

    private void sendChat(String text, String user, String image, String user_id, String time) {
//        String key = mMessageDatabase.child("timeout_72").child("message").push().getKey();
//
//        Map<String, Object> postValues = new HashMap();
//        postValues.put(Constant.MESSAGE_TEXT, text);
//        postValues.put(Constant.MESSAGE_USER, user);
//        postValues.put(Constant.MESSAGE_ID, user_id);
//        postValues.put(Constant.MESSAGE_TIME, time);
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/timeout_72/message/" + roomID + "/" + key, postValues);
//
//        mMessageDatabase.push().setValue(childUpdates);
//
//        mMessageDatabase.updateChildren(childUpdates);
//
//        sendPushNotificationToReceiver(user, text, user_id, Utility.
//                getAppPrefString(getApplicationContext(), Constant.ARG_FIREBASE_TOKEN), token);

        final Map<String, String> map = new HashMap<String, String>();

        map.put("sender_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
        map.put("recever_id", uid);
        map.put("sender_name", user);
        map.put("recevier_name", userName);
        map.put("text", text);
        map.put("text_id", new Random().nextInt() + "");
        map.put("date", time);
        map.put("userImage", userImage);
        map.put("devide_token", token);
        mMessageDatabase.push().setValue(map);

        sendPushNotificationToReceiver(user, image, text, user_id, Utility.getAppPrefString(mContext, Constant.ARG_FIREBASE_TOKEN)
                , token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
        Utility.writeSharedPreferencesInt(getApplicationContext(),NOTI_COUNT,0);

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nMgr != null) {
            nMgr.cancelAll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    private void sendPushNotificationToReceiver(String username,
                                                String image,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .image(image)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }


    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("type")!=null &&
                getIntent().getStringExtra("type").equalsIgnoreCase("notification")) {
            Intent intent = new Intent(mContext, ChatListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("type", "notification");
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}