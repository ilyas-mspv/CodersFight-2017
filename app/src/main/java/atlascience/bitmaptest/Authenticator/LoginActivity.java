package atlascience.bitmaptest.Authenticator;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.SuccessResponse;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseAppCompatActivity {


    @BindView(R.id.email_login) EditText email_et;
    @BindView(R.id.sign_up) TextView sign_up;
    @BindView(R.id.reset_password) TextView forgot_password;
    @BindView(R.id.password_login)  EditText password_et;
    @BindView(R.id.login_btn) Button login_btn;
    SessionManager session;

    //GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//        initGoogleAuth();
//        initFacebookAuth();

        init();

    }

    private void initFacebookAuth() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void initGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


//        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_google);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });
    }



    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, "Google good", Toast.LENGTH_SHORT).show();
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Toast.makeText(this, "Person name: " + personName + "\n"+
                        "Person GivenName: " + personGivenName + "\n"+
                        "Person FamilyName: " + personFamilyName + "\n"+
                        "Person ID: " + personId + "\n", Toast.LENGTH_SHORT).show();
                //TODO add dialog with adding nickname

            }

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {

        session = new SessionManager(getApplicationContext());

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_et.getText().toString();
                final String password = password_et.getText().toString();
                if(checkEmail(email) && checkPassword(password)) log_in(email,password);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    private boolean checkPassword(String password) {
        if(password.length() > 4) {
            return true;
        } else {
            password_et.setError(getString(R.string.password_error));
            return false;
        }
    }

    private boolean checkEmail(String email) {
        if(email.contains("@")&& email.contains(".")){
            return true;
        }else {
            email_et.setError(getString(R.string.email_error));
            return false;
        }
    }

    private void log_in(final String email, String password) {

        AppController.getApi().getUser(Constants.Methods.Version.VERSION2,Constants.Methods.User.SIGN_IN, email, password)
                .enqueue(new Callback<JsonObject>() {
                    @Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        SuccessResponse s = new SuccessResponse(response);
                        if(s.success()) {
                            User user = new User(response);
                            session.createLoginSession(user.getId(), user.getUsername(), email,user.getUrl(),user.getStatus());
                            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                            finish();
                        }else{
                            setErrorAlert(1);
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        setErrorAlert(t.getMessage());
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
    }


}
