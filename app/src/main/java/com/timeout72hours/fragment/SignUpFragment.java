package com.timeout72hours.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookHelper;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookResponse;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookUser;
import com.timeout72hours.attributes.socialloginhelper.googleSignIn.GooglePlusSignInHelper;
import com.timeout72hours.attributes.socialloginhelper.googleSignIn.GoogleResponseListener;
import com.timeout72hours.model.CountryData;
import com.timeout72hours.model.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by bhumit on 29/11/17.
 */

public class SignUpFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, FacebookResponse, GoogleResponseListener {
    private Context mContext;
    private View view;
    private ImageView ivUserImage;
    private EditText etName, etEmail, etMobile, etPassword, etConfirmPassword, etCountry, etCity;
    private TextView tvDate;
    private RadioGroup rgGender;
    private Button btnSignUp;

    private String gender = "Male", dateString = "";
    private LinearLayout llDate;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;

    private APIInterface apiInterface;
    private File fnew;
    private String base64ImageString = "", fileExt = "", selectedCountryId = "";
    private String[] countryId, countryName;

    private CustomProgressDialog mProgressDialog;

    private GooglePlusSignInHelper mGHelper = null;
    private FacebookHelper mFbHelper = null;

    private ImageView mBtnGoogleSignIn;
    private ImageView mBtnFacebookSignIn;

    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 101;
    private final int TWITTER_SIGN_IN_REQUEST_CODE = 140;

    private UserDetails userDetails;
    private CustomProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_signup, null);

        initUI();

        return view;
    }

    private void initUI() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        etName = (EditText) view.findViewById(R.id.et_name);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etMobile = (EditText) view.findViewById(R.id.et_mobile);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);
        etCountry = (EditText) view.findViewById(R.id.et_Country);
        etCountry.setOnClickListener(this);
        etCity = (EditText) view.findViewById(R.id.et_city);
        btnSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);


        if (mGHelper == null) {
            //Google api initialization
            mGHelper = new GooglePlusSignInHelper(getActivity(), (GoogleResponseListener) SignUpFragment.this);
        }

        if (mFbHelper == null) {
            //fb api initialization
            mFbHelper = new FacebookHelper((FacebookResponse) SignUpFragment.this,
                    "id,name,email,gender,birthday,picture.type(large),cover",
                    getActivity());
        }

        mBtnGoogleSignIn = (ImageView) view.findViewById(R.id.btn_google_sign_in_button);
        mBtnFacebookSignIn = (ImageView) view.findViewById(R.id.bt_act_login_fb);
        mBtnGoogleSignIn.setOnClickListener(this);
        mBtnFacebookSignIn.setOnClickListener(this);


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
            Call<CountryData> call = apiInterface.getCountries();

            call.enqueue(new Callback<CountryData>() {
                @Override
                public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CountryData countryData = response.body();

                        countryId = new String[countryData.getContries_list().size()];
                        countryName = new String[countryData.getContries_list().size()];

                        for (int i = 0; i < countryData.getContries_list().size(); i++) {
                            countryId[i] = countryData.getContries_list().get(i).getId();
                            countryName[i] = countryData.getContries_list().get(i).getName();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryData> call, Throwable t) {
                 //   Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
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
            case R.id.btn_sign_up:
                Utility.hideSoftKeyboard(getActivity());
                if (etName.getText().toString().length() == 0 ||
                        etEmail.getText().toString().length() == 0 ||
                        etMobile.getText().toString().length() == 0 ||
                        etPassword.getText().toString().length() == 0 ||
                        etConfirmPassword.getText().toString().length() == 0 ||
                        etCountry.getText().toString().length() == 0 ||
                        etCity.getText().toString().length() == 0 ||
                        dateString.equalsIgnoreCase("") ||
                        etMobile.getText().toString().length() != 10 ||
                        !Utility.isValidEmail(etEmail.getText().toString()) ||
                        !etPassword.getText().toString()
                                .equalsIgnoreCase(etConfirmPassword.getText().toString())) {
                    if (etName.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Full name");
                    } else if (etEmail.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Email ID");
                    } else if (etMobile.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Mobile Number");
                    } else if (etCountry.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Select Your Country");
                    } else if (etCity.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Select Your City");
                    } else if (etPassword.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Password");
                    } else if (etConfirmPassword.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Confirm Password");
                    } else if (dateString.equalsIgnoreCase("")) {
                        Utility.showToast(getActivity(), "Select Date");
                    } else {
                        if (etMobile.getText().toString().length() != 10) {
                            Utility.showToast(getActivity(), "Enter Valid Mobile Number");
                        } else if (!Utility.isValidEmail(etEmail.getText().toString())) {
                            Utility.showToast(getActivity(), "Enter Valid Email ID");
                        } else if (!etPassword.getText().toString()
                                .equalsIgnoreCase(etConfirmPassword.getText().toString())) {
                            Utility.showToast(getActivity(), "Password & Confirm Password Must be Same");
                        }
                    }
                } else {
                    performRegister();
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
            case R.id.btn_google_sign_in_button:
                mGHelper.performSignIn();
                break;
            case R.id.bt_act_login_fb:
                mFbHelper.performSignIn(getActivity());
                break;
            default:
                break;
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

    private void performRegister() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            Call<JsonObject> call = apiInterface.register(etName.getText().toString(),
                    etEmail.getText().toString(), etMobile.getText().toString(),
                    Utility.sendDateConvert(dateString), etCity.getText().toString(),
                    selectedCountryId, gender,
                    etPassword.getText().toString(), base64ImageString, fileExt,
                    Utility.getAppPrefString(getActivity(), Constant.ARG_FIREBASE_TOKEN), "android");

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (!response.body().get("response").getAsString().equals("false")) {
                            sendOTP(response.body().get("mobile").getAsString());
                        } else {
                            Utility.showToast(getActivity(), response.body().get("message").getAsString());
                        }

                        // ((LoginRegisterActivity) getActivity()).toOtpFrag(response.body().get("mobile").getAsString(),"signup");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    public void showDatePickerDialog() {
        DatePickerDialog dpd = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateString = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        tvDate.setText(Utility.getDateConvert(dateString));
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


//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        bmOptions.inSampleSize = 2;
//                        Bitmap bitmap = BitmapFactory.decodeFile(fnew.getAbsolutePath(), bmOptions);
//                        FileOutputStream out = new FileOutputStream(fnew);
//
//                        ExifInterface ei = new ExifInterface(fnew.getAbsolutePath());
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
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                        out.close();

                        base64ImageString = Utility.getStringFile(fnew);
                        fileExt = Utility.getFileExt(fnew.getAbsolutePath());

                        Picasso.with(mContext).load(Uri.fromFile(fnew)).transform(new CircleTransform()).into(ivUserImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 13:
                    if (data == null) {
                        return;
                    }

                    try {
                        Uri selectedImage = data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                                null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String path = c.getString(columnIndex);
                        c.close();

                        fnew = new File(path);

                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        bmOptions.inSampleSize = 2;
                        Bitmap bitmap = BitmapFactory.decodeFile(fnew.getAbsolutePath(), bmOptions);
                        FileOutputStream out = new FileOutputStream(fnew);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.close();

                        Picasso.with(mContext).load(Uri.fromFile(fnew)).transform(new CircleTransform()).into(ivUserImage);

                        base64ImageString = Utility.getStringFile(fnew);
                        fileExt = Utility.getFileExt(fnew.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case GOOGLE_SIGN_IN_REQUEST_CODE:
                    mGHelper.onActivityResult(requestCode, resultCode, data);
                    break;
                default:
                    if (mFbHelper != null) {
                        mFbHelper.onActivityResult(requestCode, resultCode, data);
                    }
                    break;
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    public void onFbSignInFail() {

    }

    @Override
    public void onFbSignInSuccess() {

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {

        userDetails = new UserDetails();
        if (facebookUser.facebookID != null) {
            userDetails.setID(facebookUser.facebookID);
            if (facebookUser.name != null) {
                userDetails.setName(facebookUser.name);
            }
            if (facebookUser.gender != null) {
                if (facebookUser.gender.equalsIgnoreCase("male")) {
                    userDetails.setGender("Male");
                } else {
                    userDetails.setGender("Female");
                }
            }
            if (facebookUser.email != null) {
                userDetails.setEmail(facebookUser.email);
            }
            if (facebookUser.profilePic != null) {
                userDetails.setProfilePic(facebookUser.profilePic);
            }
            if (facebookUser.coverPicUrl != null) {
                userDetails.setCoverPicUrl(facebookUser.coverPicUrl);
            }

            performSocialLogin("facebook");
        }
    }

    @Override
    public void onGSignInFail() {

    }

    @Override
    public void onGSignInSuccess(GoogleSignInAccount accountDetails) {

//        accountDetails.get();

        userDetails = new UserDetails();
        if (accountDetails.getId() != null) {
            userDetails.setID(accountDetails.getId());
        }
        if (accountDetails.getDisplayName() != null) {
            userDetails.setName(accountDetails.getDisplayName());
        }
        if (accountDetails.getEmail() != null) {
            userDetails.setEmail(accountDetails.getEmail());
        }
        if (accountDetails.getPhotoUrl() != null) {
            userDetails.setProfilePic(accountDetails.getPhotoUrl() + "");
        }

        performSocialLogin("google");
    }

    private void performSocialLogin(String social_type) {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            Call<JsonObject> call = apiInterface.socialLogin(userDetails.getID(), social_type,
                    userDetails.getName(), userDetails.getEmail(), "", "",
                    userDetails.getGender(), "", userDetails.getProfilePic(), "", Utility.getAppPrefString(getActivity(), Constant.ARG_FIREBASE_TOKEN), "android");

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")) {
                            Utility.writeSharedPreferences(mContext, Constant.SOCIAL_ID, response.body().get("users_id").getAsString());

                            if (response.body().get("mobile") != null && response.body().get("mobile").getAsString().equalsIgnoreCase("")) {
                                if (response.body().get("email") != null) {
                                    ((LoginRegisterActivity) mContext).changefragment(new SocialVerificationFragment(), response.body().get("email").getAsString());
                                } else {
                                    ((LoginRegisterActivity) mContext).changefragment(new SocialVerificationFragment(), "");
                                }

                            } else {
                                if (response.body().get("mobile_verify").getAsString().equalsIgnoreCase("0")) {
                                    sendOTP(response.body().get("mobile").getAsString());
                                } else {
                                    Utility.showToastSuccess(getActivity(), response.body().get("message").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.USER_ID, response.body().get("users_id").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.NAME, response.body().get("name").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.EMAIL, response.body().get("email").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.MOBILE, response.body().get("mobile").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.IMAGE, response.body().get("image").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.SERIAL_NUMBER, response.body().get("serial_no").getAsString());

                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    getActivity().finish();
                                }

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
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

    private void sendOTP(final String mobile) {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            Call<JsonObject> call = apiInterface.forgotPassword(mobile);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        final JsonObject jsonObject = response.body();

                        if (jsonObject.get("response").getAsString().equalsIgnoreCase("true")) {
                            ((LoginRegisterActivity) getActivity()).toOtpFrag(mobile, "signup", jsonObject.get("sent_on").getAsString());
                        } else {
                            Utility.showToast(getActivity(), jsonObject.get("message").getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

}
