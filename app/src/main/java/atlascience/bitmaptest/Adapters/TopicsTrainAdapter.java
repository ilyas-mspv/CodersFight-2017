package atlascience.bitmaptest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 8/21/2017.
 */

public class TopicsTrainAdapter extends RecyclerView.Adapter<TopicsTrainAdapter.TopicsViewHolder> {

    private Topics topics;
    private Context context;
    private ArrayList<Integer> d;

    public TopicsTrainAdapter(Topics topics, Context context) {
        this.topics = topics;
        this.context = context;
    }

    @Override
    public TopicsTrainAdapter.TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topic_train, parent, false);
        return new TopicsTrainAdapter.TopicsViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final TopicsTrainAdapter.TopicsViewHolder holder, final int position) {
        d = new ArrayList<Integer>();

        if(topics.isOpened(position)){
            holder.topic_id.setText(topics.getId(position));
            holder.topic.setText(topics.getTopic(position));
            holder.content.setText(topics.getContent(position));

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.checkBox.isChecked()){
                        holder.checkBox.setChecked(true);
                        d.add(Integer.valueOf(topics.getId(position)));
                    }else{
                        d.remove(Integer.valueOf(topics.getId(position)));
                        holder.checkBox.setChecked(false);
                    }
                    if(d.size()>0){
                        for (int i = 0; i < d.size(); i++) {
                            Log.i("ADAPTER", String.valueOf(d.size()));
                            Toast.makeText(context, Arrays.toString(d.toArray()), Toast.LENGTH_SHORT).show();
                        }
                        sendIntent(topics.getId(position));
                    }
                }
            });
        }
    }

    public void sendIntent(String id){
        Intent intent = new Intent("topics-data");
        intent.putIntegerArrayListExtra("array",d);
        intent.putExtra("topic_id",id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class TopicsViewHolder extends RecyclerView.ViewHolder {

        TextView topic_id,topic, content;
        CheckBox checkBox;

        public TopicsViewHolder(View itemView) {
            super(itemView);
            topic_id = (TextView) itemView.findViewById(R.id.topic_id);
            topic = (TextView) itemView.findViewById(R.id.topic_content);
            content = (TextView) itemView.findViewById(R.id.content_from_topic);
            checkBox = (CheckBox) itemView.findViewById(R.id.train_check_box);

        }
    }


}