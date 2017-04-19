package atlascience.bitmaptest.Models.Knowledge;


import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Response;

public class Topics {

    JsonObject res;
    private String content;
    private  String topic;

    public Topics(Context context, Response<JsonObject> response) {
        res = response.body();

    }

    public JsonArray getData(){
        return res.get("topics").getAsJsonArray();
    }
    public int size(){
        return getData().size();
    }

    public String getContent(int position) {
        return getData().get(position).getAsJsonObject().get("content").getAsString();
    }

    public String getTopic(int position) {
        return getData().get(position).getAsJsonObject().get("topic").getAsString();
    }
}
