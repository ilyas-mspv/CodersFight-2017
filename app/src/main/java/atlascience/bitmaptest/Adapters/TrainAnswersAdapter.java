package atlascience.bitmaptest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import atlascience.bitmaptest.Models.TrainMode.Answers;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 17.10.17.
 */

public class TrainAnswersAdapter extends RecyclerView.Adapter<TrainAnswersAdapter.AnswersViewHolder> {

    private View contactView;
    private Context context;
    private Answers answers;

    public TrainAnswersAdapter(Context context, Answers answers) {
        this.context = context;
        this.answers = answers;
    }


    @Override
    public AnswersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        contactView = inflater.inflate(R.layout.row_answers_train_mode, parent, false);

        return new AnswersViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(AnswersViewHolder holder, int position) {
//        holder.id.setText(answers.getOrder(position));
        holder.answer.setText(answers.getAnswer(position));
    }

    @Override
    public int getItemCount() {
        //todo check if working
        return answers.size();
    }

    class AnswersViewHolder extends RecyclerView.ViewHolder{
        TextView id,answer;
        AnswersViewHolder(View itemView) {
            super(itemView);
            id= (TextView) itemView.findViewById(R.id.answer_id_train_mode);
            answer = (TextView) itemView.findViewById(R.id.answer_container);
        }
    }
}
