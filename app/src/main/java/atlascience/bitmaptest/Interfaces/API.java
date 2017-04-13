package atlascience.bitmaptest.Interfaces;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import atlascience.bitmaptest.Models.Knowledge.TopicsResponse;
import atlascience.bitmaptest.Models.Queue.QueueResponse;
import atlascience.bitmaptest.Models.Rating.RatingResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {


    // USER FUNCTIONS

    @GET("api.php")
    Call<JsonObject> addUser (
            @Query("method") String method,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> getUser(
            @Query("method") String method,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> forgotPassword(
            @Query("method") String method,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET ("api.php")
    Call<JsonObject> set_token(
            @Query("method") String method,
            @Query("id") String id,
            @Query("token") String token
    );

    //GAME FUNCTIONS

    @GET ("api.php")
    Call<JsonObject> addtoQueue(
            @Query("method") String method,
            @Query("id") String id
    );

    @GET ("api.php")
    Call<QueueResponse> get_all_from_queue(
            @Query("method") String method
    );

    @GET ("api.php")
    Call<JsonObject> add_to_game(
            @Query("method") String method,
            @Query("id") String id
    );

    @GET ("api.php")
    Call<JSONObject> invite_to_game(
            @Query("method") String method,
            @Query("id") int id,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> get_question(
            @Query("method") String method,
            @Query("games_id") int game_id,
            @Query("zone") int zone,
            @Query("user") int user
    );

    @GET("api.php")
    Call<RatingResponse> getAllUser(
            @Query("method") String method,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> set_answer1(
            @Query("method") String method,
            @Query("game_round_id") int game_round_id,
            @Query("user_id_one") int user_id1,
            @Query("time_one") double time1,
            @Query("answer_one") int answer1,
            @Query("move_id") int move_id

    );

    @GET("api.php")
    Call<JSONObject> set_answer2(
            @Query("method") String method,
            @Query("game_round_id") int game_round_id,
            @Query("user_id_two") int user_id2,
            @Query("time_two") double time2,
            @Query("answer_two") int answer2,
            @Query("move_id") int move_id

    );
    @GET("api.php")
    Call<JSONObject> get_answer_time(
            @Query("method") String method,
            @Query("user_id") int user_id,
            @Query("game_round_id") int game_round_id
    );

    @GET("api.php")
    Call<TopicsResponse> getAllTopics(
            @Query("method") String method
    );

    @GET("api.php")
    Call<JSONObject> request_game(
            @Query("method") String method,
            @Query("my_id") int my_id,
            @Query("user_id") int user_id
    );

    @GET("api.php")
    Call<JSONObject> approved_request(
            @Query("method") String method,
            @Query("apr") int apr_id,
            @Query("req") int req_id,
            @Query("true") String is_true
    );

    @GET("api.php")
    Call<QueueResponse> delete_from_queue(
            @Query("method") String method,
            @Query("id") int id
    );

    @Multipart
    @POST("api.php")
    Call<JSONObject> upload_photo(
            @Part("method") RequestBody method,
            @Part("username") RequestBody username,
            @Part MultipartBody.Part file
    );

}
