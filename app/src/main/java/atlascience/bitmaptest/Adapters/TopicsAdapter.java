package atlascience.bitmaptest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.SQLite.KnowledgeDatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder> {

    private Topics topics;
    public boolean isClickable = true;
    Context context;
    SessionManager sessionManager;
    String user_id;
    boolean opened = false;
    private List<Topics> listTopics;
    int flag;
    KnowledgeDatabaseHelper db;

    public TopicsAdapter(Topics topics, Context context,int flag) {
        this.topics = topics;
        this.context = context;
        this.flag = flag;
    }

    public TopicsAdapter(List<Topics> topics,Context context,int flag,KnowledgeDatabaseHelper db) {
        this.listTopics = topics;
        this.context = context;
        this.flag = flag;
        this.db = db;
    }

    @Override
    public TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topic, parent, false);
        sessionManager = new SessionManager(context);
        HashMap<String,String> d = sessionManager.getUserDetails();
        user_id = d.get("id");


        return new TopicsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicsViewHolder holder, int position) {
        if(flag==1){
            if(position==0 && !topics.isOpened(position)){
                holder.lock_image.setVisibility(View.GONE);
                isClickable = true;
                open_knowledge(position);
                opened = true;Intent intent = new Intent("open_state");
                intent.putExtra("open",opened);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
            holder.topic.setText(topics.getTopic(position));
            holder.content.setText(topics.getContent(position));
            if(topics.isOpened(position)){
                holder.lock_image.setVisibility(View.GONE);
                isClickable = true;
            }else{
                isClickable = false;
            }
        }else{
            holder.topic.setText(listTopics.get(position).getTitle());
            holder.content.setText(listTopics.get(position).getContent());
            if(listTopics.get(position).getIs_open()==1){
                holder.lock_image.setVisibility(View.GONE);
                isClickable = true;
            }else{
                isClickable = false;
            }
        }
    }

    private void open_knowledge(int pos) {
        AppController.getApi().open_knowledge_topic(Constants.Methods.Version.VERSION2,Constants.Methods.Content.OPEN_KNOWLEDGE,user_id,topics.getId(pos)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return flag == 1 ? topics.size() : db.getTopicCount();
    }

    public class TopicsViewHolder extends RecyclerView.ViewHolder {

        TextView topic, content;
        ImageView lock_image;

        public TopicsViewHolder(View itemView) {
            super(itemView);
            topic = (TextView) itemView.findViewById(R.id.topic_content);
            content = (TextView) itemView.findViewById(R.id.content_from_topic);
            lock_image = (ImageView) itemView.findViewById(R.id.lock_image);
        }
    }


}
