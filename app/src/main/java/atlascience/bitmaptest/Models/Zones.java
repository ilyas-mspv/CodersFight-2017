package atlascience.bitmaptest.Models;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Zones {


    public static final String PREF_NAME = "round_results";
    public static final String KEY_WINNER = "winner";
    public static final String KEY_ZONE = "zone";
    public static final String KEY_TIME1 = "time1";
    public static final String KEY_TIME2 = "time2";
    public static final String KEY_ANSWER1 = "answer1";
    public static final String KEY_ANSWER2 = "answer2";
    public static final String KEY_MOVE_ID = "move_id";


    static SharedPreferences sharPref;
    static SharedPreferences.Editor editor;

    public Zones(Context context) {
        sharPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharPref.edit();
    }


    public static void create_result_session(int winner, int zone, double time1, double time2,
                                             int answer1, int answer2, String correct1, String correct2, boolean win1, boolean win2) {
        editor.putInt(KEY_WINNER, winner);
        editor.putInt(KEY_ZONE, zone);
        editor.putFloat(KEY_TIME1, (float) time1);
        editor.putFloat(KEY_TIME2, (float) time2);
        editor.putInt(KEY_ANSWER1, answer1);
        editor.putInt(KEY_ANSWER2, answer2);
        editor.putString("correct1", correct1);
        editor.putString("correct2", correct2);
        editor.putBoolean("win1", win1);
        editor.putBoolean("win2", win2);

        editor.commit();
    }

    public static HashMap<String, String> get_result_session() {
        HashMap<String, String> data = new HashMap<>();
        data.put(KEY_WINNER, String.valueOf(sharPref.getInt(KEY_WINNER, 0)));
        data.put(KEY_ZONE, String.valueOf(sharPref.getInt(KEY_ZONE, 0)));
        data.put(KEY_TIME1, String.valueOf(sharPref.getFloat(KEY_TIME1, 0)));
        data.put(KEY_TIME2, String.valueOf(sharPref.getFloat(KEY_TIME2, 0)));
        data.put(KEY_ANSWER1, String.valueOf(sharPref.getInt(KEY_ANSWER1, 0)));
        data.put(KEY_ANSWER2, String.valueOf(sharPref.getInt(KEY_ANSWER2, 0)));
        data.put(KEY_MOVE_ID, String.valueOf(sharPref.getInt(KEY_MOVE_ID, 0)));
        data.put("correct1", sharPref.getString("correct1", null));
        data.put("correct2", sharPref.getString("correct2", null));
        data.put("win1", String.valueOf(sharPref.getBoolean("win1", false)));
        data.put("win2", String.valueOf(sharPref.getBoolean("win2", false)));
        return data;
    }

    public static void delete() {
        editor.clear();
        editor.commit();
    }

    public static void success_session(String tag, boolean is_success) {
        editor.putString("tag", tag);
        editor.putBoolean("success", is_success);
        editor.commit();
    }

    public static HashMap<String, String> get_success() {
        HashMap<String, String> s_data = new HashMap<>();
        s_data.put("tag", String.valueOf(sharPref.getString("tag", null)));
        s_data.put("success", String.valueOf(sharPref.getBoolean("success", false)));
        return s_data;
    }

    public static void create_zones(int zones1,int zones2){
        editor.putInt("zones1",zones1);
        editor.putInt("zones2",zones2);
        editor.commit();
    }

    public static  HashMap<String,String> get_zones_result(){
        HashMap<String,String> data = new HashMap<>();
        data.put("zones1", String.valueOf(sharPref.getInt("zones1",0)));
        data.put("zones2", String.valueOf(sharPref.getInt("zones2",0)));

        return data;
    }
}

