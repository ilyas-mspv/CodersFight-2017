package atlascience.bitmaptest.Fragments;


import android.content.Intent;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.R;

public class QuestionToRateFragment extends DialogFragment {


    static int time;
    static int u_id;
    View v;
    TextView question_textView, answer1_textView, answer2_textView, answer3_textView, answer4_textView;
    ;
    CardView question_card,answer1_card,answer2_card,answer3_card,answer4_card;
    String question, answer1, answer2, answer3, answer4, answer_true, question_id;
    Button rate_minus,rate_plus;
    String url;
    ProgressBar progressBar;
    Timer timer;
    WebView question_content;
    boolean isPaused = false;
    double timer_user;
    int zone;

    public QuestionToRateFragment(int user_id) {
        u_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_to_rate_dialog,container,false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            zone = bundle.getInt("zone");
        }


        inits();
        set_data();

        return v;


    }

    private void inits() {
        question_textView = (TextView) v.findViewById(R.id.question_dialog);
        answer1_textView = (TextView) v.findViewById(R.id.answer1_dialog);
        answer2_textView = (TextView) v.findViewById(R.id.answer2_dialog);
        answer3_textView = (TextView) v.findViewById(R.id.answer3_dialog);
        answer4_textView = (TextView) v.findViewById(R.id.answer4_dialog);

        question_card = (CardView) v.findViewById(R.id.question_card);
        answer1_card = (CardView) v.findViewById(R.id.answer1_card);
        answer2_card = (CardView) v.findViewById(R.id.answer2_card);
        answer3_card = (CardView) v.findViewById(R.id.answer3_card);
        answer4_card = (CardView) v.findViewById(R.id.answer4_card);

        rate_plus = (Button) v.findViewById(R.id.plus_rating_button);
        rate_minus = (Button) v.findViewById(R.id.minus_rating_button);

        rate_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        rate_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.timer_progress_bar_rating);
        progressBar.setRotation(180);


    }

    public void set_data() {

        HashMap<String,String> data = Question.getQuestion();
        question = data.get(Question.KEY_QUESTION);
        answer1 = data.get(Question.KEY_ANSWER1);
        answer2 = data.get(Question.KEY_ANSWER2);
        answer3 = data.get(Question.KEY_ANSWER3);
        answer4 = data.get(Question.KEY_ANSWER4);
        answer_true = data.get(Question.KEY_ANSWER_TRUE);
        time = Integer.parseInt(data.get(Question.KEY_TIME));
        question_id = data.get(Question.KEY_QUESTION_ID);


        progressBar.setProgress(100);

        timer = new Timer(time * 1000, 500);
        timer.start();


        question_textView.setText(question);
        answer1_textView.setText(answer1);
        answer2_textView.setText(answer2);
        answer3_textView.setText(answer3);
        answer4_textView.setText(answer4);

        url = "http://codfight.atlascience.ru/code/" + question + ".html";
        question_content.setWebViewClient(new MyBrowser());
        question_content.loadUrl(url);




    }

    public void send_rating() {

        //todo complete sending rating to question database
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class Timer extends CountDownTimer {


        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timer_user = millisUntilFinished / 1000;
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
