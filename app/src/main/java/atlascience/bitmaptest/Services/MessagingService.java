package atlascience.bitmaptest.Services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import atlascience.bitmaptest.Activities.MainActivity;
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
                handleDataMessage(json,remoteMessage.getData().toString());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    private void handleDataMessage(JSONObject json,String data) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            String tag = json.getString("tag");
            switch(tag){

                case "game_request":
                    get_request_game(json, "game_request");
                    break;

                case "approval_request":
                    get_approval_request(json, "approval_request");
                    break;

                case "game":
                    get_game(json);
                    break;

                case "question":
                    get_question(json, "question",data);
                    break;

                case "answer":
                    get_answer_time(json, "answer");
                    break;

                case "success_answer":
                    get_answer_success(json, "success_answer");
                    break;
                case "result":
                    get_result(json, "result");
                    break;

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void get_approval_request(JSONObject json, String approval_request) throws JSONException {
        String username = json.getString("username");
        int user_id = json.getInt("user_id");
        String is_approved = json.getString("is_true");


        Game game = new Game(getApplicationContext());
        Game.set_approval(user_id, username, is_approved);

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", approval_request);
        push.putExtra("apr_id", user_id);
        push.putExtra("username", username);
        push.putExtra("is_true", is_approved);
        LocalBroadcastManager.getInstance(this).sendBroadcast(push);

    }

    private void get_request_game(JSONObject json, String game_request) throws JSONException {
        String username = json.getString("username");
        int user_id = json.getInt("user_id");
        Game game = new Game(getApplicationContext());
        Game.set_request(user_id, username);

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", game_request);
        push.putExtra("user_id", user_id);
        push.putExtra("username", username);
        LocalBroadcastManager.getInstance(this).sendBroadcast(push);

    }

    private void get_result(JSONObject json, String result) throws JSONException {
        boolean success = Boolean.parseBoolean(json.getString("success"));
        Zones zones = new Zones(getApplicationContext());
        Zones.success_session(result, success);

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", result);
        push.putExtra("success", success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(push);

    }

    private void get_answer_success(JSONObject json, String success_answer) throws JSONException {

        boolean is_success = Boolean.parseBoolean(json.getString("success"));

        Zones zones = new Zones(getApplicationContext());
        Zones.success_session(success_answer, is_success);
        Log.i(TAG, "SUCCESS" + String.valueOf(is_success));

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", success_answer);
        push.putExtra("success", is_success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(push);
    }

    private void get_answer_time(JSONObject json, String tag) throws JSONException {
        int winner = json.getInt("winner");
        int zone = json.getInt("zone");
        double time1 = json.getDouble("time1");
        double time2 = json.getDouble("time2");
        int answer1 = json.getInt("answer1");
        int answer2 = json.getInt("answer2");
        String winner1 = json.getString("winner1");
        String winner2 = json.getString("winner2");
        String correct1 = json.getString("correct1");
        String correct2 = json.getString("correct2");

        boolean win1 = Boolean.parseBoolean(winner1);
        boolean win2 = Boolean.parseBoolean(winner2);

        Zones zones = new Zones(getApplicationContext());
        Zones.create_result_session(winner, zone, time1, time2, answer1, answer2, correct1, correct2, win1, win2);

        Intent push = new Intent(Config.PUSH_NOTIFICATION);
        push.putExtra("tag", tag);
        push.putExtra("winner", winner);
        push.putExtra("zone", zone);
        push.putExtra("time1", time1);
        push.putExtra("time2", time2);
        push.putExtra("answer1", answer1);
        push.putExtra("answer2", answer2);
        push.putExtra("correct1", correct1);
        push.putExtra("correct2", correct2);
        push.putExtra("win1", win1);
        push.putExtra("win2", win2);
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
            final String username1 = json.getString("username_id1");
            String username2 = json.getString("username_id2");
            JSONObject zones = json.getJSONObject("zones");
            Object zone_1 = zones.get(String.valueOf(start_zone1));
            Object zone_2 = zones.get(String.valueOf(start_zone2));
            String zone1 = zone_1.toString();
            String zone2 = zone_2.toString();

            String user2_photo = "0";
            String user1_photo = "0";


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

            Game.create_game(game_id, user_id1, user_id2, first_player, start_zone1, start_zone2, username1, username2, Integer.parseInt(zone1), Integer.parseInt(zone2),user2_photo,user1_photo);

            startActivity(new Intent(MessagingService.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private void get_question(JSONObject json, String tag,String data) {

        try {


            int user_id = json.getInt("user_id");
            int question_id = json.getInt("question_id");
            String question_answer = json.getString("question");
            String question_type = json.getString("question_type");
            String answer1_answer = json.getString("answer1");
            if(answer1_answer.contains("*%")) answer1_answer.replace("*%","=");
            String answer2_answer = json.getString("answer2");
            if(answer2_answer.contains("*%")) answer2_answer.replace("*%","=");
            String answer3_answer = json.getString("answer3");
            if(answer3_answer.contains("*%")) answer3_answer.replace("*%","=");
            String answer4_answer = json.getString("answer4");
            if(answer4_answer.contains("*%")) answer4_answer.replace("*%","=");
            int time_answer = json.getInt("time");
            int answer_true = json.getInt("answer_true");
            int zone = json.getInt("zone");
            int game_round_id = json.getInt("game_round_id");

            Log.i(TAG,question_answer + " and " + time_answer);
            Log.i(TAG,answer1_answer);
            Log.i(TAG, answer2_answer);
            Log.i(TAG, answer3_answer);
            Log.i(TAG, answer4_answer);
            Log.i(TAG, String.valueOf(answer_true));
            Log.i(TAG, String.valueOf(user_id));

            Question ques = new Question(getApplicationContext());
            Question.create_question(tag, user_id, question_id, question_answer, answer1_answer, answer2_answer, answer3_answer, answer4_answer, answer_true, time_answer, zone, game_round_id, question_type);

                Intent push = new Intent (Config.PUSH_NOTIFICATION);
                push.putExtra("tag",tag);
                push.putExtra("user_id",user_id);
                push.putExtra("question_id",question_id);
            push.putExtra("question_type", question_type);
            push.putExtra("question", question_answer);
                push.putExtra("answer1",answer1_answer);
                push.putExtra("answer2",answer2_answer);
                push.putExtra("answer3",answer3_answer);
                push.putExtra("answer4",answer4_answer);
                push.putExtra("time",time_answer);
                push.putExtra("answer_true",answer_true);
            push.putExtra("zone", zone);
            push.putExtra("game_round_id", game_round_id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(push);


        }catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}