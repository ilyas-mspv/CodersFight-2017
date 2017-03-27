package atlascience.bitmaptest.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Auth.SessionManager;
import atlascience.bitmaptest.Fragments.QuestionToAnswerFragment;
import atlascience.bitmaptest.Fragments.QuestionToRateFragment;
import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.Models.Zones;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    //map and activity
    ImageView r1,r2,r3,r4,r5;
    boolean doublePress = false;
    SessionManager session;
    //users' data
    String name;
    TextView user_id1,user_id2,logs;
    String game_id, first_player, user1_id, user2_id, start_zone1, start_zone2, username1, username2;
    int first_user_id;
    int[] zones = new int[6];
    int zone_1, zone_2, user_zone1, user_zone2;
    FragmentManager fm = getSupportFragmentManager();
    Bundle bundle;
    private final View.OnTouchListener rOnTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            int color = 0;
            try {
                color = bmp.getPixel((int) event.getX(), (int) event.getY());
            } catch (Exception e) {
                Log.e("COLOR ERROR", e.toString());
            }
            if (color == Color.TRANSPARENT)
                return false;
            else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isMyZone(v.getId())) {
                            choose_land(v.getId());
                        }
                        int i = get_touched(v.getId());
                        bundle = new Bundle();
                        bundle.putInt("z", i);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;

            }


        }
    };
    boolean isTouchable;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());

        initMap();
        notification_receiver();
        GetSetData();

    }

    private void notification_receiver() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String tag = intent.getStringExtra("tag");
                    switch (tag) {
                        case "question_to_answer":

                            int user_id = intent.getIntExtra("first_user_id", 0);
                            int question_id = intent.getIntExtra("question_id", 0);
                            String question_answer = intent.getStringExtra("question");
                            String answer1 = intent.getStringExtra("answer1");
                            String answer2 = intent.getStringExtra("answer2");
                            String answer3 = intent.getStringExtra("answer3");
                            String answer4 = intent.getStringExtra("answer4");
                            int time = intent.getIntExtra("time", 0);
                            int answer_true = intent.getIntExtra("answer_true", 0);

                            Question ques = new Question(getApplicationContext());
                            ques.create_question(tag, user_id, question_id, question_answer, answer1, answer2, answer3, answer4, answer_true, time);

                            HashMap<String, String> question_data = ques.getQuestion();
                            String question = question_data.get(Question.KEY_QUESTION);
                            if (!question.equals("")) create_question_to_answer_dialog();
                            break;
                        case "question_to_rate":

                            int user_id_rate = intent.getIntExtra("first_user_id", 0);
                            int question_id_rate = intent.getIntExtra("question_id", 0);
                            String question_answer_rate = intent.getStringExtra("question");
                            String answer1_rate = intent.getStringExtra("answer1");
                            String answer2_rate = intent.getStringExtra("answer2");
                            String answer3_rate = intent.getStringExtra("answer3");
                            String answer4_rate = intent.getStringExtra("answer4");
                            int time_rate = intent.getIntExtra("time", 0);
                            int answer_true_rate = intent.getIntExtra("answer_true", 0);

                            Question ques_rate = new Question(getApplicationContext());
                            ques_rate.create_question(tag, user_id_rate, question_id_rate, question_answer_rate, answer1_rate, answer2_rate, answer3_rate, answer4_rate, answer_true_rate, time_rate);

                            HashMap<String, String> question_data_rate = ques_rate.getQuestion();
                            String question_rate = question_data_rate.get(Question.KEY_QUESTION);
                            if (!question_rate.equals("")) create_question_to_rate_dialog();
                            break;

                        case "answer":

                            boolean is_captured = intent.getBooleanExtra("captured", false);
                            int u_id = intent.getIntExtra("user_id", 0);
                            int z = intent.getIntExtra("zone", 0);

                            Zones zones = new Zones(getApplicationContext());
                            zones.create_zone_session(tag, is_captured, z, u_id);

                            HashMap<String, String> zones_data = zones.get_zone_session();
                            String captured = zones_data.get(Zones.CAPTURED);
                            String user_id_answer = zones_data.get(Zones.USER_ID);
                            String zone = zones_data.get(Zones.ZONE);

                            Log.e(TAG, "CAPTURED: " + captured);
                            Log.e(TAG, "USER_ID: " + user_id_answer);
                            Log.e(TAG, "ZONE: " + zone);

                            setCapturedZone(captured, user_id_answer, zone);
                            break;
                    }
                }
            }
        };
    }

    //sets when Game has just created
    private void initMap() {
        r1 = (ImageView) findViewById(R.id.r1);
        r2 = (ImageView) findViewById(R.id.r2);
        r3 = (ImageView) findViewById(R.id.r3);
        r4 = (ImageView) findViewById(R.id.r4);
        r5 = (ImageView) findViewById(R.id.r5);

        r1.setDrawingCacheEnabled(true);
        r2.setDrawingCacheEnabled(true);
        r3.setDrawingCacheEnabled(true);
        r4.setDrawingCacheEnabled(true);
        r5.setDrawingCacheEnabled(true);

        Button finish_test = (Button) findViewById(R.id.finish_test);
        finish_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game game = new Game(getApplicationContext());
                Zones z = new Zones(getApplicationContext());
                Question q = new Question(getApplicationContext());
                z.delete();
                game.delete_game();
                q.delete();
                System.exit(0);
            }
        });

    }

    private  void GetSetData(){

        user_id1 = (TextView) findViewById(R.id.user_id1);
        user_id2 = (TextView) findViewById(R.id.user_id2);
        logs = (TextView) findViewById(R.id.logs_main);

        //setting user's default data
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);

        try{
            Game game = new Game(getApplicationContext());
            HashMap<String, String> data = game.getDetails();
            game_id = data.get(Game.KEY_GAME_ID);
            first_player = data.get(Game.KEY_FIRST);
            user1_id = data.get(Game.KEY_USER_ID1);
            user2_id = data.get(Game.KEY_USER_ID2);
            start_zone1 = data.get(Game.KEY_START_ZONE_1);
            start_zone2 = data.get(Game.KEY_START_ZONE_2);
            username1 = data.get(Game.KEY_USERNAME_1);
            username2 = data.get(Game.KEY_USERNAME_2);
        }catch (Error e){
            Log.e(TAG,e.toString());
        }


        init_first_player();

        //TODO switch to background opacity
        switch (zone_1) {
            case 1:
                r1.setImageResource(R.drawable.map_area_1_green_capital);
                break;
            case 2:
                r2.setImageResource(R.drawable.map_area_2_green_capital);
                break;
            case 3:
                r3.setImageResource(R.drawable.map_area_3_green_capital);
                break;
            case 4:
                r4.setImageResource(R.drawable.map_area_4_green_capital);
                break;
            case 5:
                r5.setImageResource(R.drawable.map_area_5_green_capital);
                break;
        }

        switch (zone_2) {
            case 1:
                r1.setImageResource(R.drawable.map_area_1_red_capital);
                break;
            case 2:
                r2.setImageResource(R.drawable.map_area_2_red_capital);
                break;
            case 3:
                r3.setImageResource(R.drawable.map_area_3_red_capital);
                break;
            case 4:
                r4.setImageResource(R.drawable.map_area_4_red_capital);
                break;
            case 5:
                r5.setImageResource(R.drawable.map_area_5_red_capital);
                break;
        }

    }

    private void init_first_player() {

        //if username1 equals default profile's name, define first player.
        if(username1.equals(name)) {
            //if user1 is first
            if(first_player.equals(user1_id)) {

                first_user_id = Integer.parseInt(user1_id);
                logs.setTextColor(getResources().getColor(R.color.green));
                logs.setText("You're first.");
                isTouchable = true;
                setOnClickListenersForUser(isTouchable);

            }else{

                first_user_id = Integer.parseInt(user2_id);
                logs.setTextColor(getResources().getColor(R.color.red));
                logs.setText(username2 +" is first.");
                isTouchable = false;
                setOnClickListenersForUser(isTouchable);

            }

            user_id1.setText(username1 + "(You)");
            zone_1 = Integer.parseInt(start_zone1);
            zone_2 = Integer.parseInt(start_zone2);
            user_id2.setText(username2);


        } else{
            if(first_player.equals(user2_id)){

                first_user_id = Integer.parseInt(user2_id);
                logs.setTextColor(getResources().getColor(R.color.green));
                logs.setText("You're first.");
                isTouchable = true;
                setOnClickListenersForUser(isTouchable);


            }else{

                first_user_id = Integer.parseInt(user1_id);
                logs.setTextColor(getResources().getColor(R.color.red));
                logs.setText(username1 +" is first.");
                isTouchable = false;
                setOnClickListenersForUser(isTouchable);
            }

            user_id1.setText(username2 + "(You)");
            zone_1 = Integer.parseInt(start_zone2);
            zone_2 = Integer.parseInt(start_zone1);
            user_id2.setText(username1);

        }
    }

    //zones part
    public boolean isMyZone(int id) {

        int s_zone1 = Integer.parseInt(start_zone1);
        int s_zone2 = Integer.parseInt(start_zone2);
        zones[0] = 0;
        zones[s_zone1] = Integer.parseInt(user1_id);
        zones[s_zone2] = Integer.parseInt(user2_id);

        int i = get_touched(id);
        return zones[i] == first_user_id;
    }

    public void setOnClickListenersForUser(boolean isTouchable) {
        if (isTouchable) {
            r1.setOnTouchListener(rOnTouch);
            r2.setOnTouchListener(rOnTouch);
            r3.setOnTouchListener(rOnTouch);
            r4.setOnTouchListener(rOnTouch);
            r5.setOnTouchListener(rOnTouch);
        }
    }

    public int get_touched(int id) {

        switch (id) {
            case R.id.r1:
                return 1;
            case R.id.r2:
                return 2;
            case R.id.r3:
                return 3;
            case R.id.r4:
                return 4;
            case R.id.r5:
                return 5;
        }
        return 0;

    }

    public void setCapturedZone(String captured, String u_id, String z) {
        boolean is_captured = Boolean.parseBoolean(captured);
        int user_id = Integer.parseInt(u_id);
        int zone = Integer.parseInt(z);

        //checking is array full or not
        boolean is_full = false;
        for (int i = 0; i < zones.length; i++) {
            if (zones[i] == 0) {
                is_full = true;
            }
        }
        if (is_full) {
            if (is_captured) {
                zones[zone] = user_id;
                if (username1.equals(name)) {
                    if (user_id == Integer.parseInt(user1_id)) {
                        switch (zone) {
                            case 1:
                                r1.setImageResource(R.drawable.map_area_1_green_not_capital);
                                break;
                            case 2:
                                r2.setImageResource(R.drawable.map_area_2_green_not_capital);
                                break;
                            case 3:
                                r3.setImageResource(R.drawable.map_area_3_green_not_capital);
                                break;
                            case 4:
                                r4.setImageResource(R.drawable.map_area_4_green_not_capital);
                                break;
                            case 5:
                                r5.setImageResource(R.drawable.map_area_5_green_not_capital);
                                break;
                        }
                    } else {
                        switch (zone) {
                            case 1:
                                r1.setImageResource(R.drawable.map_area_1_red_not_capital);
                                break;
                            case 2:
                                r2.setImageResource(R.drawable.map_area_2_red_not_capital);
                                break;
                            case 3:
                                r3.setImageResource(R.drawable.map_area_3_red_not_capital);
                                break;
                            case 4:
                                r4.setImageResource(R.drawable.map_area_4_red_not_capital);
                                break;
                            case 5:
                                r5.setImageResource(R.drawable.map_area_5_red_not_capital);
                                break;
                        }
                    }
                } else {
                    if (user_id == Integer.parseInt(user1_id)) {
                        switch (zone) {
                            case 1:
                                r1.setImageResource(R.drawable.map_area_1_red_not_capital);
                                break;
                            case 2:
                                r2.setImageResource(R.drawable.map_area_2_red_not_capital);
                                break;
                            case 3:
                                r3.setImageResource(R.drawable.map_area_3_red_not_capital);
                                break;
                            case 4:
                                r4.setImageResource(R.drawable.map_area_4_red_not_capital);
                                break;
                            case 5:
                                r5.setImageResource(R.drawable.map_area_5_red_not_capital);
                                break;
                        }
                    } else {
                        switch (zone) {
                            case 1:
                                r1.setImageResource(R.drawable.map_area_1_green_not_capital);
                                break;
                            case 2:
                                r2.setImageResource(R.drawable.map_area_2_green_not_capital);
                                break;
                            case 3:
                                r3.setImageResource(R.drawable.map_area_3_green_not_capital);
                                break;
                            case 4:
                                r4.setImageResource(R.drawable.map_area_4_green_not_capital);
                                break;
                            case 5:
                                r5.setImageResource(R.drawable.map_area_5_green_not_capital);
                                break;
                        }
                    }
                }

            } else {
                //not captured
                Toast.makeText(getApplicationContext(), "didn't capture", Toast.LENGTH_LONG).show();
                change_move(user_id);
            }
        } else {
            Log.e(TAG, "is array full: " + String.valueOf(is_full));
        }


    }

    public void change_move(int user_id) {
        if (first_user_id == Integer.parseInt(user1_id)) {
            if (first_user_id == user_id) {
                first_user_id = Integer.parseInt(user2_id);
                setOnClickListenersForUser(true);
            } else {
                first_user_id = Integer.parseInt(user1_id);
                setOnClickListenersForUser(true);
            }
        }else{
            if (first_user_id == user_id) {
                first_user_id = Integer.parseInt(user1_id);
                setOnClickListenersForUser(true);
            } else {
                first_user_id = Integer.parseInt(user1_id);
                setOnClickListenersForUser(true);
            }
        }
    }

    //override methods
    @Override
    public void onBackPressed() {
        if(doublePress){
            super.onBackPressed();
            return;
        }
        this.doublePress = true;
        Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePress = false;
            }
        },2000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    //giving data to server
    public void choose_land(int id){
        final int i = get_touched(id);
        AppController.getApi().choose_land("choose_land", game_id, i, String.valueOf(first_user_id)).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {


            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
            }
        });

    }

    //question part
    private void create_question_to_answer_dialog() {
        QuestionToAnswerFragment questionToAnswerFragment = new QuestionToAnswerFragment(first_user_id);
        questionToAnswerFragment.setArguments(bundle);
        questionToAnswerFragment.show(fm,"tag");
    }

    private void create_question_to_rate_dialog() {
        QuestionToRateFragment questionToRateFragment = new QuestionToRateFragment(first_user_id);
        questionToRateFragment.show(fm,"tag");
    }

}
