package com.timeout72hours.attributes;

/**
 * Created by bhumit on 29/11/17.
 */

public class Constant {
    public static final String TAG = "Timeout72";

    public static final String ARG_USERS = "users";
    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_RECEIVER_UID = "receiver_uid";
    public static final String ARG_CHAT_ROOMS = "chat_rooms";
    public static final String ARG_FIREBASE_TOKEN = "firebaseToken";

    // Constant URLs
    public static final String PREFS_NAME = "MapLocations";

    public static final String BASE_URL = "http://aditlinux.com/timeout72/";

    public static final String URL_REGISTER = BASE_URL + "user/add";
    public static final String URL_LOGIN = BASE_URL + "user/login";
    public static final String URL_SOCIAL_LOGIN = BASE_URL + "user/social_login";
    public static final String URL_FORGOT_PASSWORD = BASE_URL + "user/forgot_password";
    public static final String URL_VERIFY_OTP = BASE_URL + "user/new_pass_otp_vefify";
    public static final String URL_GET_COUNTRY = BASE_URL + "get_contries";
    public static final String URL_GET_FAQ = BASE_URL + "faqlist";
    public static final String URL_GET_ARTIST_LIST = BASE_URL + "artist_detail";
    public static final String URL_GET_USER_PROFILE = BASE_URL + "user/profile";
    public static final String URL_UPDATE_USER_PROFILE = BASE_URL + "user/update_profile";
    public static final String URL_GET_ABOUT_US = BASE_URL + "cms/about-us";
    public static final String URL_VERSION_CHECK = BASE_URL + "version_check";
    public static final String URL_GET_PASSES = BASE_URL + "get_pass_detail";
    public static final String URL_MOBILE_UPDATE = "user/update_mobile";
    public static final String URL_NEW_PASSWORD = "user/new_pass_update";
    public static final String URL_UPDATE_USER_IMAGE = "user/update_profile_image";
    public static final String URL_GET_ARTICLES = "user/articlelist";
    public static final String URL_GET_ARTICLE_DETAIL = "user/article_detail";
    public static final String URL_ADD_LIKE = "user/article_like";
    public static final String URL_ADD_COMMENT = "user/article_comment";
    public static final String URL_GET_COMMENTS = "article_comment_paginate";
    public static final String URL_GET_EVENTS = "eventlist";
    public static final String URL_SEARCH_USERS = "search_user";
    public static final String URL_SEND_FRIEND_REQUEST = "send_friend_request";
    public static final String URL_GET_FRIEND_LIST = "friend_list";
    public static final String URL_GET_FRIEND_REQUEST_LIST = "friend_request_list";
    public static final String URL_ACCEPT_REJECT_REQUESTS = "accept_reject_request";
    public static final String URL_GET_CURRENT_VERSION = "current_version";
    public static final String URL_LOGOUT = "user/logout";
    public static final String URL_LIST_ONGOING_EVENT = "ongoing_event";
    public static final String URL_SERIAL_NUMBER = "rfid_user/check";
    public static final String URL_GET_USER_TRANSACTIONS = "rfid_user/transaction";
    public static final String URL_GET_TRANSACTION_DETAIL = "transaction/detail";

    // User data constants
    public static final String NOTI_COUNT = "noti_count";
    public static final String SERIAL_NUMBER = "serial_number";
    public static final String USER_ID = "users_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String IMAGE = "image";
    public static final String SOCIAL_ID = "social_id";

    public static final String RECENT_USER = "recent_user";
    public static final String RECENT_MESSAGE = "last_message";
    public static final String RECIEVER_UID = "reciever_id";
    public static final String SENDER_UID = "sender_id";
    public static final String RECENT_TIME = "recent_time";
    public static final String RECENT_USER_IMAGE = "recent_user_image";
}
