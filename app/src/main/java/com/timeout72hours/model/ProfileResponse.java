package com.timeout72hours.model;

/**
 * Created by hardip on 3/12/17.
 */

public class ProfileResponse {

    private String response;
    private String message;
    private String name;
    private String dob;
    private String city;
    private String countries_id;
    private String image;
    private String gender;
    private String age;
    private String countries_name;

    public String getFriend_request_noti_status() {
        return friend_request_noti_status;
    }

    public void setFriend_request_noti_status(String friend_request_noti_status) {
        this.friend_request_noti_status = friend_request_noti_status;
    }

    private String friend_request_noti_status;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountries_id() {
        return countries_id;
    }

    public void setCountries_id(String countries_id) {
        this.countries_id = countries_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountries_name() {
        return countries_name;
    }

    public void setCountries_name(String countries_name) {
        this.countries_name = countries_name;
    }
}
