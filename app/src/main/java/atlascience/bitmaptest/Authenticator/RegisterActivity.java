package atlascience.bitmaptest.Authenticator;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.GeneralDialogFragment;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static atlascience.bitmaptest.Models.Game.context;

public class RegisterActivity  extends AppCompatActivity implements GeneralDialogFragment.OnDialogFragmentClickListener {


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
            if (pass.length() > 4) {
                if (mail.contains("@") && mail.contains(".")) {
                    AppController.getApi().addUser("addUser", user, mail, pass)
                            .enqueue(new Callback<JsonObject>() {
                                         @Override
                                         public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                             String r = response.body().toString();

                                             if (!r.contains("User exists")) {
                                                 if (r.contains("User created")) {
                                                     login(mail, pass);
                                                 }else{
                                                     new SweetAlertDialog(RegisterActivity.this)
                                                             .setTitleText("User is already registered")
                                                             .setContentText("Try to login or create another one")
                                                             .show();

                                                 }
                                             }

                                         }

                                         @Override
                                         public void onFailure(Call<JsonObject> call, Throwable t) {

                                         }
                                     }
                            );
                } else {
                    email.setError("Invalid email");
                }
            } else {
                password.setError("Password is too small");
            }
    }

    private void login(final String email,final String password) {

        AppController.getApi().getUser("getUser",email,password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                User user = new User(response);
                session.createLoginSession(user.getId(),user.getUsername(),email,user.getUrl(),user.getStatus());
                Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {

    }

    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {

    }
}

