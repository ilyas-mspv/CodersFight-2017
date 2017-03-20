package atlascience.bitmaptest.Auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity{


    EditText reset_email,reset_password;
    Button reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset_email = (EditText) findViewById(R.id.email_reset);
        reset_password = (EditText) findViewById(R.id.password_reset);
        reset = (Button) findViewById(R.id.reset_btn) ;

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = reset_email.getText().toString();
                final String pass = reset_password.getText().toString();


                AppController.getApi().forgotPassword("forgotPassword", email, pass)
                        .enqueue(new Callback<JsonObject>() {
                                     @Override
                                     public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                         startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                     }

                                     @Override
                                     public void onFailure(Call<JsonObject> call, Throwable t) {

                                     }
                                 }
                        );
            }
        });

    }
}
