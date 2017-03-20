package atlascience.bitmaptest.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Objects.Game;
import atlascience.bitmaptest.Objects.Question;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionToAnswerFragment extends DialogFragment {
    View v;
    TextView answer1_textView,answer2_textView,answer3_textView,answer4_textView;
    CardView question_card,answer1_card,answer2_card,answer3_card,answer4_card;
    String question,answer1,answer2,answer3,answer4,answer_true,question_id;;
    static int time;

    WebView question_content;

    static  int u_id;

    public QuestionToAnswerFragment(int user_id) {
        u_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_to_answer_dialog,container,false);

        getsetData();

        return v;


    }

    private void getsetData() {

        answer1_textView = (TextView) v.findViewById(R.id.answer1_dialog);
        answer2_textView = (TextView) v.findViewById(R.id.answer2_dialog);
        answer3_textView = (TextView) v.findViewById(R.id.answer3_dialog);
        answer4_textView = (TextView) v.findViewById(R.id.answer4_dialog);
        question_content = (WebView) v.findViewById(R.id.web_question_content);

        question_card = (CardView) v.findViewById(R.id.question_card);
        answer1_card = (CardView) v.findViewById(R.id.answer1_card);
        answer2_card = (CardView) v.findViewById(R.id.answer2_card);
        answer3_card = (CardView) v.findViewById(R.id.answer3_card);
        answer4_card = (CardView) v.findViewById(R.id.answer4_card);



        HashMap<String,String> data = Question.getQuestion();
        question = data.get(Question.KEY_QUESTION);
        answer1 = data.get(Question.KEY_ANSWER1);
        answer2 = data.get(Question.KEY_ANSWER2);
        answer3 = data.get(Question.KEY_ANSWER3);
        answer4 = data.get(Question.KEY_ANSWER4);
        answer_true = data.get(Question.KEY_ANSWER_TRUE);
        time = Integer.parseInt(data.get(Question.KEY_TIME));
        question_id = data.get(Question.KEY_QUESTION);

        answer1_textView.setText(answer1);
        answer2_textView.setText(answer2);
        answer3_textView.setText(answer3);
        answer4_textView.setText(answer4);

        if(answer_true.equals(card_listeners())){
            get_answer();
        }else{
            get_answer();
        }

//        question_content.setWebViewClient(new MyBrowser());
//        question_content.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        question_content.loadUrl(question);


    }

    private void get_answer() {
        HashMap<String,String> game_data = Game.getDetails();

        AppController.getApi().get_answer("get_answer",game_data.get(Game.KEY_GAME_ID),
                String.valueOf(question_id),String.valueOf(card_listeners()),"5",
                String.valueOf(u_id)).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Toast.makeText(getActivity().getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }


    private int card_listeners() {
        final int[] i = {0};
        answer1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = 1;
            }
        });
        answer2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] =2;
            }
        });
        answer3_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0]=3;
            }
        });
        answer4_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = 4;
            }
        });

        return i[0];
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
