package atlascience.bitmaptest.Activities;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Auth.SessionManager;
import atlascience.bitmaptest.Objects.Game;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import atlascience.bitmaptest.Services.MyFirebaseInstanceIDService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    SessionManager session;
    Button logout,play_button,rating;
    TextView username;
    ProgressDialog dialog;
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        get_data();
        init_buttons();

}


    private void init_buttons() {

        logout = (Button) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });


    }
    private void get_data() {
        session = new SessionManager(getApplicationContext());
        username = (TextView) findViewById(R.id.username_profile);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        final String id = user.get(SessionManager.KEY_ID);
        String name = user.get(SessionManager.KEY_NAME);


            String token = FirebaseInstanceId.getInstance().getToken();
            set_token(id,token);
            session.create_token(token);

            play_button = (Button) findViewById(R.id.game_play);
            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    server_add_to_queue(id);
                    Intent i = new Intent(ProfileActivity.this,QueueActivity.class);
                    startActivity(i);

                }
            });

        username.setText(name);


    }

    public void server_add_to_queue(final String id){
        AppController.getApi().addtoQueue("add_to_queue",id)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String res = response.body().toString();
                        if(res.contains("You were added to queue")){
                            Toast.makeText(getApplicationContext(),"You were added to queue. Wait please for play",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    public void set_token(String id,String token){

        if(session.get_token().equals(token)){
            Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
        }else{
            AppController.getApi().set_token("set_token",id,token).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }
}
