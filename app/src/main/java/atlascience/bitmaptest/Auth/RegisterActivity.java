package atlascience.bitmaptest.Auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity  extends AppCompatActivity {


    EditText username,email,password;
    Button sign_up;
    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.nickname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_up = (Button) findViewById(R.id.sign_up_btn);
        session = new SessionManager(getApplicationContext());

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

    }

    public void addUser() {

       final String user = username.getText().toString();
       final String mail = email.getText().toString();
       final String pass = password.getText().toString();


        AppController.getApi().addUser("addUser", user, mail, pass)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String r = response.body().toString();
                        if(r.contains("User created")){
                            login(mail,pass);
                        }else{
                            Toast.makeText(getApplicationContext(),"Error. Try again later",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                }
                );
    }

    private void login(final String email,final String password) {

        AppController.getApi().getUser("getUser",email,password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                User user = new User(response);
                session.createLoginSession(user.getId(),user.getUsername(),email);

                Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}

