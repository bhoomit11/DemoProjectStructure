package com.timeout72hours.attributes.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.timeout72hours.R;
import com.timeout72hours.activities.ChatActivity;
import com.timeout72hours.activities.ChatListActivity;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import static com.timeout72hours.attributes.Constant.NOTI_COUNT;
import static com.timeout72hours.attributes.Constant.USER_ID;

/**
 * Created by bhumit.belani on 11/21/2016.
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    int numMessages = 0;
//    Notification.InboxStyle inboxStyle;
//
//    Uri defaultSoundUri;
//
//    public MyFcmListenerService() {
//        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        inboxStyle = new Notification.InboxStyle();
//    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        try {
            if (message.getData() != null) {
                Map data = message.getData();
                Log.e(Constant.TAG, "Main Data: " + data.toString());

                JSONObject jsonObject = new JSONObject(data);

                if (jsonObject.has("Type") && jsonObject.get("Type").toString().equalsIgnoreCase("friends")) {

                    sendNotification(jsonObject.getString("title"), jsonObject.getString("message"), "", "friends",
                            "", "",jsonObject.getString("sub_type"));

                } else if (jsonObject.has("Type") && jsonObject.get("Type").toString().equalsIgnoreCase("general")) {

                    sendNotification(jsonObject.getString("title"), jsonObject.getString("message"), "", "general",
                            "", "","");

                } else {
                    if (!FirebaseChatMainApp.isChatActivityOpen()) {
                        sendNotification(jsonObject.getString("username"), jsonObject.getString("text"),
                                jsonObject.getString("image"), "chat",
                                jsonObject.getString("fcm_token"), jsonObject.getString("uid"),"");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String Title, String message, String image, String type, String token, String id,String sub_type) {
        Intent intent = null;
        if (Utility.getAppPrefString(getApplicationContext(), USER_ID).equalsIgnoreCase("")) {
            intent = new Intent(this, LoginRegisterActivity.class);
        } else {
            if (type.equalsIgnoreCase("chat")) {
                int count = Utility.getAppPrefInt(getApplicationContext(), NOTI_COUNT);

                if (count > 0) {
                    Title = "New Messages";
                    message = "You have some unread messages.";
                    intent = new Intent(this, ChatListActivity.class);
                    intent.putExtra("type", "notification");
                } else {
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("type", "notification");
                    intent.putExtra("name", Title);
                    intent.putExtra("image", image);
                    intent.putExtra("token", token);
                    intent.putExtra("id", id);
                }
                count = count + 1;
                Utility.writeSharedPreferencesInt(getApplicationContext(), NOTI_COUNT, count);

            } else if (type.equalsIgnoreCase("friends")) {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("type", "notification");
                intent.putExtra("sub_type",sub_type);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt() /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ticker_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                .setContentTitle(Title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (type.equalsIgnoreCase("chat")) {
            notificationManager.notify(237 /* ID of notification */, notificationBuilder.build());
        } else {
            notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
        }
    }


//    private void sendNotificationInbox(String Title, String message, String image, String type, String token, String id) {
//        Intent intent = null;
//        if (Utility.getAppPrefString(getApplicationContext(), Constant.USER_ID).equalsIgnoreCase("")) {
//            intent = new Intent(this, LoginRegisterActivity.class);
//        } else {
//            if (type.equalsIgnoreCase("chat")) {
//                intent = new Intent(this, ChatActivity.class);
//                intent.putExtra("type", "notification");
//                intent.putExtra("name", Title);
//                intent.putExtra("image", image);
//                intent.putExtra("token", token);
//                intent.putExtra("id", id);
//            }
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, new Random().nextInt() /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Notification.Builder notificationBuilder = new Notification.Builder(this)
//                .setSmallIcon(R.mipmap.ticker_logo)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
//                .setContentTitle(Title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(this)
//                .setContentTitle("New Message")
//                .setContentText(Title + ": " + message)
//                .setSmallIcon(R.mipmap.ticker_logo)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
//                .setAutoCancel(true);
//
//
//        inboxStyle.setBigContentTitle("New Messages");
//        inboxStyle.addLine(Title + ": " + message);
//        builder.setStyle(inboxStyle);
//        nManager.notify(1, builder.build());
//
////        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
//    }
}