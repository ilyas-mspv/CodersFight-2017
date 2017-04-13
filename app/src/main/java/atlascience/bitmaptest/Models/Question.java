package atlascience.bitmaptest.Models;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Question {

    public static final String PREF_NAME = "question_round";
    public static final String KEY_TIME = "time";
    public static final String KEY_QUESTION_ID = "question_id";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER1 = "answer1";
    public static final String KEY_ANSWER2 = "answer2";
    public static final String KEY_ANSWER3 = "answer3";
    public static final String KEY_ANSWER4 = "answer4";
    public static final String KEY_ANSWER_TRUE = "answer_true";
    public static final String KEY_USER_ID = "user_id";
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    String question;
    int time;
    String answer1, answer2, answer3, answer4;

    public Question(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public static void create_question(String tag, int user_id, int question_id, String question, String answer1,
                                       String answer2, String answer3, String answer4, int answer_true, int time, int zone, int game_round_id, String question_type) {
        editor.putString("tag", tag);
        editor.putInt(KEY_USER_ID, user_id);
        editor.putInt(KEY_QUESTION_ID,question_id);
        editor.putString(KEY_QUESTION,question);
        editor.putString(KEY_ANSWER1,answer1);
        editor.putString(KEY_ANSWER2,answer2);
        editor.putString(KEY_ANSWER3,answer3);
        editor.putString(KEY_ANSWER4,answer4);
        editor.putInt(KEY_ANSWER_TRUE, answer_true);
        editor.putInt(KEY_TIME, time);
        editor.putInt("zone", zone);
        editor.putInt("game_round_id", game_round_id);
        editor.putString("question_type", question_type);

        editor.commit();
    }

    public static HashMap<String,String> getQuestion(){
        HashMap<String ,String> data = new HashMap<>();
        data.put(KEY_QUESTION_ID, String.valueOf(sharedPreferences.getInt(KEY_QUESTION_ID, 0)));
        data.put(KEY_QUESTION,sharedPreferences.getString(KEY_QUESTION,""));
        data.put(KEY_ANSWER1,sharedPreferences.getString(KEY_ANSWER1,""));
        data.put(KEY_ANSWER2,sharedPreferences.getString(KEY_ANSWER2,""));
        data.put(KEY_ANSWER3,sharedPreferences.getString(KEY_ANSWER3,""));
        data.put(KEY_ANSWER4,sharedPreferences.getString(KEY_ANSWER4,""));
        data.put(KEY_ANSWER_TRUE, String.valueOf(sharedPreferences.getInt(KEY_ANSWER_TRUE,0)));
        data.put(KEY_TIME, String.valueOf(sharedPreferences.getInt(KEY_TIME,0)));
        data.put("zone", String.valueOf(sharedPreferences.getInt("zone", 0)));
        data.put("game_round_id", String.valueOf(sharedPreferences.getInt("game_round_id", 0)));
        data.put("question_type", sharedPreferences.getString("question_type", null));
        return data;
    }

    public static void delete() {
        editor.clear();
        editor.commit();
    }
}
