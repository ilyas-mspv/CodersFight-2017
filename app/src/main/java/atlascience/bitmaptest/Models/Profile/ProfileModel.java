package atlascience.bitmaptest.Models.Profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by Ilyas on 10/5/2017.
 */

public class ProfileModel  {

    private JsonObject js;

    public ProfileModel(Response<JsonObject> response) {
        js = response.body();
    }

    public JsonArray getData(){
        return js.get("ids").getAsJsonArray();
    }

    public  int size(){
        return getData().size();
    }

    public String getId(int pos){
        return  getData().get(pos).getAsJsonObject().get("id").getAsString();
    }
}
