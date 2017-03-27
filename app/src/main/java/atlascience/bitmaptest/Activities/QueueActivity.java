package atlascience.bitmaptest.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;

import java.util.HashMap;


import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Auth.SessionManager;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueActivity extends AppCompatActivity {

    private static final String TAG = QueueActivity.class.getSimpleName();
    Button find_player,random_player;
    SessionManager session;
    String username;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        session_init();

    }


    private void session_init() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String id =  user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_NAME);
        random_init(id);
        notif_reciever();
    }

    private void random_init(final String id) {
        random_player = (Button) findViewById(R.id.random_player);
        random_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server_go_to_game(id);
            }
        });
    }
    private void notif_reciever() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
    }


    public void server_go_to_game(String id){
        AppController.getApi().add_to_game("go_to_game",id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    if(!response.body().toString().equals("No one in queue")){
                        final ProgressDialog dialog = new ProgressDialog(QueueActivity.this);
                        dialog.setMessage("Creating a game..");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setCancelable(false);
                        dialog.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                dialog.dismiss();

                            }
                        }, 1000);
                    }

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(QueueActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("Please try again.");
                    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.create();
                    alert.show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
