package atlascience.bitmaptest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.HashMap;

import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game.Game;
import atlascience.bitmaptest.Models.Results;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 17-Apr-17.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {


    Results resultsList;
    Context context;
    Game game;
    SessionManager session;
    HashMap<String,String> data_game;
    HashMap<String,String> data_user;
    int user_id2;
    int user_id1;
    int user_id;

    public ResultsAdapter(Results resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;

        game = new Game(context);
        session = new SessionManager(context);
        data_game = Game.getDetails();
        data_user = session.getUserDetails();
        user_id1 = Integer.parseInt(data_game.get(Game.KEY_USER_ID1));
        user_id2 = Integer.parseInt(data_game.get(Game.KEY_USER_ID2));
        user_id = Integer.parseInt(data_user.get(SessionManager.KEY_ID));

    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_question_answer_results, parent, false);

        return new ResultsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {


        TextView[] answersTV = new TextView[] {
                holder.answer1_results,
                holder.answer2_results,
                holder.answer3_results,
                holder.answer4_results
        };

        holder.question_web_view_results.setWebViewClient(new MyBrowser());
        holder.question_web_view_results.loadUrl(Constants.URLS.QUESTION_URL + resultsList.get_question(position) + ".html");
        holder.answer1_results.setText(String.valueOf(resultsList.get_question_answer1(position)));
        holder.answer2_results.setText(String.valueOf(resultsList.get_question_answer2(position)));
        holder.answer3_results.setText(String.valueOf(resultsList.get_question_answer3(position)));
        holder.answer4_results.setText(String.valueOf(resultsList.get_question_answer4(position)));

        Log.d("MYLOGS", user_id + " " + user_id1 + " " + user_id2 + " " + position);
        int userAnswer;

        if (user_id == user_id1) {
            userAnswer = resultsList.get_user_answer1(position);
            holder.time_results.setText(String.valueOf(resultsList.get_time1(position)));
        } else {
            userAnswer = resultsList.get_user_answer2(position);
            holder.time_results.setText(String.valueOf(resultsList.get_time2(position)));
        }

        Log.d("MYLOGS", String.valueOf(userAnswer));
        Log.d("MYLOGS", String.valueOf(resultsList.get_answer_true(position)));
        Log.d("MYLOGS", "----------------------------------------------------");

        if (resultsList.get_answer_true(position) != userAnswer) {
            if(user_id==user_id1){
                answersTV[resultsList.get_user_answer1(position)-1].setTextColor(context.getResources().getColor(R.color.red));
            }else{
                answersTV[resultsList.get_user_answer2(position)-1].setTextColor(context.getResources().getColor(R.color.red));
            }

        }
        answersTV[resultsList.get_answer_true(position)-1].setTextColor(context.getResources().getColor(R.color.green));
    }

    @Override
    public int getItemCount() {
        return resultsList.get_size();
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder{

        TextView time_results,answer1_results,answer2_results,answer3_results,answer4_results;
        WebView question_web_view_results;

        public ResultsViewHolder(View itemView) {
            super(itemView);
            time_results = (TextView) itemView.findViewById(R.id.time_results);
            answer1_results = (TextView) itemView.findViewById(R.id.answer1_results);
            answer2_results = (TextView) itemView.findViewById(R.id.answer2_results);
            answer3_results = (TextView) itemView.findViewById(R.id.answer3_results);
            answer4_results = (TextView) itemView.findViewById(R.id.answer4_results);
            question_web_view_results = (WebView) itemView.findViewById(R.id.question_web_view_results);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
