package atlascience.bitmaptest.Objects;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import retrofit2.Response;

public class User {
    JsonObject Response;

    public User(Response<JsonObject> response) {
        Response = response.body().getAsJsonObject();
    }

    public String getUsername() {
        return Response.get("username").getAsString();
    }

    public int getId() {
        return Response.get("id").getAsInt();
    }

    public String getEmail() {
        return Response.get("email").getAsString();
    }





}

