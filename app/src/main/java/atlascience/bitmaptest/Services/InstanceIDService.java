package atlascience.bitmaptest.Services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import atlascience.bitmaptest.Auth.SessionManager;


public class InstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = InstanceIDService.class.getSimpleName();
    String refreshedToken;
    SessionManager session;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        session = new SessionManager(getApplicationContext());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", refreshedToken);
        editor.commit();
        sendRegistrationToServer(refreshedToken);


        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        session.create_token(token);
    }

}