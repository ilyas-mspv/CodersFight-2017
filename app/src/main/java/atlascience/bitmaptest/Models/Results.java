package atlascience.bitmaptest.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Response;

/**
 * Created by Ilyas on 17-Apr-17.
 */

public class Results {

    JsonObject res;

    public Results(Response<JsonObject> response) {
      res = response.body().getAsJsonObject().get("result").getAsJsonObject();
    }

    public int getWinner() {
        return res.get("winner").getAsInt();
    }

    public JsonArray getData() {
        JsonArray jsonArray = res.get("data").getAsJsonArray();
        return jsonArray;
    }

    public int get_size(){
        return getData().size();
    }

    public String get_question(int position) {
        return getData().get(position).getAsJsonObject().get("question").getAsString();
    }

    public int get_answer_true(int pos){
        return getData().get(pos).getAsJsonObject().get("answer_true").getAsInt();
    }

    public String get_question_answer1(int pos){
        return  getData().get(pos).getAsJsonObject().get("question_answer1").getAsString();
    }

    public String get_question_answer2(int pos){
        return  getData().get(pos).getAsJsonObject().get("question_answer2").getAsString();
    }

    public String get_question_answer3(int pos){
        return  getData().get(pos).getAsJsonObject().get("question_answer3").getAsString();
    }

    public String get_question_answer4(int pos){
        return  getData().get(pos).getAsJsonObject().get("question_answer4").getAsString();
    }

    public double get_time1(int pos){
        return getData().get(pos).getAsJsonObject().get("time1").getAsDouble();
    }

    public double get_time2(int pos){
        return getData().get(pos).getAsJsonObject().get("time2").getAsDouble();
    }

    public int get_user_answer1(int pos){
        return  getData().get(pos).getAsJsonObject().get("user_answer1").getAsInt();
    }

    public int get_user_answer2(int pos){
        return  getData().get(pos).getAsJsonObject().get("user_answer2").getAsInt();
    }

}
