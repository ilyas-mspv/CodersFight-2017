package atlascience.bitmaptest.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.util.StringTokenizer;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Auth.SessionManager;
import atlascience.bitmaptest.Fragments.QuestionToAnswerFragment;
import atlascience.bitmaptest.Fragments.QuestionToRateFragment;
import atlascience.bitmaptest.Objects.Game;
import atlascience.bitmaptest.Objects.Question;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    //map and activity
    ImageView r1,r2,r3,r4,r5;
    boolean doublePress = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SessionManager session;

    //users' data
    TextView user_id1,user_id2,logs;
    String  game_id,first_player,user1_id,user2_id,start_zone1,start_zone2;
    String username1,username2;
    String zone1_user1,zone2_user2;
    int user_id;
    int[] zones = new int[6];

    //question data
    int time;
    String question,answer1,answer2,answer3,answer4;
    FragmentManager fm = getSupportFragmentManager();



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
                    int user_id = intent.getIntExtra("user_id",0);
                    int question_id = intent.getIntExtra("question_id",0);
                    String question = intent.getStringExtra("question");
                    String answer1 = intent.getStringExtra("answer1");
                    String answer2 = intent.getStringExtra("answer2");
                    String answer3 = intent.getStringExtra("answer3");
                    String answer4 = intent.getStringExtra("answer4");
                    int answer_true = intent.getIntExtra("answer_true",0);
                    int time = intent.getIntExtra("time",0);
                    Question question_obj = new Question(getApplicationContext());
                    question_obj.create_question(user_id,question_id,question,answer1,answer2,answer3,answer4,answer_true,time);

                    if(tag.equals("question_to_answer")) create_question_to_answer_dialog();
                    if(tag.equals("question_to_rate")) create_question_to_rate_dialog();
                }
            }
        };
    }



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
                game.delete_game();
            }
        });

    }

    public void setOnClickListenersForUser(){
        r1.setOnTouchListener(rOnTouch);
        r2.setOnTouchListener(rOnTouch);
        r3.setOnTouchListener(rOnTouch);
        r4.setOnTouchListener(rOnTouch);
        r5.setOnTouchListener(rOnTouch);
    }
    private  void GetSetData(){

        int zone_1;
        int zone_2;
        user_id1 = (TextView) findViewById(R.id.user_id1);
        user_id2 = (TextView) findViewById(R.id.user_id2);
        logs = (TextView) findViewById(R.id.logs_main);

        //get game details
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

        //setting user's default data
        HashMap<String, String> user = session.getUserDetails();
        final String name = user.get(SessionManager.KEY_NAME);

        //if username1 equals default profile's name, define first player.
        if(username1.equals(name)) {
            //if user1 is first
            if(first_player.equals(user1_id)) {

                user_id = Integer.parseInt(user1_id);
                logs.setTextColor(getResources().getColor(R.color.green));
                logs.setText("You're first.");
                setOnClickListenersForUser();


            }else{

                user_id = Integer.parseInt(user2_id);
                logs.setTextColor(getResources().getColor(R.color.red));
                logs.setText(username2 +" is first.");

            }

            user_id1.setText(username1 + "(You)");
            zone_1 = Integer.parseInt(start_zone1);
            zone_2 = Integer.parseInt(start_zone2);
            user_id2.setText(username2);


        } else{
            if(first_player.equals(user2_id)){

                user_id = Integer.parseInt(user2_id);
                logs.setTextColor(getResources().getColor(R.color.green));
                logs.setText("You're first.");
                setOnClickListenersForUser();


            }else{

                user_id = Integer.parseInt(user1_id);
                logs.setTextColor(getResources().getColor(R.color.red));
                logs.setText(username1 +" is first.");


            }

            user_id1.setText(username2 + "(You)");
            zone_1 = Integer.parseInt(start_zone2);
            zone_2 = Integer.parseInt(start_zone1);
            user_id2.setText(username1);

        }



        //TODO switch to background opacity
        switch (zone_1){
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

        switch (zone_2){
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


    public boolean isMyZone(int id){

        int s_zone1 = Integer.parseInt(start_zone1);
        int s_zone2 = Integer.parseInt(start_zone2);
        zones[s_zone1] = Integer.parseInt(user1_id);
        zones[s_zone2] = Integer.parseInt(user2_id);

         int i = get_touched(id);
        if(zones[i] == user_id){
            return true;
        }else{
            return false;
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



    public int get_touched(int id){
    switch (id){
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
    public void choose_land(int id){
        int i = get_touched(id);
        AppController.getApi().choose_land("choose_land",game_id,i,String.valueOf(user_id)).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });

    }
    private void create_question_to_answer_dialog() {
        QuestionToAnswerFragment questionToAnswerFragment = new QuestionToAnswerFragment(user_id);
        questionToAnswerFragment.show(fm,"tag");
    }
    private void create_question_to_rate_dialog() {
        QuestionToRateFragment questionToRateFragment = new QuestionToRateFragment();
        questionToRateFragment.show(fm,"tag");
    }


    private final View.OnTouchListener rOnTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            int color = 0;
            try {
                color = bmp.getPixel((int) event.getX(), (int) event.getY());
            } catch (Exception e) {
                Log.e("COLOR ERROR",e.toString());
            }
            if (color == Color.TRANSPARENT)
                return false;
            else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isMyZone(v.getId())){
                            choose_land(v.getId());
                        }else{
                            Toast.makeText(getApplicationContext(),"Can't capture for a while",Toast.LENGTH_LONG).show();
                        }
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

}
