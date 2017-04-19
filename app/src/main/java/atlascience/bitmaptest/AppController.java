package atlascience.bitmaptest;


import android.app.Application;

import atlascience.bitmaptest.Interfaces.API;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppController  extends Application{

    private static API api;
    private static AppController mInstance;
    private Retrofit retrofit;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static API getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URLS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(API.class);
    }

}
