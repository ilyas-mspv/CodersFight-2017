package atlascience.bitmaptest.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.HashMap;

import atlascience.bitmaptest.Adapters.ResultsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game.Game;
import atlascience.bitmaptest.Models.Game.Question;
import atlascience.bitmaptest.Models.Game.Zones;
import atlascience.bitmaptest.Models.Results;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ilyas on 05-Apr-17.
 */

public class ResultsActivity extends BaseAppCompatActivity {

    TextView winner_state_result,zones_count1_results,zones_count2_results;
    RecyclerView  rv;
    Button new_game,return_to_profile;

    String zones1,zones2;
    CardView card_view_results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


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
        card_view_results = (CardView) findViewById(R.id.card_view_results);

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
        Zones zones = new Zones(getApplicationContext());
        HashMap<String,String> zones_data = Zones.get_zones_result();
        zones1 = zones_data.get("zones1");
        zones2 = zones_data.get("zones2");

        zones_count1_results.setText(zones1);
        zones_count2_results.setText(zones2);

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game game = new Game(getApplicationContext());
                Question question = new Question(getApplicationContext());
                Zones zones = new Zones(getApplicationContext());
                Zones.delete();
                Game.delete_game();
                Question.delete();
                showProgress(getResources().getString(R.string.dialog_load_type));
                AppController.getApi().addtoQueue(Constants.Methods.Version.VERSION,Constants.Methods.Game.Queue.ADD, String.valueOf(my_id)).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        dismissProgress();
                        setErrorAlert(t.getMessage());
                    }
                });
                finish();
                startActivity(new Intent(ResultsActivity.this, QueueActivity.class));
            }
        });
        return_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game game = new Game(getApplicationContext());
                Question question = new Question(getApplicationContext());
                Zones zones = new Zones(getApplicationContext());
                finish();
                Zones.delete();
                Game.delete_game();
                Question.delete();
                startActivity(new Intent(ResultsActivity.this,ProfileActivity.class));
            }
        });
        showProgress(getResources().getString(R.string.dialog_load_type));
        AppController.getApi().get_results(Constants.Methods.Version.VERSION,Constants.Methods.Game.GET_RESULTS,games_id,Integer.parseInt(zones1),Integer.parseInt(zones2)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Results results = new Results(response);
                if(my_id == results.getWinner()) {
                    winner_state_result.setText(getResources().getString(R.string.text_winner));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        card_view_results.setBackground(getResources().getDrawable(R.drawable.results_background_green));
                    }
                }else{
                    winner_state_result.setText(getResources().getString(R.string.text_loser));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        card_view_results.setBackground(getResources().getDrawable(R.drawable.results_background_red));
                    }
                }
                ResultsAdapter adapter = new ResultsAdapter(new Results(response),getApplicationContext());
                rv.setAdapter(adapter);
                dismissProgress();
        }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgress();
                setErrorAlert(t.getMessage());
            }
        });

    }
}