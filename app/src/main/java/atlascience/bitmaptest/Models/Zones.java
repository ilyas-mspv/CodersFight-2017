package atlascience.bitmaptest.Models;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Zones {


    public static final String PREF_NAME = "captured_zone";
    public static final String CAPTURED = "captured";
    public static final String ZONE = "zone";
    public static final String USER_ID = "user_id";
    static SharedPreferences sharPref;
    static SharedPreferences.Editor editor;

    public Zones(Context context) {
        sharPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharPref.edit();
    }


    public static void create_zone_session(String tag, boolean captured, int zone, int user_id) {
        editor.putString("tag", tag);
        editor.putBoolean(CAPTURED, captured);
        editor.putInt(ZONE, zone);
        editor.putInt(USER_ID, user_id);
        editor.commit();
    }

    public static HashMap<String, String> get_zone_session() {
        HashMap<String, String> data = new HashMap<>();

        data.put(CAPTURED, String.valueOf(sharPref.getBoolean(CAPTURED, false)));
        data.put(ZONE, String.valueOf(sharPref.getInt(ZONE, 0)));
        data.put(USER_ID, String.valueOf(sharPref.getInt(USER_ID, 0)));
        data.put("tag", sharPref.getString("tag", null));

        return data;
    }

    public static void delete() {
        editor.clear();
        editor.commit();
    }
}
