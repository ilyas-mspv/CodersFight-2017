package atlascience.bitmaptest.Activities.TrainMode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.Adapters.TopicsTrainAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Interfaces.API;
import atlascience.bitmaptest.Models.Knowledge.Topics;
import atlascience.bitmaptest.Models.TrainMode.Codes;
import atlascience.bitmaptest.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TrainTopicsActivity extends AppCompatActivity {

    public static final String TAG = "TRAIN_TOPICS_TAG";
    private RecyclerView recyclerView;
    final int[] all_codes_counter = {0};

    //UI
    private Button train_game_btn;
    private Topics topics;
    private TopicsTrainAdapter adapter;
    private ArrayList<Integer> chosenTopicsIds;
    private SessionManager sessionManager;
    private LinearLayoutManager rv_topics_linear_manager;
    private String user_id,topic_id;

    //File vars
    private File destinationFile;
    private ArrayList<String> topic_ids = new ArrayList<>();

    private Codes codesModel;
    private Parcelable listState;

    //downloading files
    private  static API api;
    private Retrofit retrofit;
    public static  API getApi(){return  api;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_topics);
        //todo change to translatable
        setTitle("Choose topics to practise");
        inits();
        initRecycler();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("topics-data"));

        initRetrofit();


    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chosenTopicsIds = new ArrayList<>();
            chosenTopicsIds.addAll(intent.getIntegerArrayListExtra("array"));

            List<Integer> d = new ArrayList<>();
            for (int i = 0; i < chosenTopicsIds.size(); i++)
                d.add(chosenTopicsIds.get(i));


            topic_id = intent.getStringExtra("topic_id");
            topic_ids.add(topic_id);
            AppController.getApi().get_code_by_topic(Constants.Methods.Version.VERSION2,Constants.TrainMode.Methods.GET_CODE_BY_TOPIC, topic_id).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    codesModel = new Codes(response);
                    for (int i = 0; i < codesModel.size(); i++) {
                        //todo correct checking
                        downloadFile(codesModel.code_url(i),codesModel.code_name(i));
                        all_codes_counter[0]++;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    };

    private boolean check_if_file_exist(String code_name){
        File f = new File("/data/data/" + getPackageName() + "/games/" + code_name+ ".html");
        return f.exists() && !f.isDirectory();
    }
    private void initRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.codfi.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    private void inits() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String,String> d = sessionManager.getUserDetails();
        user_id = d.get("id");

        train_game_btn = (Button) findViewById(R.id.train_game_btn);
        train_game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic_ids.size()>=2){
                    Intent i = new Intent(TrainTopicsActivity.this,TrainGameActivity.class);
                    i.putExtra("topics_list",topic_ids);
                    i.putExtra("all_codes_size",all_codes_counter[0]);
                    startActivity(i);
                }else{
                    Toast.makeText(TrainTopicsActivity.this, "Choose at least 2 topics", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.train_rv);
        rv_topics_linear_manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_topics_linear_manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        AppController.getApi().get_opened_knowledge(Constants.Methods.Version.VERSION2,Constants.Methods.Content.GET_ONLY_OPENED_KNOWLEDGE,user_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                topics = new Topics(response,getApplicationContext());
                adapter = new TopicsTrainAdapter(topics,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    private void downloadFile(String url,String name) {

        api.downloadCodesRx(url)
                .flatMap(processResponse(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResult());

    }
    private Func1<Response<ResponseBody>, Observable<File>> processResponse(final String name) {
        return new Func1<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> call(Response<ResponseBody> responseBodyResponse) {
                return saveToDiskRx(responseBodyResponse,name);
            }
        };
    }
    private Observable<File> saveToDiskRx(final Response<ResponseBody> response, final String name) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {

                    new File("/data/data/" + getPackageName() + "/games").mkdir();
                    destinationFile = new File("/data/data/" + getPackageName() + "/games/" + name+ ".html");

                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    subscriber.onNext(destinationFile);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
    private Observer<File> handleResult() {
        return new Observer<File>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "Error " + e.getMessage());
            }

            @Override
            public void onNext(File file) {
                Log.d(TAG, "File downloaded to " + file.getAbsolutePath());
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}