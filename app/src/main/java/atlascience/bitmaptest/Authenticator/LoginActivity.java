package atlascience.bitmaptest.Authenticator;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    EditText email,password;
    Button login;
    TextView log_login;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email_login);
        password =(EditText) findViewById(R.id.password_login);
        log_login = (TextView) findViewById(R.id.log_login);
        session = new SessionManager(getApplicationContext());
        init();

    }


    private void init() {
        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email.getText().toString();
                final String pass = password.getText().toString();
                if (mail.contains("@") && mail.contains(".")) {
                    if (pass.length() > 4) {
                        log_in(mail, pass);
                    } else {
                        password.setError("Password is too small");
                    }
                } else {
                    email.setError("Invalid email");
                }
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

    private void log_in(final String email, String password) {

        AppController.getApi().getUser("getUser", email, password)
                .enqueue(new Callback<JsonObject>() {
                    @Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.body().toString().contains("invalid password")) {
                            if (!response.body().toString().contains("invalid email")) {
                                User user = new User(response);
                                session.createLoginSession(user.getId(), user.getUsername(), email,user.getUrl(),user.getStatus());
                                Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                log_login.setText("Invalid email.");
                            }
                        } else {
                            log_login.setText("Invalid password.");
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                }
        );
    }

}
