package atlascience.bitmaptest.Models.TrainMode;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Response;

/**
 * Created by Ilyas on 10/9/2017.
 */

public class Answers {

    private JsonObject js;

    public static final String PREFERENCE_NAME  = "train_answers";
    public static final String CORRECT  = "correct_answers";
    public static final String INCORRECT  = "incorrect_answers";

    private  Context context;
    private SharedPreferences shPref;
    private SharedPreferences.Editor editor;

    public Answers(Response<JsonObject> response) {
        js = response.body();
    }

    public Answers(Response<JsonObject> response, Context context) {
        js  = response.body();
        this.context = context;
        shPref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = shPref.edit();
    }

    public Answers(Context context){
        this.context = context;
        shPref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = shPref.edit();
    }

    public HashMap<String,String> get_data(){
        HashMap<String,String> d = new HashMap<>();
        return d;
    }
    private int count = 0;
    public void set_correct(int i){
        count++;
        if(count>0){
            //second time
            editor.putInt(CORRECT,shPref.getInt(CORRECT,0) + 1);
        }else{
            //first times
            editor.putInt(CORRECT,1);
        }

        editor.commit();
    }

    private int count2 = 0;
    public void set_incorrect(int i){
        count2++;
        if(count>0){
            //second time
            editor.putInt(INCORRECT,shPref.getInt(INCORRECT,0) + 1);
        }else{
            //first times
            editor.putInt(INCORRECT,1);
        }

        editor.commit();
    }

    public JsonArray getData(){
        return js.get("answers").getAsJsonArray();
    }
    public int size(){
        return getData().size();
    }

    public int getOrder(int pos){
        return getData().get(pos).getAsJsonObject().get("order").getAsInt();
    }

    public String getAnswer(int pos){
        return getData().get(pos).getAsJsonObject().get("answer").getAsString();
    }




}
