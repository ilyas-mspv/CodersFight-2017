package atlascience.bitmaptest.Models;

import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by Ilyas on 9/16/2017.
 */

public class SuccessResponse {

    JsonObject js;
    public SuccessResponse(Response<JsonObject> response) {
        js = response.body();
    }

    public boolean success(){
        return  js.get("success").getAsInt() == 1;
    }
    public String message(){
        if(js.get("message").getAsString().equals("")){
            return "Null";
        }else{
            return  js.get("message").getAsString();
        }
    }

    public String responseString(String responseName){
        return  js.get(responseName).getAsString();
    }

    public int responseInt(String responseName){
        return  js.get(responseName).getAsInt();
    }
}
