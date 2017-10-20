package atlascience.bitmaptest.Models.TrainMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by Ilyas on 8/24/2017.
 */

public class Codes {

    JsonObject js;

    public Codes(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData(){
        return  js.get("codes").getAsJsonArray();
    }

    public int size(){
        return getData().size();
    }

    public String code_url(int pos){
        return  getData().get(pos).getAsJsonObject().get("code_url").getAsString();
    }
    public String code_name(int pos){
        return  getData().get(pos).getAsJsonObject().get("code_name").getAsString();
    }

    public String get_name(){
        return js.get("name").getAsString();
    }


    public String get_true_answer(){
        return js.get("true_answer").getAsString();
    }


}
