package atlascience.bitmaptest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ilyas on 20-Apr-17.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    SweetAlertDialog load_alert,error_alert;

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    protected  void showToastShort(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    protected  void showToastLong(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    protected  void showProgress(String msg){
    }

    protected  void dismissProgress(){
        if(load_alert != null){
            load_alert.dismiss();
            load_alert = null;
        }
    }

    protected void setErrorAlert(String error){
//        error_alert = new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE);
//        error_alert.setTitleText(getResources().getString(R.string.dialog_error_type));
//        error_alert.setContentText(error);
//        error_alert.setCancelable(false);
//        error_alert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismiss();
//            }
//        });
//        error_alert.create();
//        error_alert.show();
    }


}
