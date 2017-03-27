package atlascience.bitmaptest.Models;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import atlascience.bitmaptest.Activities.MainActivity;
import atlascience.bitmaptest.Activities.ProfileActivity;

public class Game {
    public static final String PREF_NAME = "game_round";
    public static final String KEY_GAME_ID = "game_id";
    public static final String KEY_USER_ID1 = "user_id1";
    public static final String KEY_USER_ID2 = "user_id2";
    public static final String KEY_FIRST = "first_player";
    public static final String KEY_START_ZONE_1 = "start_zone1";
    public static final String KEY_START_ZONE_2 = "start_zone2";
    public static final String KEY_USERNAME_1 = "username_id1";
    public static final String KEY_USERNAME_2 = "username_id2";
    public static Context context;
    public static ArrayList<Integer> zones;
    static SharedPreferences sharPref;
    static SharedPreferences.Editor editor;
    JsonObject jsonObject;

    public Game(Context context) {
        sharPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharPref.edit();
        this.context = context;
        zones=new ArrayList<Integer>();
    }




    public static HashMap<String, String> getDetails(){
        HashMap<String,String> data = new HashMap<>();
        data.put(KEY_GAME_ID, String.valueOf(sharPref.getInt(KEY_GAME_ID,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USER_ID1, String.valueOf(sharPref.getInt(KEY_USER_ID1, Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USER_ID2, String.valueOf(sharPref.getInt(KEY_USER_ID2, Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USERNAME_1, sharPref.getString(KEY_USERNAME_1,""));
        data.put(KEY_USERNAME_2, sharPref.getString(KEY_USERNAME_2,""));
        data.put(KEY_FIRST,String.valueOf(sharPref.getInt(KEY_FIRST,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_START_ZONE_1, String.valueOf(sharPref.getInt(KEY_START_ZONE_1,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_START_ZONE_2, String.valueOf(sharPref.getInt(KEY_START_ZONE_2,Integer.parseInt(String.valueOf(0)))));
        data.put("zone1", String.valueOf(sharPref.getInt("zone1",0)));
        data.put("zone2", String.valueOf(sharPref.getInt("zone2",0)));



        return data;
    }
    public static  void create_game(int game_id, int user_id1, int user_id2,
                                    int first, int start_zone1,
                                    int start_zone2, final String username1, final String username2,int zone1,int zone2){
        editor.putInt(KEY_GAME_ID,game_id);
        editor.putInt(KEY_USER_ID1,user_id1);
        editor.putInt(KEY_USER_ID2,user_id2);
        editor.putInt(KEY_FIRST,first);
        editor.putInt(KEY_START_ZONE_1,start_zone1);
        editor.putInt(KEY_START_ZONE_2,start_zone2);

        editor.putString(KEY_USERNAME_1,username1);
        editor.putString(KEY_USERNAME_2,username2);

        editor.putInt("zone1",zone1);
        editor.putInt("zone2",zone2);

        zones.add(zone1);
        zones.add(zone2);

        editor.commit();
    }

    public static  void delete_game(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    public void go_game(){
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
