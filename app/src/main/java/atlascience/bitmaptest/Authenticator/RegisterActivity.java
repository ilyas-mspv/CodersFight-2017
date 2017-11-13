package atlascience.bitmaptest.Authenticator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.User;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.GeneralDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static atlascience.bitmaptest.Constants.Methods.User.GET;
import static atlascience.bitmaptest.Constants.Methods.Version.VERSION;

public class RegisterActivity  extends BaseAppCompatActivity implements GeneralDialogFragment.OnDialogFragmentClickListener {

    @BindView(R.id.nickname) EditText username;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.sign_up_btn) Button sign_up;

    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

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
                    AppController.getApi().addUser(VERSION,Constants.Methods.User.ADD, user, mail, pass)
                            .enqueue(new Callback<JsonObject>() {
                                         @Override
                                         public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                             String r = response.body().toString();

                                             if (!r.contains("User exists")) {
                                                 if (r.contains("User created")) {
                                                     login(mail, pass);
                                                 }else{
                                                     AlertDialog.Builder d = new AlertDialog.Builder(RegisterActivity.this)
                                                             .setTitle(getString(R.string.user_error))
                                                             .setMessage(getString(R.string.user_error_msg));
                                                     AlertDialog f = d.create();
                                                     f.show();

                                                 }
                                             }

                                         }



                                @Override
                                         public void onFailure(Call<JsonObject> call, Throwable t) {

                                         }
                                     }
                            );
                } else {
                    email.setError(getString(R.string.email_error));
                }
            } else {
                password.setError(getString(R.string.password_error));
            }
    }

    private void login(final String email,final String password) {

        AppController.getApi().getUser(VERSION,GET,email,password).enqueue(new Callback<JsonObject>() {
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
                setErrorAlert(t.getMessage());
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

