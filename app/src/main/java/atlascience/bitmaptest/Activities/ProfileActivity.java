package atlascience.bitmaptest.Activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import atlascience.bitmaptest.Activities.Learn.KnowledgeTopicsActivity;
import atlascience.bitmaptest.Activities.Settings.SettingsActivity;
import atlascience.bitmaptest.Activities.TrainMode.TrainTopicsActivity;
import atlascience.bitmaptest.Adapters.ProfileButtonsAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Profile.ProfileButtonsModel;
import atlascience.bitmaptest.Models.Profile.ProfileModel;
import atlascience.bitmaptest.Models.SuccessResponse;
import atlascience.bitmaptest.Models.TrainMode.TrainSettings;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.ItemClickSupport;
import atlascience.bitmaptest.Utils.ItemOffsetDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseAppCompatActivity {

    //UI

    Unbinder unbinder;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.username_profile) TextView username;
    @BindView(R.id.game_play) Button play_button;
//    @BindView(R.id.rating__btn_profile) Button rating;
//    @BindView(R.id.knowledge_btn_profile) Button knowledge;
//    @BindView(R.id.provide_question) Button provide_question;
//    @BindView(R.id.train_mode_btn) Button train_mode;
    @BindView(R.id.profile_photo) CircleImageView profile_photo;
    @BindView(R.id.statistics_btn_profile) ImageView statistics;
    @BindView(R.id.rv_profile_container) RecyclerView rv_profile;
    List<ProfileButtonsModel> models;


    private static final int STORAGE_PERMISSION_CODE = 123;

    //data
    SessionManager session;
    HashMap<String,String> user;

    //image operations
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initUI();
        requestStoragePermission();
        get_data();
        init();
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        prepareData();
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.default_spacing_small);
        int resId = R.anim.grid_layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        rv_profile.setAdapter(new ProfileButtonsAdapter(models));
        rv_profile.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_profile.setLayoutAnimation(animation);
        rv_profile.addItemDecoration(new ItemOffsetDecoration(spacing));

        ItemClickSupport.addTo(rv_profile).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(position == 1){
                    startActivity(new Intent(ProfileActivity.this, KnowledgeTopicsActivity.class));
                }

                if(position==2){
                    startActivity(new Intent(ProfileActivity.this, RatingActivity.class));
                }

                if(position == 0){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                        dialog.setTitle("Difficulty");
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.select_dialog_item);
                        //todo make this items able to translate
                        arrayAdapter.add("easy");
                        arrayAdapter.add("medium");
                        arrayAdapter.add("hard");
                        dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //todo add to shared prefs
                                TrainSettings trainSettings = new TrainSettings(getApplicationContext());
                                trainSettings.play(which + 1);
                                Log.i("MY_TAG", "difficulty is " + which + 1);

                                Intent i = new Intent(ProfileActivity.this, TrainTopicsActivity.class);
                                startActivity(i);
                            }
                        });
                        dialog.show();
                }
            }
        });

    }

    private void prepareData() {
        models = new ArrayList<>();
        models.add(new ProfileButtonsModel("Train Mode"));
        models.add(new ProfileButtonsModel("Knowledge"));
        models.add(new ProfileButtonsModel("Rating"));
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
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
    }



    private void init() {

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            select_photo();
            }
        });
//
//        provide_question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, ProvideQuestionActivity.class));
//            }
//        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,StatisticsActivity.class));
            }
        });

//
//        rating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, RatingActivity.class));
//            }
//        });
//
//        knowledge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, KnowledgeTopicsActivity.class));
//            }
//        });
//
//        if(check_if_knowledge_available()){
//            train_mode.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void onClick(View v) {
//
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
//                        dialog.setTitle("Difficulty");
//                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.select_dialog_item);
//                        //todo make this items able to translate
//                        arrayAdapter.add("easy");
//                        arrayAdapter.add("medium");
//                        arrayAdapter.add("hard");
//                        dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //todo add to shared prefs
//                                TrainSettings trainSettings = new TrainSettings(getApplicationContext());
//                                trainSettings.play(which + 1);
//                                Log.i("MY_TAG", "difficulty is " + which + 1);
//
//                                Intent i = new Intent(ProfileActivity.this, TrainTopicsActivity.class);
//                                startActivity(i);
//                            }
//                        });
//                        dialog.show();
//                }
//            });
//        }else{
//            train_mode.setEnabled(false);
//        }
    }

    private boolean check_if_knowledge_available() {
        /*
        *  check if user has only one knowledge topic
        */

        String user_id = user.get(SessionManager.KEY_ID);
        final int[] flag = {0};
            AppController.getApi().get_knowledge_ids_by_user(Constants.Methods.Version.VERSION2, Constants.Methods.Content.GET_KNOWLEDGE_IDS,user_id).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    ProfileModel profileModel = new ProfileModel(response);
                    SuccessResponse s = new SuccessResponse(response);

                    if(profileModel.size() == 1){
                        flag[0] = 1;
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        return flag[0]==0;
    }


    private void select_photo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void get_data() {
        session = new SessionManager(getApplicationContext());
        username = (TextView) findViewById(R.id.username_profile);
        if(session.isLoggedIn()) {
            user = session.getUserDetails();
            int a_status = Integer.parseInt(user.get(SessionManager.KEY_ACCOUNT_STATUS));
            String url = user.get(SessionManager.KEY_IMAGE_URL);

            final String id = user.get(SessionManager.KEY_ID);
            String name = user.get(SessionManager.KEY_NAME);

            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    server_add_to_queue(id);
                    Intent i = new Intent(ProfileActivity.this, QueueActivity.class);
                    startActivity(i);

                }
            });

            if(isNetworkAvailable()){
                showProgress();

                if(!url.equals(""))
                    Picasso.with(ProfileActivity.this).load(url).error(R.drawable.default_user).into(profile_photo);
//
//                provide_question.setVisibility(View.GONE);
//                if (a_status == 0) {
//                    //usual user
//                } else {
//                    //teacher
//                    provide_question.setVisibility(View.VISIBLE);
//                }

                username.setText(name);
                dismissProgress();
            }else{
                setErrorAlert(0);
            }

        }
    }

    public void server_add_to_queue(final String id){
        showProgress();
        AppController.getApi().addtoQueue(Constants.Methods.Version.VERSION,Constants.Methods.Game.Queue.ADD,id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dismissProgress();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgress();
                setErrorAlert(t.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(ProfileActivity.this,SettingsActivity.class));
                return true;
        }

        return true;

    }
}
