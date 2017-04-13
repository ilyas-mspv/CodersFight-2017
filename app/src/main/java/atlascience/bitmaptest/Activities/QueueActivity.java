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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.Adapters.QueueAdapter;
import atlascience.bitmaptest.Adapters.RequestAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Queue.Queue;
import atlascience.bitmaptest.Models.Queue.QueueResponse;
import atlascience.bitmaptest.Models.RequestPlayer;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import atlascience.bitmaptest.Utils.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueActivity extends AppCompatActivity {

    private static final String TAG = QueueActivity.class.getSimpleName();
    Button find_player,random_player;
    SessionManager session;
    String username1;
    List<Queue> queueList;
    ArrayList<RequestPlayer> requestPlayers = new ArrayList<RequestPlayer>();
    RequestAdapter Req_adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv, recyclerview;
    QueueAdapter adapter;
    AlertDialog.Builder ad;
    HashMap<String, String> user;
    int user_id;
    String username;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initQueuePlayers();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getApi().delete_from_queue("delete_from_queue", Integer.parseInt(user.get(SessionManager.KEY_ID))).enqueue(new Callback<QueueResponse>() {
                    @Override
                    public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<QueueResponse> call, Throwable t) {

                    }
                });

                finish();
            }
        });

    }

    private void setRequestContent(int user_id, String username) {


        recyclerview = (RecyclerView) findViewById(R.id.rv_request);
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);

        requestPlayers.add(new RequestPlayer(user_id, username));
        Req_adapter = new RequestAdapter(requestPlayers);
        recyclerview.setAdapter(Req_adapter);
        Req_adapter.notifyDataSetChanged();
    }


    public void initQueuePlayers() {
        SessionManager session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

        rv = (RecyclerView) findViewById(R.id.queue_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                final boolean[] isfirsttouch = {true};
                if (isfirsttouch[0]) {
                    int another_id = queueList.get(position).getUser_id();
                    int id = Integer.valueOf(user.get(SessionManager.KEY_ID));
                    if (id != another_id) {
                        AppController.getApi().request_game("request_game",
                                id, another_id).enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                if (queueList.get(position).getUser_id() == user_id) {
                                    isfirsttouch[0] = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(QueueActivity.this, "You can't play with yourself", Toast.LENGTH_SHORT).show();
                    }
                    isfirsttouch[0] = false;
                } else {
                    Toast.makeText(QueueActivity.this, "You have already chosen this player", Toast.LENGTH_SHORT).show();
                }

            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_queue);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        session_init();

        get_all_from_queue();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_all_from_queue_refresh();
            }
        });
    }

    private void get_all_from_queue_refresh() {
        AppController.getApi().get_all_from_queue("get_all_from_queue").enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                rv.removeAllViews();
                queueList = response.body().getResults();
                adapter = new QueueAdapter(queueList, getApplicationContext());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {

            }
        });
    }

    public void get_all_from_queue() {
        AppController.getApi().get_all_from_queue("get_all_from_queue").enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {
                queueList = response.body().getResults();

                adapter = new QueueAdapter(queueList, getApplicationContext());

                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {

            }
        });
    }

    private void session_init() {
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();

        String id =  user.get(SessionManager.KEY_ID);
        username1 = user.get(SessionManager.KEY_NAME);
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
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {

                    switch (intent.getStringExtra("tag")) {
                        case "game_request":
                            user_id = intent.getIntExtra("user_id", 0);
                            username = intent.getStringExtra("username");
                            Game game = new Game(getApplicationContext());
                            Game.set_request(user_id, username);
                            HashMap<String, String> g_data = Game.get_request();
                            user_id = Integer.parseInt(g_data.get("user_id"));
                            username = g_data.get("username");
                            setRequestContent(user_id, username);
                            break;
                        case "approval_request":
                            int apr_id = intent.getIntExtra("apr_id", 0);
                            String nickname = intent.getStringExtra("username");
                            String is_approved = intent.getStringExtra("is_true");
                            Game g = new Game(getApplicationContext());
                            Game.set_approval(apr_id, nickname, is_approved);
                            HashMap<String, String> g_d = Game.get_approval();
                            apr_id = Integer.parseInt(g_d.get("user_id"));
                            nickname = g_d.get("username");
                            is_approved = g_d.get("is_true");
                            if (is_approved.equals("yes")) {
                                set_alert_positive(apr_id, nickname);
                            } else {
                                set_alert_negative(apr_id, nickname);
                            }
                            break;
                    }
                }
            }
        };
    }

    private void set_alert_negative(int apr_id, String nickname) {
        ad = new AlertDialog.Builder(getApplicationContext());
        ad.setMessage(nickname + " don't want to play");
        ad.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "good.", Toast.LENGTH_SHORT).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "not cool twice", Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();
    }

    private void set_alert_positive(final int apr_id, String nickname) {
        ad = new AlertDialog.Builder(QueueActivity.this);
        ad.setMessage(nickname + " wants to play with you. Will you start?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AppController.getApi().invite_to_game("invite_to_game", Integer.parseInt(user.get(SessionManager.KEY_ID)), apr_id).enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "not cool", Toast.LENGTH_SHORT).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "not cool twice", Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();
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
                    } else {
                        Toast.makeText(QueueActivity.this, "No one in queue", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppController.getApi().delete_from_queue("delete_from_queue", Integer.parseInt(user.get(SessionManager.KEY_ID))).enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {

            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AppController.getApi().delete_from_queue("delete_from_queue", Integer.parseInt(user.get(SessionManager.KEY_ID))).enqueue(new Callback<QueueResponse>() {
            @Override
            public void onResponse(Call<QueueResponse> call, Response<QueueResponse> response) {

            }

            @Override
            public void onFailure(Call<QueueResponse> call, Throwable t) {

            }
        });

    }
}
