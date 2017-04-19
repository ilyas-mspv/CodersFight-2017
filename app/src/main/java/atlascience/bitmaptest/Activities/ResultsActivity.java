package atlascience.bitmaptest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.HashMap;

import atlascience.bitmaptest.Adapters.ResultsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Queue.Queue;
import atlascience.bitmaptest.Models.Results;
import atlascience.bitmaptest.R;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ilyas on 05-Apr-17.
 */

public class ResultsActivity extends AppCompatActivity {

    TextView winner_state_result,zones_count1_results,zones_count2_results;
    RecyclerView  rv;
    Button new_game,return_to_profile;
    SweetAlertDialog d;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        d = new SweetAlertDialog(ResultsActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        d.setTitleText("Loading..");
        d.show();

        receive_data();
    }

    private void receive_data(){
        //UI
        winner_state_result = (TextView) findViewById(R.id.winner_state_result);
        rv = (RecyclerView) findViewById(R.id.rv_answers_results);
        zones_count1_results = (TextView) findViewById(R.id.zones_count1_results);
        zones_count2_results = (TextView) findViewById(R.id.zones_count2_results);
        new_game = (Button) findViewById(R.id.new_game_btn_results);
        return_to_profile = (Button) findViewById(R.id.return_profile_btn_results);


        //user
        final SessionManager session = new SessionManager(getApplicationContext());
        final HashMap<String,String> user_data = session.getUserDetails();
        final String name = user_data.get(SessionManager.KEY_NAME);
        final int my_id = Integer.parseInt(user_data.get(SessionManager.KEY_ID));

        //recycler
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());


        //game
        final Game game = new Game(getApplicationContext());
        final HashMap<String,String> game_data = Game.getDetails();
        int games_id = Integer.parseInt(game_data.get(Game.KEY_GAME_ID));

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, QueueActivity.class));
            }
        });
        return_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this,ProfileActivity.class));
            }
        });


        AppController.getApi().get_results("get_game_results",games_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Results results = new Results(response);
                if(my_id== results.getWinner()){
                    winner_state_result.setText("you win");
                }else{
                    winner_state_result.setText("you lose");
                }
                ResultsAdapter adapter = new ResultsAdapter(new Results(response),getApplicationContext());
                rv.setAdapter(adapter);
        }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        AppController.getApi().get_zones("get_zones",games_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                Log.e("TAG",res.toString());
                zones_count1_results.setText(String.valueOf(res.get("zones1").getAsInt()));
                zones_count2_results.setText(String.valueOf(res.get("zones2").getAsInt()));
                d.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}