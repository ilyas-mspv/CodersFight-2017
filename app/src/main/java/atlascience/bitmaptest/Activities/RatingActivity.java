package atlascience.bitmaptest.Activities;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.Adapters.RatingAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Models.Rating.Rating;
import atlascience.bitmaptest.Models.Rating.RatingResponse;
import atlascience.bitmaptest.R;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends BaseAppCompatActivity {

    private static final String TAG = RatingActivity.class.getSimpleName();
    SweetAlertDialog dialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
        showProgress(getResources().getString(R.string.dialog_load_type));
        get_list();

    }

    public void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_rating);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    public void get_list() {
        SessionManager session = new SessionManager(getApplicationContext());
        final HashMap<String, String> d = session.getUserDetails();

        AppController.getApi().getAllUser("getAllUserByRating", Integer.parseInt(d.get(SessionManager.KEY_ID))).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                List<Rating> users = response.body().getResults();
                recyclerView.setAdapter(new RatingAdapter(users, getApplicationContext()));
                dismissProgress();
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {
                dismissProgress();
                setErrorAlert(t.getMessage());
            }
        });
    }

}
