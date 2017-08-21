package atlascience.bitmaptest.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticsActivity extends BaseAppCompatActivity {

    TextView rating_stats, played_times_stats,winner_times_stats, answered_questions_stats, correct_answers_stats,error_msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        rating_stats  =(TextView) findViewById(R.id.rating_stats);
        played_times_stats  =(TextView) findViewById(R.id.played_times_stats);
        winner_times_stats  =(TextView) findViewById(R.id.winner_times_stats);
        answered_questions_stats  =(TextView) findViewById(R.id.answered_questions_stats);
        correct_answers_stats  =(TextView) findViewById(R.id.correct_answers_stats);
        error_msg = (TextView) findViewById(R.id.error_msg);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String,String> data = sessionManager.getUserDetails();

        showProgress(getResources().getString(R.string.dialog_load_type));

        AppController.getApi().get_stats_data("get_all_stats", Integer.parseInt(data.get(SessionManager.KEY_ID))).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                int success = res.get("success").getAsInt();
                if(success==1){
                    rating_stats.setVisibility(View.VISIBLE);
                    played_times_stats.setVisibility(View.VISIBLE);
                    winner_times_stats.setVisibility(View.VISIBLE);
                    answered_questions_stats.setVisibility(View.VISIBLE);
                    correct_answers_stats.setVisibility(View.VISIBLE);

                    rating_stats.setText(getResources().getString(R.string.stats_text_rating) +" "+ String.valueOf(res.get("rating").getAsInt()));
                    played_times_stats.setText(getResources().getString(R.string.stats_text_played_times) +" "+ String.valueOf(res.get("played_times").getAsInt()));
                    winner_times_stats.setText(getResources().getString(R.string.stats_text_winner_times)+" "+String.valueOf(res.get("winner_times").getAsInt()));
                    answered_questions_stats.setText(getResources().getString(R.string.stats_text_answered_questions)+" "+String.valueOf(res.get("answered_questions").getAsInt()));
                    correct_answers_stats.setText(getResources().getString(R.string.stats_text_correct_answers)+" "+String.valueOf(res.get("correct_answers").getAsInt()));
                    dismissProgress();
                }else{
                    error_msg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgress();
                setErrorAlert(t.getMessage());
            }
        });

    }
}