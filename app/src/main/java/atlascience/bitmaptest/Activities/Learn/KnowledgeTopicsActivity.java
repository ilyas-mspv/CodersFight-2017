package atlascience.bitmaptest.Activities.Learn;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.Adapters.TopicsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game.Question;
import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.SQLite.KnowledgeDatabaseHelper;
import atlascience.bitmaptest.Utils.ItemClickSupport;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeTopicsActivity extends BaseAppCompatActivity {

    RecyclerView recyclerView;
    Topics topics;
    SweetAlertDialog dialog;
    SessionManager sessionManager;
    String user_id;
    boolean is_first_opened_state = false;
    KnowledgeDatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_topics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new KnowledgeDatabaseHelper(getApplicationContext());

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String> d = sessionManager.getUserDetails();
        user_id = d.get("id");

        initRv();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("open_state"));
    }


    private boolean check_if_content_loaded(){
        return false;
    }

    private void initRv() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        final int flag;
        if(isNetworkConnected()){
            initServerContent();
            flag = 1;
        }else{
            initDB();
            flag = 0;
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(flag == 1){
                    if(topics.isOpened(position)){
                        String id = topics.getId(position);
                        String content = topics.getContent(position);
                        String topic = topics.getTopic(position);
                        Question question = new Question(getApplicationContext());
                        Question.create_content_ses(id,content,topic);
                        startActivity(new Intent(KnowledgeTopicsActivity.this,KnowledgeContentActivity.class));
                    }else if(is_first_opened_state){

                        String id = topics.getId(position);
                        String content = topics.getContent(position);
                        String topic = topics.getTopic(position);
                        Question question = new Question(getApplicationContext());
                        Question.create_content_ses(id,content,topic);
                        startActivity(new Intent(KnowledgeTopicsActivity.this,KnowledgeContentActivity.class));
                    }else{
                        Toast.makeText(KnowledgeTopicsActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //if offline
                    Toast.makeText(KnowledgeTopicsActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initDB() {
        List<Topics> models = db.getAllTopics();
        recyclerView.setAdapter(new TopicsAdapter(models,getApplicationContext(),0,db));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            is_first_opened_state = intent.getBooleanExtra("open",false);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initServerContent() {
        showProgress();

        AppController.getApi().get_opened_knowledge(Constants.Methods.Version.VERSION2,Constants.Methods.Content.GET_OPENED_KNOWLEDGE,user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                topics = new Topics(response,getApplicationContext());
                for (int i = 0; i < topics.size(); i++) {
                    int j  = topics.isOpened(i) ? 1 : 0;
                    db.addTopic(topics.getId(i),topics.getTopic(i),topics.getContent(i),j);
                }
                recyclerView.setAdapter(new TopicsAdapter(topics,getApplicationContext(),1));
                dismissProgress();

                int resId = R.anim.layout_animation_fall_down;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);

                recyclerView.setLayoutAnimation(animation);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgress();
                setErrorAlert(t.getMessage());
            }
        });

    }

}
