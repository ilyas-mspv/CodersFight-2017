package atlascience.bitmaptest.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticsActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rating_stats) TextView rating_stats;
    @BindView(R.id.played_times_stats) TextView played_times_stats;
    @BindView(R.id.winner_times_stats) TextView winner_times_stats;
    @BindView(R.id.answered_questions_stats) TextView answered_questions_stats;
    @BindView(R.id.correct_answers_stats) TextView correct_answers_stats;
    @BindView(R.id.error_msg) TextView error_msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String,String> data = sessionManager.getUserDetails();

        showProgress();

        AppController.getApi().get_stats_data(Constants.Methods.Version.VERSION,Constants.Methods.Content.GET_STATISTICS, Integer.parseInt(data.get(SessionManager.KEY_ID))).enqueue(new Callback<JsonObject>() {
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