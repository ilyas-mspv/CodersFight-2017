package atlascience.bitmaptest.Activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import atlascience.bitmaptest.Adapters.TopicsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.Models.Knowledge.TopicsResponse;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeTopicsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_topics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        AppController.getApi().getAllTopics("getAllTopics").enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                List<Topics> topics = response.body().getResults();
                recyclerView.setAdapter(new TopicsAdapter(topics, R.layout.row_topic, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {

            }
        });
    }
}
