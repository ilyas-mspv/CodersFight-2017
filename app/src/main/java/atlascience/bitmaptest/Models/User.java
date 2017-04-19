package atlascience.bitmaptest.Models;


import com.google.gson.JsonObject;

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

    public String getUrl(){return Response.get("photo").getAsString();}

    public int getStatus(){
        return Response.get("account_status").getAsInt();
    }

}

