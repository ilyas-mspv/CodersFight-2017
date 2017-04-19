package atlascience.bitmaptest.Activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    //data
    SessionManager session;
    HashMap<String,String> user;
    //UI
    Button logout, play_button, rating, knowledge, statistics,provide_question;
    TextView username;
    ImageView profile_photo;
    //image operations
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar();
        requestStoragePermission();
        init();
        get_data();
    }

    public void uploadMultipart() {
        String path = getPath(filePath);

        try {
            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, uploadId, Constants.Profile.UPLOAD_URL)
                    .addFileToUpload(path, "image")
                    .addParameter("user_id",user.get(SessionManager.KEY_ID))
                    .addParameter("name", user.get(SessionManager.KEY_NAME))
                    .setMaxRetries(2)
                    .startUpload();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_photo.setImageBitmap(bitmap);
                uploadMultipart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void init() {
        profile_photo = (CircleImageView) findViewById(R.id.profile_photo);
        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            select_photo();
            }
        });

        provide_question = (Button) findViewById(R.id.provide_question);
        provide_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProvideQuestionActivity.class));
            }
        });

        logout = (Button) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

        rating = (Button) findViewById(R.id.rating__btn_profile);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, RatingActivity.class));
            }
        });
        knowledge = (Button) findViewById(R.id.knowledge_btn_profile);
        knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, KnowledgeTopicsActivity.class));
            }
        });

    }


    private void select_photo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void get_data() {
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        username = (TextView) findViewById(R.id.username_profile);
        if(session.isLoggedIn()) {
            user = session.getUserDetails();
            int a_status = Integer.parseInt(user.get(SessionManager.KEY_ACCOUNT_STATUS));

            provide_question.setVisibility(View.GONE);
            if (a_status == 0) {
                //usual user
            } else {
                //teacher
                provide_question.setVisibility(View.VISIBLE);
            }

            final String id = user.get(SessionManager.KEY_ID);
            String name = user.get(SessionManager.KEY_NAME);
            String photo_url = user.get(SessionManager.KEY_IMAGE_URL);
            if(!photo_url.equals("")) update_photo_url();

            String token = FirebaseInstanceId.getInstance().getToken();
            set_token(id, token);
            session.create_token(token);

            play_button = (Button) findViewById(R.id.game_play);
            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    server_add_to_queue(id);
                    Intent i = new Intent(ProfileActivity.this, QueueActivity.class);
                    startActivity(i);

                }
            });

            username.setText(name);
        }
    }

    private void update_photo_url(){
        AppController.getApi().update_url("update_url",user.get(SessionManager.KEY_ID)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String url = jsonObject.get("photo_url").getAsString();
                if(!url.equals(""))
                Picasso.with(ProfileActivity.this).load(url).error(R.drawable.default_user).into(profile_photo);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void server_add_to_queue(final String id){
        AppController.getApi().addtoQueue("add_to_queue",id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void set_token(String id,String token){

        if(session.get_token().equals(token)){
            Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_SHORT).show();
        }else{
            AppController.getApi().set_token("set_token",id,token).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }

}
