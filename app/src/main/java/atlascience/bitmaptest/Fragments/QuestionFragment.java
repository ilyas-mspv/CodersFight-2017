package atlascience.bitmaptest.Fragments;


import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends DialogFragment {
    static int time;
    static int u_id;
    static int move_user_id;
    int r_time;
    View v;
    TextView answer1_textView, answer2_textView, answer3_textView, answer4_textView;
    CardView question_card,answer1_card,answer2_card,answer3_card,answer4_card;
    String question, answer1, answer2, answer3, answer4, answer_true, question_id, zone_question, game_round_id;
    String url;
    ProgressBar progressBar;
    WebView question_content;
    MyCountdownTimer timer;

    int timer_user;
    int zone;

    public QuestionFragment(int user_id) {
        u_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_to_answer_dialog,container,false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);

        inits();
        getsetData();

        return v;

    }

    private void inits() {

        answer1_textView = (TextView) v.findViewById(R.id.answer1_dialog);
        answer2_textView = (TextView) v.findViewById(R.id.answer2_dialog);
        answer3_textView = (TextView) v.findViewById(R.id.answer3_dialog);
        answer4_textView = (TextView) v.findViewById(R.id.answer4_dialog);
        question_content = (WebView) v.findViewById(R.id.web_question_content);

        answer1_card = (CardView) v.findViewById(R.id.answer1_card);
        answer2_card = (CardView) v.findViewById(R.id.answer2_card);
        answer3_card = (CardView) v.findViewById(R.id.answer3_card);
        answer4_card = (CardView) v.findViewById(R.id.answer4_card);

        progressBar = (ProgressBar) v.findViewById(R.id.timer_progress_bar);
        progressBar.setRotation(180);
    }


    private void getsetData() {

        HashMap<String,String> data = Question.getQuestion();
        question = data.get(Question.KEY_QUESTION);
        answer1 = data.get(Question.KEY_ANSWER1);
        answer2 = data.get(Question.KEY_ANSWER2);
        answer3 = data.get(Question.KEY_ANSWER3);
        answer4 = data.get(Question.KEY_ANSWER4);
        answer_true = data.get(Question.KEY_ANSWER_TRUE);
        time = Integer.parseInt(data.get(Question.KEY_TIME));
        r_time = Integer.parseInt(data.get(Question.KEY_TIME));
        question_id = data.get(Question.KEY_QUESTION_ID);
        zone_question = data.get("zone");
        game_round_id = data.get("game_round_id");

        progressBar.setProgress(100);

        timer = new MyCountdownTimer(time * 1000, 500);
        timer.start();

        answer1_textView.setText(answer1);
        answer2_textView.setText(answer2);
        answer3_textView.setText(answer3);
        answer4_textView.setText(answer4);


        url = Constants.URLS.QUESTION_URL + question + ".html";
        question_content.setWebViewClient(new MyBrowser());
        question_content.loadUrl(url);

        card_listeners();
    }

    private void set_answer(int answer) {

        zone = Integer.valueOf(zone_question);
        Game game = new Game(getActivity().getApplicationContext());
        HashMap<String,String> game_data = Game.getDetails();
        String game_id = game_data.get(Game.KEY_GAME_ID);
        String user_id1 = game_data.get(Game.KEY_USER_ID1);
        String user_id2 = game_data.get(Game.KEY_USER_ID2);
        String username1 = game_data.get(Game.KEY_USERNAME_1);
        String username2 = game_data.get(Game.KEY_USERNAME_2);
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user_data = session.getUserDetails();
        String name = user_data.get(SessionManager.KEY_NAME);
        if (name.equals(username1)) {
            AppController.getApi().set_answer1("set_answer_one",
                    Integer.parseInt(game_round_id),
                    Integer.parseInt(user_id1),
                    (r_time * 1000) - timer_user,
                    answer
            ).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
        } else {
            AppController.getApi().set_answer2("set_answer_two",
                    Integer.parseInt(game_round_id),
                    Integer.parseInt(user_id2),
                    (r_time * 1000) - timer_user,
                    answer
            ).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
        }
    }


    private int card_listeners() {


        final int[] i = {0};
        answer1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = 1;
                set_answer(i[0]);
                timerPause();
            }
        });
        answer2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] =2;
                set_answer(i[0]);
                timerPause();
            }
        });
        answer3_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = 3;
                set_answer(i[0]);
                timerPause();
            }
        });
        answer4_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = 4;
                set_answer(i[0]);
                timerPause();
            }
        });

        return i[0];
    }

    public void timerPause() {
        timer.cancel();
        getDialog().dismiss();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class MyCountdownTimer extends CountDownTimer {


        public MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timer_user = (int) millisUntilFinished;
            long timer_time = millisUntilFinished / 100;
            progressBar.setProgress((int) timer_time);

        }

        @Override
        public void onFinish() {
            progressBar.setProgress(0);
            getDialog().dismiss();
        }
    }


}
