package atlascience.bitmaptest.Models.Knowledge;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class Topics {

    JsonObject res;
    String id,title,content;
    int is_open;
    private  Context context;
    private SharedPreferences shPref;
    private SharedPreferences.Editor editor;

    public Topics(Response<JsonObject> response,Context context) {
        res = response.body();
        this.context = context;
        shPref = context.getSharedPreferences("open_knowledge", Context.MODE_PRIVATE);
        editor = shPref.edit();
    }

    public Topics() {
    }

    public Topics(Context context) {
        this.context = context;
    }

    private JsonArray setData(){
        editor.putString("jsondata",res.get("topics").getAsJsonArray().toString());
        return res.get("topics").getAsJsonArray();
    }

    public int size(){
        return setData().size();
    }

    public JSONArray getData() throws JSONException {
        JSONObject jsonData;
        String strJson = shPref.getString("jsondata","0");
        if(strJson != null) jsonData = new JSONObject(strJson); else jsonData = new JSONObject("");
        return jsonData.getJSONArray("topics");
    }


    public String getId(int position){
        return  setData().get(position).getAsJsonObject().get("id").getAsString();
    }

    public String getContent(int position) {
        return setData().get(position).getAsJsonObject().get("content").getAsString();
    }

    public String getTopic(int position) {
        return setData().get(position).getAsJsonObject().get("topic").getAsString();
    }

    public boolean isOpened (int pos){
        return setData().get(pos).getAsJsonObject().get("open").getAsString().equals("true");
    }



    //GETTERS
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getIs_open() {
        return is_open;
    }

    //SETTERS
    public void setRes(JsonObject res) {
        this.res = res;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }
}
