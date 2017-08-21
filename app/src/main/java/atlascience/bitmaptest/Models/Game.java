package atlascience.bitmaptest.Models;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

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
    public static final String KEY_USER2_PHOTO = "user2_photo";
    public static final String KEY_USER1_PHOTO = "user1_photo";
    public static Context context;
    public static ArrayList<Integer> zones;
    static SharedPreferences sharPref;
    static SharedPreferences.Editor editor;


    public Game(Context context) {
        sharPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharPref.edit();
        Game.context = context;
        zones=new ArrayList<Integer>();
    }




    public static HashMap<String, String> getDetails(){
        HashMap<String,String> data = new HashMap<>();
        data.put(KEY_GAME_ID, String.valueOf(sharPref.getInt(KEY_GAME_ID,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USER_ID1, String.valueOf(sharPref.getInt(KEY_USER_ID1, Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USER_ID2, String.valueOf(sharPref.getInt(KEY_USER_ID2, Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_USERNAME_1, sharPref.getString(KEY_USERNAME_1,""));
        data.put(KEY_USERNAME_2, sharPref.getString(KEY_USERNAME_2,""));
        data.put(KEY_USER2_PHOTO, sharPref.getString(KEY_USER2_PHOTO,""));
        data.put(KEY_FIRST,String.valueOf(sharPref.getInt(KEY_FIRST,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_START_ZONE_1, String.valueOf(sharPref.getInt(KEY_START_ZONE_1,Integer.parseInt(String.valueOf(0)))));
        data.put(KEY_START_ZONE_2, String.valueOf(sharPref.getInt(KEY_START_ZONE_2,Integer.parseInt(String.valueOf(0)))));
        data.put("zone1", String.valueOf(sharPref.getInt("zone1",0)));
        data.put("zone2", String.valueOf(sharPref.getInt("zone2",0)));

        return data;
    }
    public static  void create_game(int game_id, int user_id1, int user_id2,
                                    int first, int start_zone1,
                                    int start_zone2, final String username1,
                                    final String username2,int zone1,int zone2,
                                    String user2_photo,String user1_photo){
        editor.putInt(KEY_GAME_ID,game_id);
        editor.putInt(KEY_USER_ID1,user_id1);
        editor.putInt(KEY_USER_ID2,user_id2);
        editor.putInt(KEY_FIRST,first);
        editor.putInt(KEY_START_ZONE_1,start_zone1);
        editor.putInt(KEY_START_ZONE_2,start_zone2);

        editor.putString(KEY_USERNAME_1,username1);
        editor.putString(KEY_USERNAME_2,username2);
        editor.putString(KEY_USER2_PHOTO,user2_photo);
        editor.putString(KEY_USER1_PHOTO,user1_photo);

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

    public static void set_request(int user_id, String username) {
        editor.putInt("user_id", user_id);
        editor.putString("username", username);
        editor.commit();
    }

    public static HashMap<String, String> get_request() {
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", String.valueOf(sharPref.getInt("user_id", 0)));
        data.put("username", sharPref.getString("username", null));

        return data;
    }

    public static void set_approval(int user_id, String username, String is_true) {
        editor.putInt("user_id", user_id);
        editor.putString("username", username);
        editor.putString("is_true", is_true);
        editor.commit();
    }

    public static HashMap<String, String> get_approval() {
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", String.valueOf(sharPref.getInt("user_id", 0)));
        data.put("username", sharPref.getString("username", null));
        data.put("is_true", sharPref.getString("is_true", null));

        return data;
    }

    public static void delete_request() {
        editor.clear();
        editor.commit();
    }


    public static void setMapAreas(int zone, int user_id) {
        int[] areas = new int[5];
        areas[0] = 0;
        areas[1] = 0;
        areas[2] = 0;
        areas[3] = 0;
        areas[4] = 0;
        areas[zone - 1] = user_id;

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < areas.length; i++) {
            str.append(areas[i]).append(",");
        }
        editor.putString("areas", str.toString());
        editor.commit();
    }

    public static boolean areAreasFull() {
        HashMap<String, String> data = new HashMap<>();
        String savedString = data.put("areas", sharPref.getString("areas", null));
        StringTokenizer st = new StringTokenizer(savedString, ",");
        int[] areas = new int[4];
        for (int i = 0; i < 4; i++) {
            areas[i] = Integer.parseInt(st.nextToken());
        }
        boolean is_full = false;
        for (int i = 0; i < areas.length; i++) {
            if (areas[i] != 0) is_full = true;
        }
        return is_full;
    }

}
