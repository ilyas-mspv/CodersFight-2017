package atlascience.bitmaptest.Interfaces;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import atlascience.bitmaptest.Models.Queue.QueueResponse;
import atlascience.bitmaptest.Models.Rating.RatingResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface API {


    // USER FUNCTIONS

    @GET("api.php")
    Call<JsonObject> addUser (
            @Query("v") String version,
            @Query("method") String method,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> getUser(
            @Query("v") String version,
            @Query("method") String method,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> forgotPassword(
            @Query("v") String version,
            @Query("method") String method,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> set_token(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") String id,
            @Query("token") String token
    );

    @GET ("api.php")
    Call<JsonObject> update_url(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") String user_id
    );

    //GAME FUNCTIONS

    @GET ("api.php")
    Call<JsonObject> addtoQueue(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") String id
    );

    @GET ("api.php")
    Call<QueueResponse> get_all_from_queue(
            @Query("v") String version,
            @Query("method") String method
    );

    @GET ("api.php")
    Call<JsonObject> add_to_game(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") String id
    );

    @GET ("api.php")
    Call<JSONObject> invite_to_game(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") int id,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> get_question(
            @Query("v") String version,
            @Query("method") String method,
            @Query("games_id") int game_id,
            @Query("zone") int zone,
            @Query("user") int user
    );

    @GET("api.php")
    Call<RatingResponse> getAllUser(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> set_answer1(
            @Query("v") String version,
            @Query("method") String method,
            @Query("game_round_id") int game_round_id,
            @Query("user_id_one") int user_id1,
            @Query("time_one") double time1,
            @Query("answer_one") int answer1
    );

    @GET("api.php")
    Call<JSONObject> set_answer2(
            @Query("v") String version,
            @Query("method") String method,
            @Query("game_round_id") int game_round_id,
            @Query("user_id_two") int user_id2,
            @Query("time_two") double time2,
            @Query("answer_two") int answer2
    );
    @GET("api.php")
    Call<JSONObject> get_answer_time(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") int user_id,
            @Query("game_round_id") int game_round_id
    );

    @GET("api.php")
    Call<JsonObject> getAllTopics(
            @Query("v") String version,
            @Query("method") String method
    );

    @GET("api.php")
    Call<JSONObject> request_game(
            @Query("v") String version,
            @Query("method") String method,
            @Query("my_id") int my_id,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> approved_request(
            @Query("v") String version,
            @Query("method") String method,
            @Query("apr") int apr_id,
            @Query("req") int req_id,
            @Query("true") String is_true
    );

    @GET("api.php")
    Call<QueueResponse> delete_from_queue(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") int id
    );

    @Multipart
    @POST("api.php")
    Call<JSONObject> upload_photo(
            @Query("v") String version,
            @Part("method") RequestBody method,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part file
    );

    @GET("api.php")
    Call<JsonObject> get_results(
            @Query("v") String version,
        @Query("method") String method,
        @Query("games_id") int games_id,
        @Query("zones_one") int zones1,
        @Query("zones_two") int zones2
    );

    //CONTENT


    @GET("api.php")
    Call<JsonObject> error_info (
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") String user_id,
            @Query("d_model") String d_model,
            @Query("d_model_v") String d_android_version,
            @Query("app_v") String app_v,
            @Query("error") String error
    );



    @GET("api.php")
    Call<JsonObject> provide_question(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") int user_id,
            @Query("question") String question,
            @Query("question_type") String question_type,
            @Query("answer_one") String answer_one,
            @Query("answer_two") String answer_two,
            @Query("answer_three") String answer_three,
            @Query("answer_four") String answer_four,
            @Query("time") int time,
            @Query("true_answer") int true_answer
    );

    @GET("api.php")
    Call<JsonObject> get_stats_data (
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JsonObject> get_code_by_topic (
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") String id
    );


    @GET("api.php")
    Call<JsonObject> get_opened_knowledge(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") String id
    );



    @GET("api.php")
    Call<JsonObject> open_knowledge_topic(
            @Query("v") String version,
            @Query("method") String method,
            @Query("user_id") String id,
            @Query("topic_id") String topic_id
    );


    @GET("api.php")
    Call<JsonObject> get_knowledge_ids_by_user(
            @Query("v") String version,
            @Query("method") String method,
            @Query("id") String id
    );

    //TRAIN MODE

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadCodesRx(@Url String fileUrl);

    @GET("api.php")
    Call<JsonObject> answers_get_all(
            @Query("v") String version,
            @Query("method") String method,
            @Query("code") String code
    );


    @GET("api.php")
    Call<JsonObject> get_code_info(
            @Query("v") String version,
            @Query("method") String method,
            @Query("code") String code
    );

}
