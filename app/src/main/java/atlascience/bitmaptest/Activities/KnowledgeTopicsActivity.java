package atlascience.bitmaptest.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;

import java.util.List;

import atlascience.bitmaptest.Adapters.TopicsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeTopicsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Topics topics;
    TopicsAdapter topicsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_topics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        AppController.getApi().getAllTopics("getAllTopics").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                topics = new Topics(getApplicationContext(),response);
                recyclerView.setAdapter(new TopicsAdapter(topics,getApplicationContext()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String content = topics.getContent(position);
                String topic = topics.getTopic(position);
                Question question = new Question(getApplicationContext());
                Question.create_content_ses(content,topic);
                startActivity(new Intent(KnowledgeTopicsActivity.this,KnowledgeContentActivity.class));
            }
        });
    }
}
