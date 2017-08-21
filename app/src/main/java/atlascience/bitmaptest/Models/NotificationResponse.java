package atlascience.bitmaptest.Models;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by Ilyas on 8/18/2017.
 */

public class NotificationResponse {

    JsonObject js;

    public NotificationResponse(Response<JsonObject> response) {
        this.js = response.body();
    }


}
