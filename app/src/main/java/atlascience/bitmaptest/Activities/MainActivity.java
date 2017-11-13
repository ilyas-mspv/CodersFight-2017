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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Fragments.AnswerResultFragment;
import atlascience.bitmaptest.Fragments.QuestionFragment;
import atlascience.bitmaptest.Models.Game.Game;
import atlascience.bitmaptest.Models.Game.Question;
import atlascience.bitmaptest.Models.Game.Zones;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Services.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseAppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.user_id1) TextView textView_user_id1;
    @BindView(R.id.user_id2) TextView textView_user_id2;
    @BindView(R.id.logs_main) TextView textview_logs;
    @BindView(R.id.steps_counter_game) TextView steps_counter_game;
    @BindView(R.id.r1) ImageView r1;
    @BindView(R.id.r2) ImageView r2;
    @BindView(R.id.r3) ImageView r3;
    @BindView(R.id.r4) ImageView r4;
    @BindView(R.id.r5) ImageView r5;

    private static final String TAG = MainActivity.class.getSimpleName();
    SessionManager session;
    int account_status = 0;
    FragmentManager fm = getSupportFragmentManager();
    Bundle bundle = new Bundle();
    AlertDialog.Builder wait_dialog; AlertDialog d;
    boolean doublePress = false;

    //users' data
    String  game_id,name,
            first_player,
            user_id1, user_id2,
            start_zone1, start_zone2,
            username1, username2;
    int move_user_id;

    int stepsCounter = 0;
    int[] areas = new int[5];
    int zone_1, zone_2;
    boolean is_success_answer = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    ImageView[] zonesImageView;
    int[] zonesDrawablesCapitalGreen = new int[]{
            R.drawable.map_area_1_green_capital,
            R.drawable.map_area_2_green_capital,
            R.drawable.map_area_3_green_capital,
            R.drawable.map_area_4_green_capital,
            R.drawable.map_area_5_green_capital};

    int[] zonesDrawablesCapitalRed = new int[] {
            R.drawable.map_area_1_red_capital,
            R.drawable.map_area_2_red_capital,
            R.drawable.map_area_3_red_capital,
            R.drawable.map_area_4_red_capital,
            R.drawable.map_area_5_red_capital};

    int[] zonesDrawablesNotCapitalRed = new int[] {
            R.drawable.map_area_1_red_not_capital,
            R.drawable.map_area_2_red_not_capital,
            R.drawable.map_area_3_red_not_capital,
            R.drawable.map_area_4_red_not_capital,
            R.drawable.map_area_5_red_not_capital};

    int[] zonesDrawablesNotCapitalGreen = new int[] {
            R.drawable.map_area_1_green_not_capital,
            R.drawable.map_area_2_green_not_capital,
            R.drawable.map_area_3_green_not_capital,
            R.drawable.map_area_4_green_not_capital,
            R.drawable.map_area_5_green_not_capital};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        zonesImageView = new ImageView[] {r1, r2, r3, r4, r5};

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        account_status = Integer.parseInt(user.get(SessionManager.KEY_ACCOUNT_STATUS));

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
                        case "question":
                            int user_id = intent.getIntExtra("user_id", 0);
                            int question_id = intent.getIntExtra("question_id", 0);
                            String question_type = intent.getStringExtra("question_type");
                            String question_answer = intent.getStringExtra("question");
                            String answer1_question = intent.getStringExtra("answer1");
                            String answer2_question = intent.getStringExtra("answer2");
                            String answer3_question = intent.getStringExtra("answer3");
                            String answer4_question = intent.getStringExtra("answer4");
                            int time = intent.getIntExtra("time", 0);
                            int answer_true = intent.getIntExtra("answer_true", 0);
                            int zone_question = intent.getIntExtra("zone",0);
                            int game_round_id = intent.getIntExtra("game_round_id",0);

                            Question ques = new Question(getApplicationContext());
                            Question.create_question(tag, user_id, question_id,
                                                question_answer,
                                                answer1_question, answer2_question, answer3_question, answer4_question,
                                    answer_true, time,zone_question,game_round_id,question_type);

                            HashMap<String, String> question_data = Question.getQuestion();
                            String question = question_data.get(Question.KEY_QUESTION);
                            String ques_type = question_data.get("question_type");

                            if (!question.equals("")){
                                if(ques_type.equals("insert")){
                                    create_question_dialog();

                                }
                                if(ques_type.equals("select")){
                                    create_question_dialog();
                                }
                            }

                            break;

                        case "success_answer":
                            Zones _zones = new Zones(getApplicationContext());
                            HashMap<String,String> s = Zones.get_success();
                            is_success_answer = Boolean.parseBoolean(s.get("success"));
                            Question _question = new Question(getApplicationContext());
                            HashMap<String, String> q_data = Question.getQuestion();
                            if(is_success_answer){
                                int my_id;
                                if(username1.equals(name)){
                                    my_id = Integer.parseInt(user_id1);
                                }else{
                                    my_id = Integer.parseInt(user_id2);
                                }
                                AppController.getApi().get_answer_time(Constants.Methods.Version.VERSION,Constants.Methods.Game.GET_ANSWER_TIME,my_id,
                                        Integer.parseInt(q_data.get("game_round_id"))).enqueue(new Callback<JSONObject>() {
                                    @Override
                                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<JSONObject> call, Throwable t) {

                                    }
                                });
                            }
                            break;

                        case "answer":

                            int _winner = intent.getIntExtra("winner",0);
                            int _zone = intent.getIntExtra("zone",0);
                            double _time1 = intent.getDoubleExtra("time1",0);
                            double _time2 = intent.getDoubleExtra("time2",0);
                            int _answer1 = intent.getIntExtra("answer1",0);
                            int _answer2 = intent.getIntExtra("answer2",0);
                            String correct1 = intent.getStringExtra("correct1");
                            String correct2 = intent.getStringExtra("correct2");
                            boolean _win1 = intent.getBooleanExtra("win1",false);
                            boolean _win2 = intent.getBooleanExtra("win2",false);

                            Zones zones = new Zones(getApplicationContext());
                            Zones.create_result_session(_winner,_zone,_time1,_time2,_answer1,_answer2,correct1,correct2,_win1,_win2);

                            HashMap<String, String> zones_data = Zones.get_result_session();
                            int winner = Integer.parseInt(zones_data.get(Zones.KEY_WINNER));
                            int zone = Integer.parseInt(zones_data.get(Zones.KEY_ZONE));
                            double time1 = Double.parseDouble(zones_data.get(Zones.KEY_TIME1));
                            double time2 = Double.parseDouble(zones_data.get(Zones.KEY_TIME2));
                            String answer1 = zones_data.get(Zones.KEY_ANSWER1);
                            String answer2 = zones_data.get(Zones.KEY_ANSWER2);


                            Log.i(TAG, String.valueOf(winner));
                            Log.i(TAG, String.valueOf(zone));
                            Log.i(TAG, String.valueOf(time1));
                            Log.i(TAG, String.valueOf(time2));
                            Log.i(TAG, String.valueOf(answer1));
                            Log.i(TAG, String.valueOf(answer2));

                            int u_id1 = Integer.parseInt(user_id1);
                            int u_id2 = Integer.parseInt(user_id2);

                            setArea( winner, zone, u_id1, u_id2,time1,time2,answer1,answer2,correct1,correct2);

                            break;
                    }
                }
            }
        };
    }

    //sets when Game has just created
    private void initMap() {

        for (int i = 0; i < 4; i++) {
            zonesImageView[i].setDrawingCacheEnabled(true);
        }

//        Button finish_test = (Button) findViewById(R.id.finish_test);
//        finish_test.setVisibility(View.GONE);
//
//        if(account_status==2){
//            finish_test.setVisibility(View.VISIBLE);
//            finish_test.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Game game = new Game(getApplicationContext());
//                    Zones z = new Zones(getApplicationContext());
//                    Question q = new Question(getApplicationContext());
//                    Zones.delete();
//                    Game.delete_game();
//                    Question.delete();
//                    System.exit(0);
//                }
//            });
//        }


    }

    private  void GetSetData(){

        try{
            Game game = new Game(getApplicationContext());
            HashMap<String, String> data = Game.getDetails();
            game_id = data.get(Game.KEY_GAME_ID);
            first_player = data.get(Game.KEY_FIRST);
            user_id1 = data.get(Game.KEY_USER_ID1);
            user_id2 = data.get(Game.KEY_USER_ID2);
            start_zone1 = data.get(Game.KEY_START_ZONE_1);
            start_zone2 = data.get(Game.KEY_START_ZONE_2);
            username1 = data.get(Game.KEY_USERNAME_1);
            username2 = data.get(Game.KEY_USERNAME_2);
        }catch (Error e){
            Log.e(TAG,e.toString());
        }

        init_first_player();


        for (int i = 0; i < 5; i++) {
            if(i ==zone_1-1) zonesImageView[i].setImageResource(zonesDrawablesCapitalGreen[i]);
        }
        for (int i = 0; i < 5; i++) {
            if(i==zone_2-1) zonesImageView[i].setImageResource(zonesDrawablesCapitalRed[i]);
        }
    }

    private void init_first_player() {

        //if username1 equals default profile's name, define first player.
        if(username1.equals(name)) {
            //if user1 is first
            if(first_player.equals(user_id1)) {

                move_user_id = Integer.parseInt(user_id1);
                textview_logs.setTextColor(getResources().getColor(R.color.green));
                textview_logs.setText(username1+"'s"+" move!");
                setOnClickListenersForUser(true);

            }else{

                move_user_id = Integer.parseInt(user_id2);
                textview_logs.setTextColor(getResources().getColor(R.color.red));
                textview_logs.setText(username2 +"'s"+" move!");
                setOnClickListenersForUser(false);

            }

            textView_user_id1.setText(username1 + "(You)");
            zone_1 = Integer.parseInt(start_zone1);
            zone_2 = Integer.parseInt(start_zone2);
            textView_user_id2.setText(username2);


        } else{
            if(first_player.equals(user_id2)){

                move_user_id = Integer.parseInt(user_id2);
                textview_logs.setTextColor(getResources().getColor(R.color.green));
                textview_logs.setText(username2 +"'s"+" move!");
                setOnClickListenersForUser(true);


            }else{

                move_user_id = Integer.parseInt(user_id1);
                textview_logs.setTextColor(getResources().getColor(R.color.red));
                textview_logs.setText(username1 +"'s"+" move!");
                setOnClickListenersForUser(false);
            }

            textView_user_id1.setText(username2 + "(You)");
            zone_1 = Integer.parseInt(start_zone2);
            zone_2 = Integer.parseInt(start_zone1);
            textView_user_id2.setText(username1);

        }
    }

    //areas part
    public boolean isMyZone(int id) {
        int i = get_touched(id);
        int my_id = 0;
        if(username1.equals(name)){
            my_id = Integer.parseInt(user_id1);
        }else{
            my_id = Integer.parseInt(user_id2);
        }
        return areas[i-1] == my_id;
    }

    public void setOnClickListenersForUser(boolean isTouchable) {

        if (isTouchable) {

            for (int i = 0; i < 5; i++) {
                zonesImageView[i].setEnabled(true);
                zonesImageView[i].setOnTouchListener(rOnTouch);
            }

        }else{

            for (int i = 0; i < 5; i++) {
                zonesImageView[i].setEnabled(false);
            }
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

    private boolean isCapital(int zone) {
        return zone == Integer.parseInt(start_zone1) || zone == Integer.parseInt(start_zone2);
    }

    private void setArea(int winner,int zone,int u_id1,int u_id2,double time1,double time2,String answer1,String answer2, String correct1,String correct2) {
        Game game = new Game(getApplicationContext());

        areas[Integer.parseInt(start_zone1)-1] = u_id1;
        areas[Integer.parseInt(start_zone2)-1] = u_id2;
        stepsCounter++;
        if(isStepAvailable(stepsCounter) && isAreaAvailable() && !isAllInvaded()){
            if(account_status==2)
            if(isAreaAvailable()) Toast.makeText(getApplicationContext(),"available",Toast.LENGTH_SHORT).show();

            steps_counter_game.setText("Steps: "+String.valueOf(stepsCounter));
            create_result_dialog_round(winner, time1, time2, answer1, answer2,correct1,correct2);
            if (username1.equals(name)) {
                //user1 green
                //user2 red
                if (winner == u_id1) {
                    //green
                    areas[zone - 1] = u_id1;
                    set_green_zone(zone);

                    textview_logs.setTextColor(getResources().getColor(R.color.green));
                    textview_logs.setText(username1 + "'s move!");
                    move_user_id = u_id1;
                    setOnClickListenersForUser(true);

                }
                if (winner == u_id2) {
                    //red
                    set_red_zone(zone);
                    areas[zone - 1] = u_id2;

                    textview_logs.setTextColor(getResources().getColor(R.color.red));
                    textview_logs.setText(username2 + "'s move!");
                    move_user_id = u_id2;
                    setOnClickListenersForUser(false);
                }
                if (winner == 0) {
                    if (move_user_id == u_id1) {
                        textview_logs.setTextColor(getResources().getColor(R.color.red));
                        textview_logs.setText(username2 + "'s" + " move!");
                        move_user_id = u_id2;
                        setOnClickListenersForUser(false);
                    } else {
                        textview_logs.setTextColor(getResources().getColor(R.color.green));
                        textview_logs.setText(username1 + "'s" + " move!");
                        move_user_id = u_id1;
                        setOnClickListenersForUser(true);
                    }
                }

            } else {
                //user1 red
                //user2 green
                if(account_status==2)
                    if(isAreaAvailable()) Toast.makeText(getApplicationContext(),"available",Toast.LENGTH_SHORT).show();

                if (winner == u_id2) {
                    //green
                    areas[zone - 1] = u_id2;
                    set_green_zone(zone);

                    textview_logs.setTextColor(getResources().getColor(R.color.green));
                    textview_logs.setText(username2 + "'s" + " move!");
                    move_user_id = u_id2;
                    setOnClickListenersForUser(true);

                }
                if (winner == u_id1) {
                    //red
                    set_red_zone(zone);
                    areas[zone - 1] = u_id1;

                    textview_logs.setTextColor(getResources().getColor(R.color.red));
                    textview_logs.setText(username1 + "'s" + " move!");
                    move_user_id = u_id1;
                    setOnClickListenersForUser(false);

                }
                if (winner == 0) {
                    if (move_user_id == u_id2) {
                        textview_logs.setTextColor(getResources().getColor(R.color.red));
                        textview_logs.setText(username1 + "'s" + " move!");
                        move_user_id = u_id1;

                        setOnClickListenersForUser(false);
                    } else {
                        textview_logs.setTextColor(getResources().getColor(R.color.green));
                        textview_logs.setText(username2 + "'s" + " move!");
                        move_user_id = u_id2;
                        setOnClickListenersForUser(true);
                    }
                }

            }

        }else{
            if(account_status==2)
                if(!isAreaAvailable())
                    Toast.makeText(getApplicationContext(), "not available", Toast.LENGTH_SHORT).show();

            //end round
            int zones1 = zones1();
            int zones2 = zones2();
            create_result_dialog_end(winner, time1, time2, answer1, answer2,correct1,correct2,zones1,zones2);

        }
    }

    private int zones1(){
        int c = 0;
        for (int i = 0; i < areas.length; i++) {
            if(areas[i]==Integer.parseInt(user_id1)){
                c++;
            }
        }
        return c;
    }

    private int zones2(){
        int c = 0;
        for (int i = 0; i < areas.length; i++) {
            if(areas[i]==Integer.parseInt(user_id2)){
                c++;
            }
        }
        return c;
    }


    private void set_red_zone(int zone) {
        if(!isCapital(zone)){

            for (int i = 0; i < 5; i++) {
                if(zone-1 == i) zonesImageView[i].setImageResource(zonesDrawablesNotCapitalRed[i]);
            }

        }else{

            for (int i = 0; i < 5; i++) {
                if(zone-1 ==i) zonesImageView[i].setImageResource(zonesDrawablesCapitalRed[i]);
            }
        }
    }

    private void set_green_zone(int zone) {
        if(!isCapital(zone)){

            for (int i = 0; i < 5; i++) {
                if(zone-1 ==i) zonesImageView[i].setImageResource(zonesDrawablesNotCapitalGreen[i]);
            }
        }else{
            for (int i = 0; i < 5; i++) {
                if(zone-1 ==i) zonesImageView[i].setImageResource(zonesDrawablesCapitalGreen[i]);
            }
        }

    }

   private boolean isAreaAvailable(){
       int counter = 0;
       for (int i = 0; i < areas.length; i++) {
           if(areas[i]==Integer.parseInt(user_id1) || areas[i] == Integer.parseInt(user_id2))
               counter++;
       }

       return counter != areas.length;
   }

    private boolean isAlmostAllInvaded(){
        int counter =0;
        int my_id;
        if(username1.equals(name)){
            my_id = Integer.parseInt(user_id1);
        }else{
            my_id = Integer.parseInt(user_id2);
        }

        for (int i = 0; i < areas.length; i++) {
            if(areas[i]==my_id){
                counter++;
            }
        }
        return counter == 4;
    }

    private  boolean isAllInvaded(){
        int counter =0;
        int my_id;
        if(username1.equals(name)){
            my_id = Integer.parseInt(user_id1);
        }else{
            my_id = Integer.parseInt(user_id2);
        }

        for (int i = 0; i < areas.length; i++) {
            if(areas[i]==my_id){
                counter++;
            }
        }
        return counter == areas.length;
    }

    public boolean isStepAvailable(int move){
        int steps = 6;
        return move < steps;
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
        // by doing this, the activity will be notified each time areasFlag new message arrives
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
        unbinder.unbind();

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

    }

    //giving data to server
    public void choose_land(int id){
        final int i = get_touched(id);
        AppController.getApi().get_question(Constants.Methods.Version.VERSION,Constants.Methods.Game.Question.GET, Integer.parseInt(game_id), i, move_user_id).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

        //question part
    private void create_question_dialog() {
        wait_dialog = new AlertDialog.Builder(MainActivity.this);
        wait_dialog.setTitle(getString(R.string.dialog_load_type));
        d = wait_dialog.create();
        d.show();
        int my_id;
        if(username1.equals(name)){
            my_id = Integer.parseInt(user_id1);
        }else{
            my_id = Integer.parseInt(user_id2);
        }
        QuestionFragment questionFragment = new QuestionFragment(my_id);
        questionFragment.setArguments(bundle);
        questionFragment.show(fm,"tag");
    }

    private void create_result_dialog_round(int winner, double time1, double time2, String answer1, String answer2, String correct1, String correct2) {
        wait_dialog.setTitle("Forward to the answer");
        AnswerResultFragment fragment = new AnswerResultFragment(winner,time1,time2,answer1,answer2,correct1,correct2,0,d);
        fragment.show(fm,"tag");
    }

    private void create_result_dialog_end(int winner, double time1, double time2, String answer1, String answer2, String correct1, String correct2,int zones1,int zones2) {
        wait_dialog.setTitle("Forward to the answer");
        AnswerResultFragment fragment = new AnswerResultFragment(winner,time1,time2,answer1,answer2,correct1,correct2,1,zones1,zones2,d);
        fragment.show(fm,"tag");
    }

    public void limit_clicks(){

        for (int i = 0; i < 5; i++) {
            zonesImageView[i].setEnabled(false);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    zonesImageView[i].setEnabled(true);
                }
            }
        },1000);
    }


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

//                        if(!isMyZone(v.getId()))
                            if(!isCapital(get_touched(v.getId()))){
                                choose_land(v.getId());
                            }
//                            else{
//                                if(isAlmostAllInvaded())
//                                    Toast.makeText(MainActivity.this, "yeah", Toast.LENGTH_SHORT).show();
//                                else
//                                    Toast.makeText(MainActivity.this, "nope", Toast.LENGTH_SHORT).show();

//                            }
                        limit_clicks();
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