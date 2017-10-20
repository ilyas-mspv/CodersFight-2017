    package atlascience.bitmaptest.Utils;

    import android.content.Context;
    import android.content.Intent;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.Handler;
    import android.support.annotation.Nullable;
    import android.support.v7.app.AppCompatActivity;

    import com.google.firebase.iid.FirebaseInstanceId;
    import com.google.gson.JsonObject;

    import java.util.HashMap;

    import atlascience.bitmaptest.Activities.ProfileActivity;
    import atlascience.bitmaptest.AppController;
    import atlascience.bitmaptest.Authenticator.LoginActivity;
    import atlascience.bitmaptest.Authenticator.SessionManager;
    import atlascience.bitmaptest.Constants;
    import atlascience.bitmaptest.R;
    import de.hdodenhof.circleimageview.CircleImageView;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;


/**
 * Created by Ilyas on 22-Apr-17.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    SessionManager session;
    HashMap<String,String> user;
    CircleImageView profile_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Tasks().execute();
            }
        }, SPLASH_TIME_OUT);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class Tasks extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            session.checkLogin();

            if(isNetworkAvailable()) {
                user = session.getUserDetails();
                if (session.isLoggedIn()) {
                    String id = user.get(SessionManager.KEY_ID);
                    String token = FirebaseInstanceId.getInstance().getToken();
                    set_token(id, token);
                    session.create_token(token);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(session.isLoggedIn()){
                Intent i = new Intent(SplashScreenActivity.this, ProfileActivity.class);
                startActivity(i);

                finish();
            }else{
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
            }

        }
    }


    public void set_token(String id,String token){

        AppController.getApi().set_token(Constants.Methods.Version.VERSION,Constants.Methods.User.SET_TOKEN,id,token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}


