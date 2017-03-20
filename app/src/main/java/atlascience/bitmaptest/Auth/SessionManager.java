package atlascience.bitmaptest.Auth;

import java.net.InetAddress;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import atlascience.bitmaptest.Auth.LoginActivity;

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "User";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "token";

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
           if(ipAddr.equals("")){
               return false;
           }else{
               return true;
           }

        } catch (Exception e) {
            return false;
        }

    }

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(int id,String name, String email){
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt("id",id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
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