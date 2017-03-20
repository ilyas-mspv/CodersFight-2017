package atlascience.bitmaptest.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import atlascience.bitmaptest.Objects.Question;
import atlascience.bitmaptest.R;

public class QuestionToRateFragment extends DialogFragment {


    View v;
    TextView question_textView,answer1_textView,answer2_textView,answer3_textView,answer4_textView;
    CardView question_card,answer1_card,answer2_card,answer3_card,answer4_card;
    String question,answer1,answer2,answer3,answer4;
    Button rate_minus,rate_plus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_question_to_rate_dialog,container,false);

        getsetData();

        return v;


    }

    private void getsetData() {
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

        rate_plus = (Button) v.findViewById(R.id.rate_plus);
        rate_minus = (Button) v.findViewById(R.id.rate_minus);

        card_listeners();

        HashMap<String,String> data = Question.getQuestion();
        question = data.get(Question.KEY_QUESTION);
        answer1 = data.get(Question.KEY_ANSWER1);
        answer2 = data.get(Question.KEY_ANSWER2);
        answer3 = data.get(Question.KEY_ANSWER3);
        answer4 = data.get(Question.KEY_ANSWER4);

        question_textView.setText(question);
        answer1_textView.setText(answer1);
        answer2_textView.setText(answer2);
        answer3_textView.setText(answer3);
        answer4_textView.setText(answer4);

    }

    private void card_listeners() {
        answer1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        answer2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        answer3_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        answer4_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });

        rate_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"plus one to question rate",Toast.LENGTH_SHORT).show();
            }
        });

        rate_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"minus one to question rate",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
