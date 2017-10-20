package atlascience.bitmaptest.Authenticator;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.SuccessResponse;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    EditText email_et, password_et;
    Button login_btn;
    TextView log_login;
    SessionManager session;

    ImageView logo_view;

    //GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initGoogleAuth();
        initFacebookAuth();

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


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
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

        logo_view = (ImageView) findViewById(R.id.logo_image);
        logo_view.setImageDrawable(Drawable.createFromPath(getResources().getResourceName(R.drawable.logo)));


        email_et = (EditText) findViewById(R.id.email_login);
        password_et =(EditText) findViewById(R.id.password_login);
        log_login = (TextView) findViewById(R.id.log_login);

        session = new SessionManager(getApplicationContext());

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_et.getText().toString();
                final String password = password_et.getText().toString();
                if(checkEmail(email) && checkPassword(password)) log_in(email,password);
            }
        });

        TextView sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        TextView forgot_password = (TextView) findViewById(R.id.reset_password);
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
            //todo make string
            password_et.setError(getResources().getResourceName(R.string.app_name));
            return false;
        }
    }

    private boolean checkEmail(String email) {
        if(email.contains("@")&& email.contains(".")){
            return true;
        }else {
            //todo make string
            email_et.setError(getResources().getResourceName(R.string.app_name));
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
                            //todo show error info to user
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


}
