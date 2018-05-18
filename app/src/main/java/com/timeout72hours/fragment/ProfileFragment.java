package com.timeout72hours.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CountryData;
import com.timeout72hours.model.FaqResponse;
import com.timeout72hours.model.ProfileResponse;
import com.timeout72hours.model.ProfileUpdateResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hardip on 3/12/17.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View view;
    private Context mContext;
    private ImageView ivUserImage;
    private EditText etName, etEmail, etMobile, etCountry, etCity;
    private TextView tvDate;
    private RadioGroup rgGender;
    private Button btn_submit;

    private String gender = "Male", dateString = "";
    private LinearLayout llDate;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;

    private APIInterface apiInterface;
    private File fnew;
    private String base64ImageString = "", fileExt = "", selectedCountryId = "";
    private String[] countryId, countryName;
    private ArrayList<String> list_cid = new ArrayList<>();

    private CustomProgressDialog mProgressDialog;
    private Intent data;
    private SwitchCompat switch_friend_noti;
    private boolean is_camera = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_profile, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        switch_friend_noti =  view.findViewById(R.id.switch_friend_noti);

        etName = (EditText) view.findViewById(R.id.et_name);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etMobile = (EditText) view.findViewById(R.id.et_mobile);
        etCountry = (EditText) view.findViewById(R.id.et_Country);
        etCountry.setOnClickListener(this);
        etCity = (EditText) view.findViewById(R.id.et_city);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        llDate = (LinearLayout) view.findViewById(R.id.ll_date);
        llDate.setOnClickListener(this);

        ivUserImage = (ImageView) view.findViewById(R.id.iv_user_image);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        rgGender = (RadioGroup) view.findViewById(R.id.rg_gender);
        rgGender.setOnCheckedChangeListener(this);

        ivUserImage.setOnClickListener(this);

        getCountries();

    }

    private void getCountries() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            Call<CountryData> call = apiInterface.getCountries();

            call.enqueue(new Callback<CountryData>() {
                @Override
                public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CountryData countryData = response.body();

                        countryId = new String[countryData.getContries_list().size()];
                        countryName = new String[countryData.getContries_list().size()];

                        for (int i = 0; i < countryData.getContries_list().size(); i++) {
                            list_cid.add(countryData.getContries_list().get(i).getId());
                            countryId[i] = countryData.getContries_list().get(i).getId();
                            countryName[i] = countryData.getContries_list().get(i).getName();
                        }
                        getUserProfile();
                    } else {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryData> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }


    private void getUserProfile() {
        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.getUserProfile(Utility.getAppPrefString(mContext, Constant.USER_ID)).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        try {
                            etName.setText(response.body().getName());
                            etMobile.setText(Utility.getAppPrefString(mContext, Constant.MOBILE));
                            etEmail.setText(Utility.getAppPrefString(mContext, Constant.EMAIL));

                            selectedCountryId = response.body().getCountries_id();
                            etCountry.setText(countryName[list_cid.indexOf(selectedCountryId)]);
                            etCity.setText(response.body().getCity());
                            gender = response.body().getGender();
                            if (gender != null && gender.equalsIgnoreCase("Female")) {
                                rgGender.check(R.id.rb_female);
                            } else {
                                gender = "Male";
                                rgGender.check(R.id.rb_male);
                            }

                         //   Picasso.with(getActivity()).invalidate(response.body().getImage());
                            if (response.body().getImage()!=null && response.body().getImage().trim().length()>0){
                                Picasso.with(mContext).load(response.body().getImage()).error(R.drawable.user).transform(new CircleTransform()).into(ivUserImage);
                            } else {
                                Picasso.with(mContext).load(R.drawable.user).transform(new CircleTransform()).into(ivUserImage);
                            }
                               if (response.body().getFriend_request_noti_status().equals("1")){
                                   switch_friend_noti.setChecked(true);
                               } else {
                                   switch_friend_noti.setChecked(false);
                               }
                            dateString = response.body().getDob();

                            if (!dateString.equalsIgnoreCase("")) {
                                try {
                                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = format1.parse(dateString);
                                    mCalendar.setTime(date);
                                    mYear = mCalendar.get(Calendar.YEAR);
                                    mMonth = mCalendar.get(Calendar.MONTH);
                                    mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                                } catch (Exception e) {
                                }
                            }
                            tvDate.setText(Utility.sendDateConvertreverse(dateString));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });

        }


    }

    public void showCountryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
        builder.setItems(countryName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etCountry.setText(countryName[which]);
                selectedCountryId = countryId[which];
            }
        });
        builder.setTitle("Select Country");
        builder.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                gender = "Male";
                break;
            case R.id.rb_female:
                gender = "Female";
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (etName.getText().toString().length() == 0 ||
                        etCountry.getText().toString().length() == 0 ||
                        etCity.getText().toString().length() == 0 ||
                        dateString.equalsIgnoreCase("")) {
                    if (etName.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Full name");
                    } else if (etCountry.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Select Your Country");
                    } else if (etCity.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Select Your City");
                    } else if (dateString.equalsIgnoreCase("")) {
                        Utility.showToast(getActivity(), "Select Date");
                    }
                } else {
                    updateProfile();
                }
                break;
            case R.id.ll_date:
                showDatePickerDialog();
                break;
            case R.id.iv_user_image:
                ChooseGalleryOrCamera();
                break;
            case R.id.et_Country:
                showCountryList();
                break;
            default:
                break;
        }
    }

    private void updateProfile() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            int friend_noti=1;
            if (switch_friend_noti.isChecked()){
                friend_noti=1;
            } else {
                friend_noti=0;
            }
            apiInterface.updateUserProfile(Utility.getAppPrefString(mContext, Constant.USER_ID),
                    etName.getText().toString(), dateString,
                    etCity.getText().toString(), selectedCountryId,
                    gender,friend_noti+"").enqueue(new Callback<ProfileUpdateResponse>() {
                @Override
                public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.showToastSuccess(getActivity(), response.body().getMessage());
                        if (response.body().getResponse().equalsIgnoreCase("true")) {
                            Utility.writeSharedPreferences(mContext, Constant.NAME, etName.getText().toString());
                            getActivity().onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }


    private void ChooseGalleryOrCamera() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (Utility.isPermissionGranted(mContext)) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        fnew = new File(android.os.Environment.getExternalStorageDirectory(), "img" + System.currentTimeMillis() + ".jpg");

                        if (android.os.Build.VERSION.SDK_INT >= 24) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    FileProvider.getUriForFile(mContext, getActivity().getPackageName() + ".provider", fnew));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fnew));
                        }

                        startActivityForResult(intent, 12);
                        dialog.dismiss();
                    } else {
                        Utility.openSettings(getActivity(), "Please enable Camera and Storage permissions from setting to continue");
                        dialog.dismiss();
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (Utility.isPermissionGranted(mContext)) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, 13);
                        dialog.dismiss();
                    } else {
                        Utility.openSettings(getActivity(), "Please enable Storage permission from setting to continue");
                        dialog.dismiss();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showDatePickerDialog() {
        DatePickerDialog dpd = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateString = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        tvDate.setText(Utility.getDateConvert(dateString));
                        dateString = Utility.sendDateConvert(dateString);

                        if (!dateString.equalsIgnoreCase("")) {
                            try {
                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = format1.parse(dateString);
                                mCalendar.setTime(date);
                                mYear = mCalendar.get(Calendar.YEAR);
                                mMonth = mCalendar.get(Calendar.MONTH);
                                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                            } catch (Exception e) {
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 12:

                    try {
                        is_camera = true;
                        LoadImage loadImage = new LoadImage();
                        loadImage.execute();
                    }
                    catch (Exception e) {
                        Log.w("TAG", "-- Error in setting image");
                    }
                    catch(OutOfMemoryError oom) {
                        Log.w("TAG", "-- OOM Error in setting image");
                    }

//                    try {
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        bmOptions.inSampleSize = 2;
//                        Bitmap bitmap = BitmapFactory.decodeFile(fnew.getAbsolutePath(), bmOptions);
//                        FileOutputStream out = new FileOutputStream(fnew);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                        out.close();
//                        ExifInterface ei;
//                        if (Build.VERSION.SDK_INT > 23) {
//                            InputStream input = getActivity().getContentResolver().openInputStream(Uri.fromFile(fnew));
//                            ei = new ExifInterface(input);
//                        } else{
//                            ei = new ExifInterface(fnew.getAbsolutePath());}
//                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_UNDEFINED);
//
////                        Bitmap rotatedBitmap = null;
//                        switch(orientation) {
//
//                            case ExifInterface.ORIENTATION_ROTATE_90:
//                                bitmap = rotateImage(bitmap, 90);
//                                break;
//
//                            case ExifInterface.ORIENTATION_ROTATE_180:
//                                bitmap = rotateImage(bitmap, 180);
//                                break;
//
//                            case ExifInterface.ORIENTATION_ROTATE_270:
//                                bitmap = rotateImage(bitmap, 270);
//                                break;
//
//                            case ExifInterface.ORIENTATION_NORMAL:
//                            default:
//                                bitmap = bitmap;
//                        }
//
//
//
//                        base64ImageString = Utility.getStringFile(fnew);
//                        fileExt = Utility.getFileExt(fnew.getAbsolutePath());
//
//                        Picasso.with(mContext).load(Uri.fromFile(fnew)).transform(new CircleTransform()).into(ivUserImage);
//
//                        updateUserImage();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    break;

                case 13:
                    if (data == null) {
                        return;
                    }
                    is_camera = false;
                    this.data = data;
                    LoadImage loadImage = new LoadImage();
                    loadImage.execute();
                    break;
                default:
                    break;
            }
        }
    }


    private class LoadImage extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if (is_camera){

                    ExifInterface exif = new ExifInterface(fnew.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    }
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    }
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inSampleSize = 2;
                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(fnew), null, bmOptions);
                    Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                    FileOutputStream out = new FileOutputStream(fnew);
                    correctBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    base64ImageString = Utility.getStringFile(fnew);
                    fileExt = Utility.getFileExt(fnew.getAbsolutePath());


                } else {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String path = c.getString(columnIndex);
                    c.close();
                    fnew = new File(path);
                    Log.e("files", "+" + fnew.length());
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(fnew.getAbsolutePath(), bmOptions);
                    FileOutputStream out = new FileOutputStream(fnew);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();

                    Log.e("files2", "+" + fnew.length());
                    base64ImageString = Utility.getStringFile(fnew);
                    fileExt = Utility.getFileExt(fnew.getAbsolutePath());

                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
//                Picasso.with(mContext).load(Uri.fromFile(fnew)).transform(new CircleTransform()).into(ivUserImage);
//
//                updateUserImage();
                Picasso.with(mContext).load(Uri.fromFile(fnew)).transform(new CircleTransform()).into(ivUserImage);

                updateUserImage();
            }
        }
    }

    private void updateUserImage() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            apiInterface.updateUserImage(Utility.getAppPrefString(mContext, Constant.USER_ID), base64ImageString, fileExt).enqueue(new Callback<ProfileUpdateResponse>() {
                @Override
                public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.showToastSuccess(getActivity(), response.body().getMessage());
                        if (response.body().getResponse().equalsIgnoreCase("true")) {
                            Utility.writeSharedPreferences(mContext, Constant.IMAGE, response.body().getImage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }

    }

}
