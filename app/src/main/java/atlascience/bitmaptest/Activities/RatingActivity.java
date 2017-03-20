package atlascience.bitmaptest.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import atlascience.bitmaptest.Adapters.RatingAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Objects.Rating_User;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {

    private static final String TAG = RatingActivity.class.getSimpleName();
    List<Rating_User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetAll().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private  class GetAll extends AsyncTask<String,String,String>{
        ProgressDialog dialog = new ProgressDialog(RatingActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            AppController.getApi().getAllUser("getAllUser").enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    String res = response.body().toString();
                    try {
                        data=new ArrayList<>();
                        JSONArray ja = new JSONArray(res);

                        for(int i = 0;i<ja.length();i++){
                            JSONObject js = ja.getJSONObject(i);
                            Rating_User rating = new Rating_User();
                            int asdd=  rating.id = js.getInt("id");
                            int ds= rating.rating = js.getInt("rating");
                            String d =rating.username = js.getString("username");

                            Log.i(TAG, String.valueOf(asdd));
                            Log.i(TAG, String.valueOf(ds));
                            Log.i(TAG, d);

                            data.add(rating);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

//            RecyclerView rv = (RecyclerView) findViewById(R.id.rating_recycler);
//            RatingAdapter mAdapter = new RatingAdapter(RatingActivity.this,data);
//            rv.setAdapter(mAdapter);
//            rv.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
        }
    }

}
