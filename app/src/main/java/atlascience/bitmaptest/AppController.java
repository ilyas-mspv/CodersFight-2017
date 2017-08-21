package atlascience.bitmaptest;


import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

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


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Toast.makeText(getApplicationContext(),"NO WAY",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        VKSdk.initialize(this);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.e("TAG", Arrays.toString(fingerprints));

        vkAccessTokenTracker.startTracking();

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
