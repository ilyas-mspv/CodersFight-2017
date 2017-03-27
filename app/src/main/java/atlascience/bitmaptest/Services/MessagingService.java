package atlascience.bitmaptest.Services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import atlascience.bitmaptest.Activities.MainActivity;
import atlascience.bitmaptest.Fragments.QuestionToAnswerFragment;
import atlascience.bitmaptest.Models.Game;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.Models.Zones;


public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = MessagingService.class.getSimpleName();



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            String tag = json.getString("tag");
            switch(tag){

                case "game":
                    get_game(json);
                    break;

                case "question_to_answer":
                    get_question_to_answer(json,"question_to_answer");
                    break;

                case "question_to_rate":
                    get_question_to_rate(json,"question_to_rate");
                    break;

                case "answer":
                    get_answer(json, "answer");
                    break;

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void get_answer(JSONObject json, String tag) throws JSONException {

        Boolean captured = json.getBoolean("captured");
        String answer = json.getString("answer");
        String time = json.getString("time");
        int zone = json.getInt("zone");
        int user_id = json.getInt("user_id");

        Zones zones = new Zones(getApplicationContext());
        zones.create_zone_session(tag, captured, zone, user_id);

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", tag);
        push.putExtra("captured", captured);
        push.putExtra("answer", answer);
        push.putExtra("time", time);
        push.putExtra("zone", zone);
        push.putExtra("user_id", user_id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(push);
    }

    private void get_game(JSONObject json) {
        Game game = new Game(getApplicationContext());
        try {
            int game_id = json.getInt("game_id");
            int user_id1 = json.getInt("user_id1");
            int user_id2 = json.getInt("user_id2");
            int start_zone1 = json.getInt("start_zone1");
            int start_zone2 = json.getInt("start_zone2");
            int first_player = json.getInt("first_player");
            String username1 = json.getString("username_id1");
            String username2 = json.getString("username_id2");
            JSONObject zones = json.getJSONObject("zones");
            Object zone_1 = zones.get(String.valueOf(start_zone1));
            Object zone_2 = zones.get(String.valueOf(start_zone2));
            String zone1 = zone_1.toString();
            String zone2 = zone_2.toString();

            Log.e(TAG, zone1);
            Log.e(TAG, zone2);
            Log.i(TAG, String.valueOf(game_id));
            Log.i(TAG, String.valueOf(user_id1));
            Log.i(TAG, String.valueOf(user_id2));
            Log.i(TAG, String.valueOf(start_zone1));
            Log.i(TAG, String.valueOf(start_zone2));
            Log.i(TAG, String.valueOf(first_player));
            Log.i(TAG, username1);
            Log.i(TAG, username2);

            game.create_game(game_id, user_id1, user_id2, first_player, start_zone1, start_zone2, username1, username2, Integer.parseInt(zone1), Integer.parseInt(zone2));
            startActivity(new Intent(MessagingService.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    //todo complete rating part(need another phone)
    private void get_question_to_rate(JSONObject json, String tag) {


        try {
            int time_rate = json.getInt("time");
            String question_rate = json.getString("question");
            String answer1_rate = json.getString("answer1");
            String answer2_rate = json.getString("answer2");
            String answer3_rate = json.getString("answer3");
            String answer4_rate = json.getString("answer4");
            int question_id = json.getInt("question_id");
            int user_id = json.getInt("user_id");
            int answer_true = json.getInt("answer_true");

            Log.i(TAG,question_rate + " and " + time_rate);
            Log.i(TAG,answer1_rate);
            Log.i(TAG,answer2_rate);
            Log.i(TAG,answer3_rate);
            Log.i(TAG,answer4_rate);
            Question ques = new Question(getApplicationContext());
            ques.create_question(tag, user_id, question_id, question_rate, answer1_rate, answer2_rate, answer3_rate, answer4_rate, answer_true, time_rate);

            Intent push = new Intent(Config.PUSH_NOTIFICATION);
                push.putExtra("tag",tag);
                push.putExtra("user_id",user_id);
                push.putExtra("question_id",question_id);
                push.putExtra("answer1",answer1_rate);
                push.putExtra("answer2",answer2_rate);
                push.putExtra("answer3",answer3_rate);
                push.putExtra("answer4",answer4_rate);
                push.putExtra("time",time_rate);
                push.putExtra("answer_true",answer_true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(push);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void get_question_to_answer(JSONObject json,String tag) {

        try {
            int user_id = json.getInt("user_id");
            int question_id = json.getInt("question_id");
            String question_answer = json.getString("question");
            String answer1_answer = json.getString("answer1");
            String answer2_answer = json.getString("answer2");
            String answer3_answer = json.getString("answer3");
            String answer4_answer = json.getString("answer4");
            int time_answer = json.getInt("time");
            int answer_true = json.getInt("answer_true");

            Log.i(TAG,question_answer + " and " + time_answer);
            Log.i(TAG,answer1_answer);
            Log.i(TAG,answer1_answer);
            Log.i(TAG,answer1_answer);
            Log.i(TAG,answer1_answer);
            Log.i(TAG, String.valueOf(answer_true));
            Log.i(TAG, String.valueOf(user_id));

            Question ques = new Question(getApplicationContext());
            ques.create_question(tag, user_id, question_id, question_answer, answer1_answer, answer2_answer, answer3_answer, answer4_answer, answer_true, time_answer);

                Intent push = new Intent (Config.PUSH_NOTIFICATION);
                push.putExtra("tag",tag);
                push.putExtra("user_id",user_id);
                push.putExtra("question_id",question_id);
            push.putExtra("question", question_answer);
                push.putExtra("answer1",answer1_answer);
                push.putExtra("answer2",answer2_answer);
                push.putExtra("answer3",answer3_answer);
                push.putExtra("answer4",answer4_answer);
                push.putExtra("time",time_answer);
                push.putExtra("answer_true",answer_true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(push);


        }catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }



}