package com.timeout72hours.attributes.retrofit;

import com.google.gson.JsonObject;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.model.AddLikeResponse;
import com.timeout72hours.model.ArticleDetailResponse;
import com.timeout72hours.model.ArticleListResponse;
import com.timeout72hours.model.ArtistListResponse;
import com.timeout72hours.model.CommentListResponse;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.CountryData;
import com.timeout72hours.model.EventListResponse;
import com.timeout72hours.model.FaqResponse;
import com.timeout72hours.model.FriendListResponse;
import com.timeout72hours.model.GetVersionResponse;
import com.timeout72hours.model.OngoingEventResponse;
import com.timeout72hours.model.PassesListResponse;
import com.timeout72hours.model.ProfileResponse;
import com.timeout72hours.model.ProfileUpdateResponse;
import com.timeout72hours.model.SearchUserListResponse;
import com.timeout72hours.model.TransactionDetailResponse;
import com.timeout72hours.model.TransactionsListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by bhumit on 13/9/17.
 */

public interface APIInterface {
    //    @GET(Constant.URL_SET_LOCATION)
//    Call<JSONObject> sendLocation(@Query("VehicleId") String VehicleId,
//                                  @Query("token") String Counter,
//                                  @Query("latitude") String latitude,
//                                  @Query("longitude") String longitude,
//                                  @Query("speed") String speed);
    @FormUrlEncoded
    @POST(Constant.URL_LOGIN)
    Call<JsonObject> login(@Field("username") String username,
                           @Field("password") String password,
                           @Field("device_token") String device_token,
                           @Field("platform") String platform);

    @FormUrlEncoded
    @POST(Constant.URL_REGISTER)
    Call<JsonObject> register(@Field("name") String name,
                              @Field("email") String email,
                              @Field("mobile") String mobile,
                              @Field("dob") String dob,
                              @Field("city") String city,
                              @Field("countries_id") String countries_id,
                              @Field("gender") String gender,
                              @Field("password") String password,
                              @Field("image") String image,
                              @Field("imageExt") String imageExt,
                              @Field("device_token") String device_token,
                              @Field("platform") String platform);

    @FormUrlEncoded
    @POST(Constant.URL_SOCIAL_LOGIN)
    Call<JsonObject> socialLogin(@Field("social_id") String social_id,
                                 @Field("social_type") String social_type,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("mobile") String mobile,
                                 @Field("dob") String dob,
                                 @Field("gender") String gender,
                                 @Field("password") String password,
                                 @Field("image") String image,
                                 @Field("imageExt") String imageExt,
                                 @Field("device_token") String device_token,
                                 @Field("platform") String platform);

    @FormUrlEncoded
    @POST(Constant.URL_VERIFY_OTP)
    Call<JsonObject> verifyOTP(@Field("mobile") String mobile,
                               @Field("otp") String otp);

    @FormUrlEncoded
    @POST(Constant.URL_MOBILE_UPDATE)
    Call<JsonObject> mobileUpdate(@Field("mobile") String mobile,
                                  @Field("users_id") String users_id,
                                  @Field("email") String email,
                                  @Field("countries_id") String countries_id);

    @FormUrlEncoded
    @POST(Constant.URL_FORGOT_PASSWORD)
    Call<JsonObject> forgotPassword(@Field("mobile") String mobile);

    @GET(Constant.URL_GET_COUNTRY)
    Call<CountryData> getCountries();

    @GET(Constant.URL_GET_FAQ)
    Call<FaqResponse> getFaqs();

    @GET(Constant.URL_GET_ARTIST_LIST)
    Call<ArtistListResponse> getArtistList();

    @FormUrlEncoded
    @POST(Constant.URL_GET_USER_PROFILE)
    Call<ProfileResponse> getUserProfile(@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_UPDATE_USER_PROFILE)
    Call<ProfileUpdateResponse> updateUserProfile(@Field("users_id") String users_id, @Field("name") String name,
                                                  @Field("dob") String dob, @Field("city") String city,
                                                  @Field("countries_id") String countries_id, @Field("gender") String gender, @Field("friend_request_noti_status") String friend_request_noti_status);

    @GET(Constant.URL_GET_ABOUT_US)
    Call<String> getAboutEvent();

    @FormUrlEncoded
    @POST(Constant.URL_VERSION_CHECK)
    Call<FaqResponse> checkVersion(@Field("device_type") String device_type, @Field("version") String version);

    @GET(Constant.URL_GET_PASSES)
    Call<PassesListResponse> getPasses();

    @FormUrlEncoded
    @POST(Constant.URL_NEW_PASSWORD)
    Call<FaqResponse> newPassOtpVerify(@Field("mobile") String mobile, @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST(Constant.URL_UPDATE_USER_IMAGE)
    Call<ProfileUpdateResponse> updateUserImage(@Field("users_id") String user_id,
                                                @Field("image") String image,
                                                @Field("imageExt") String imageExt);

    @GET(Constant.URL_GET_ARTICLES)
    Call<ArticleListResponse> getArticles();

    @FormUrlEncoded
    @POST(Constant.URL_GET_ARTICLE_DETAIL)
    Call<ArticleDetailResponse> getArticleDetail(@Field("article_id") String article_id, @Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_ADD_LIKE)
    Call<AddLikeResponse> addLike(@Field("article_id") String article_id, @Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_ADD_COMMENT)
    Call<AddLikeResponse> addComment(@Field("article_id") String article_id, @Field("users_id") String users_id, @Field("comment") String comment);

    @FormUrlEncoded
    @POST(Constant.URL_GET_COMMENTS)
    Call<CommentListResponse> getComments(@Field("article_id") String article_id, @Field("page") String page);

    @GET(Constant.URL_GET_EVENTS)
    Call<EventListResponse> getEvents();

    @FormUrlEncoded
    @POST(Constant.URL_SEARCH_USERS)
    Call<SearchUserListResponse> searchUsers(@Field("page") String page,@Field("users_id") String users_id, @Field("keyword") String keyword,
                                             @Field("gender") String gender, @Field("city") String city,
                                             @Field("countries_id") String countries_id);

    @FormUrlEncoded
    @POST(Constant.URL_SEND_FRIEND_REQUEST)
    Call<CommonResponse> sendFriendRequest(@Field("users_id") String users_id, @Field("friend_user_id") String friend_user_id, @Field("status") String status);

    @FormUrlEncoded
    @POST(Constant.URL_GET_FRIEND_LIST)
    Call<FriendListResponse> getFriendList(@Field("page") String page,@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_GET_FRIEND_REQUEST_LIST)
    Call<FriendListResponse> getFriendRequestList(@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_ACCEPT_REJECT_REQUESTS)
    Call<CommonResponse> statusRequests(@Field("request_id") String request_id, @Field("status") String status);

    @FormUrlEncoded
    @POST(Constant.URL_GET_CURRENT_VERSION)
    Call<GetVersionResponse> getVersion(@Field("device_type") String device_type,
                                        @Field("users_id") String users_id,@Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(Constant.URL_LOGOUT)
    Call<CommonResponse> logout(@Field("users_id") String users_id);


    @GET(Constant.URL_LIST_ONGOING_EVENT)
    Call<OngoingEventResponse> getOngoingEvent();

    @FormUrlEncoded
    @POST(Constant.URL_SERIAL_NUMBER)
    Call<CommonResponse> addSerialNumber(@Field("serial_no") String serial_no);

    @FormUrlEncoded
    @POST(Constant.URL_GET_USER_TRANSACTIONS)
    Call<TransactionsListResponse> getTransactions(@Field("date") String date,@Field("serial_no") String serial_no, @Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_GET_USER_TRANSACTIONS)
    Call<TransactionsListResponse> getTransaction(@Field("date") String date, @Field("users_id") String users_id);

    @FormUrlEncoded
    @POST(Constant.URL_GET_TRANSACTION_DETAIL)
    Call<TransactionDetailResponse> getTransactionDetail(@Field("transaction_id") String transaction_id);


}
