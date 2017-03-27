package atlascience.bitmaptest.Auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    EditText email,password;
    Button login;
    SessionManager session;
    String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email_login);
        password =(EditText) findViewById(R.id.password_login);

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
                if(pass.trim().length()>4) {
                    if(mail.contains("@")){
                        AppController.getApi().getUser("getUser", mail, pass)
                                .enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        String r = response.body().toString();
                                        if (r.contains("Unvalid password")) {
                                            Toast.makeText(getApplicationContext(), "Invalid password or email", Toast.LENGTH_SHORT).show();
                                        } else {
                                            User user = new User(response);
                                            session.createLoginSession(user.getId(),user.getUsername(),mail);

                                            Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                );
                    }else {
                        email.setError("Invalid email");
                    }
                }else{
                    password.setError("Password must be more than 4 letters");

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

}}
