package atlascience.bitmaptest.Authenticator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_NAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_IMAGE_URL = "url";
    public static final String KEY_ACCOUNT_STATUS = "account_status";
    private static final String PREF_NAME = "User";
    private static final String IS_LOGIN = "IsLoggedIn";
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(int id,String name, String email,String url,int status){
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID,id);
        editor.putInt(KEY_ACCOUNT_STATUS,status);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_IMAGE_URL,url);
        editor.commit();
    }


    public void create_token(String token){
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    public void checkLogin(){

        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<>();

        user.put(KEY_ID, String.valueOf(pref.getInt(KEY_ID, Integer.parseInt(String.valueOf(0)))));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_IMAGE_URL, pref.getString(KEY_IMAGE_URL,null));
        user.put(KEY_ACCOUNT_STATUS,String.valueOf(pref.getInt(KEY_ACCOUNT_STATUS, Integer.parseInt(String.valueOf(0)))));
        return user;
    }

    public HashMap<String,String> get_token(){
        HashMap<String,String> token = new HashMap<>();
        token.put(KEY_TOKEN,pref.getString(KEY_TOKEN,null));
        return token;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}