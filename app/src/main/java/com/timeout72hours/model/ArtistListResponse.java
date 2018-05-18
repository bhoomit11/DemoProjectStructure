package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 2/12/17.
 */

public class ArtistListResponse {

    private String response,message;
    private ArrayList<ArtistListData> artist_list;

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

    public ArrayList<ArtistListData> getArtist_list() {
        return artist_list;
    }

    public void setArtist_list(ArrayList<ArtistListData> artist_list) {
        this.artist_list = artist_list;
    }
}
