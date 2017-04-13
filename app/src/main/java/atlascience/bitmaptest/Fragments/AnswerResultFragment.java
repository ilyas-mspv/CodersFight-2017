package atlascience.bitmaptest.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 01-Apr-17.
 */

public class AnswerResultFragment extends DialogFragment {

    View v;
    TextView text_username1, text_username2,
            text_answer_holder1, text_answer_holder2,
            text_time_holder1, text_time_holder2, is_correct1, is_correct2;

    int winner, answer1, answer2;
    double time1, time2;
    String username1, username2, correct1, correct2;
    int r_time;
    Button ok_result;

    public AnswerResultFragment(int winner, double time1, double time2, int answer1, int answer2, String correct1, String correct2) {
        this.winner = winner;
        this.time1 = time1;
        this.time2 = time2;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.correct1 = correct1;
        this.correct2 = correct2;
    }

    public AnswerResultFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_answer_results, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(true);

        initRes();
        setRes();

        return v;
    }


    private void initRes() {
        text_username1 = (TextView) v.findViewById(R.id.username1_result_fragment);
        text_username2 = (TextView) v.findViewById(R.id.username2_result_fragment);
        text_answer_holder1 = (TextView) v.findViewById(R.id.text_view_answer_holder1);
        text_answer_holder2 = (TextView) v.findViewById(R.id.text_view_answer_holder2);
        text_time_holder1 = (TextView) v.findViewById(R.id.text_view_time_holder1);
        text_time_holder2 = (TextView) v.findViewById(R.id.text_view_time_holder2);
        is_correct1 = (TextView) v.findViewById(R.id.is_correct1);
        is_correct2 = (TextView) v.findViewById(R.id.is_correct2);

        Game game = new Game(getActivity().getApplicationContext());
        HashMap<String, String> g_data = Game.getDetails();
        username1 = g_data.get(Game.KEY_USERNAME_1);
        username2 = g_data.get(Game.KEY_USERNAME_2);

        Question question = new Question(getActivity().getApplicationContext());
        HashMap<String, String> q_data = Question.getQuestion();
        r_time = Integer.parseInt(q_data.get(Question.KEY_TIME));

        ok_result = (Button) v.findViewById(R.id.ok_fragment_results);

    }

    private void setRes() {
        text_username1.setText(username1);
        text_username2.setText(username2);
        text_answer_holder1.setText(String.valueOf(answer1));
        text_answer_holder2.setText(String.valueOf(answer2));
        text_time_holder1.setText(String.format("%.3f", time1));
        text_time_holder2.setText(String.format("%.3f", time2));
        is_correct1.setText(correct1);
        is_correct2.setText(correct2);

        ok_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }


}
