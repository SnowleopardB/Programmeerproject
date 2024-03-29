package com.example.maplife2;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserGetRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Context context2;
    private Callback callback2;

    public UserGetRequest(Context context, int userID) {

        context2 = context;
    }

    // method that makes new jsonObjectRequest and puts it in the queue.
    public void getUser(Callback activity, int userID) {

        callback2 = activity;
        String number = String.valueOf(userID);
        String url = "https://ide50-bart1997.legacy.cs50.io:8080/Maplife8/" +number;
        RequestQueue queue = Volley.newRequestQueue(context2);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, this, this);
        queue.add(jsonObjectRequest);
    }

    // define callback
    public interface Callback {
        void gotUser(User user);
        void gotUserError(String message);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMessage = error.getMessage();
        error.printStackTrace();
        callback2.gotUserError(errorMessage);
    }

    // onResponse, transcribe the JSONObject in the response to a more usable datatype:
    // A User object.
    @Override
    public void onResponse(JSONObject response) {

        ArrayList<Location> locationsarraylist = new ArrayList<>();
        ArrayList<Friend> friendsarraylist = new ArrayList<>();

        try {
            int ID = response.getInt("id");
            String password = response.getString("password");
            String name = response.getString("name");
            String email = response.getString("email");

            String locations = response.getString("location");
            JSONArray test = new JSONArray(locations);

            for (int i=0; i <test.length(); i++) {
                JSONObject location = test.getJSONObject(i);

                Location loc = new Location(
                        location.getString("latitude"),
                        location.getString("longitude"),
                        location.getString("name"),
                        location.getString("description")
                );
                locationsarraylist.add(loc);
            }

            String friends = response.getString("friends");
            JSONArray JSONfriend = new JSONArray(friends);

            for (int i=0; i <JSONfriend.length(); i++) {
                JSONObject friendobject = JSONfriend.getJSONObject(i);

                Friend friend = new Friend(
                        friendobject.getInt("id"),
                        friendobject.getString("name")
                );
                friendsarraylist.add(friend);
            }
            User user = new User(ID, email, name, password, locationsarraylist, friendsarraylist);
            callback2.gotUser(user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
