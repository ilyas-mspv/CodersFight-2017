package atlascience.bitmaptest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.HashMap;

import atlascience.bitmaptest.Authenticator.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static atlascience.bitmaptest.Constants.Methods.Content.ERROR_INFO;
import static atlascience.bitmaptest.Constants.Methods.Version.VERSION2;

/**
 * Created by Ilyas on 20-Apr-17.
 */

public class BaseAppCompatActivity extends AppCompatActivity {


    public ProgressDialog progressDialog;
    public AlertDialog alertDialog;
    public AlertDialog.Builder builder;

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.dialog_load_type));
        progressDialog.setProgress(100);
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();
    }

    protected  void dismissProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }



    protected void setErrorAlert(final String error){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_error));
        builder.setMessage(error);
        builder.setNegativeButton(getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String deviceModel = android.os.Build.MODEL;
                String deviceAndroidVersion  = Build.VERSION.RELEASE;
                String appVersion = BuildConfig.VERSION_NAME;


                AppController.getApi().error_info(VERSION2,ERROR_INFO,"0",deviceModel,deviceAndroidVersion,appVersion,error).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
    }



    protected void setErrorAlert(int code){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_error));
        if(code == 0) builder.setMessage(getString(R.string.dialog_internet_error));
        if(code == 1) builder.setMessage(getString(R.string.dialog_error));

        builder.setNegativeButton(getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
    }


    protected  void  setWarningAlert( String message){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage(message);
        builder.setNeutralButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = builder.create();
    }




}
