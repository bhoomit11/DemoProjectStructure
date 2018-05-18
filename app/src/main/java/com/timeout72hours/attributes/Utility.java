package com.timeout72hours.attributes;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timeout72hours.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Vivek on 26-Dec-16.
 */
public class Utility {

    public static void showToast(Activity context, String msg) {
        LayoutInflater inflater = context.getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, null);

        TextView custom_toast_message = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
        custom_toast_message.setText(msg);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    public static void showToastSuccess(Activity context, String msg) {
        LayoutInflater inflater = context.getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, null);

        TextView custom_toast_message = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
        ImageView custom_toast_image = (ImageView) toastLayout.findViewById(R.id.custom_toast_image);
        custom_toast_image.setImageResource(R.drawable.icon);
        custom_toast_message.setText(msg);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    public static void writeSharedPreferences(Context mContext, String key,
                                              String value) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = mContext.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        // Commit the edits!
        editor.commit();
    }


    public static String getAppPrefString(Context mContext, String key) {
        try {
            SharedPreferences settings = mContext.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_MULTI_PROCESS);
            String value = settings.getString(key, "");
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void writeSharedPreferencesInt(Context mContext, String key,
                                              int value) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = mContext.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        // Commit the edits!
        editor.commit();
    }

    public static int getAppPrefInt(Context mContext, String key) {
        try {
            SharedPreferences settings = mContext.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_MULTI_PROCESS);
            int value = settings.getInt(key, 0);
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    /*
     * To close KeyBoard if it is open
    */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getDateConvert(String str) {
        String DateString = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
            Date date = format1.parse(str);
            DateString = format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DateString;
    }

    public static String sendDateConvert(String str) {
        String DateString = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format1.parse(str);
            DateString = format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DateString;
    }

    public static String sendDateConvertreverse(String str) {
        String DateString = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
            Date date = format1.parse(str);
            DateString = format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DateString;
    }

    public static boolean isPermissionGranted(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else if (context.checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void openSettings(Activity context, String msg) {
        showToast(context,msg);

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        Log.d("lastv", lastVal);
        return lastVal;
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static Calendar getDateFromMillis(String milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return calendar;
//        return formatter.format(calendar.getTime());
    }

    public static String getDateFromMillisString(String milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
//        return calendar;
        return formatter.format(calendar.getTime());
    }
}
