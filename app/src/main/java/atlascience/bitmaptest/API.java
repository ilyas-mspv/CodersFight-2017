package atlascience.bitmaptest;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<JSONObject> get_all_from_queue(
            @Query("method") String method
    );

    @GET ("api.php")
    Call<JsonObject> add_to_game(
            @Query("method") String method,
            @Query("id") String id
    );

    @GET ("api.php")
    Call<JSONObject> choose_land(
            @Query("method") String method,
            @Query("game_id") String game_id,
            @Query("zone") int zone,
            @Query("first") String first
    );

    @GET("api.php")
    Call<JSONObject> getAllUser(
            @Query("method") String method
    );

    @GET("api.php")
    Call<JSONObject> get_answer(
            @Query("method") String method,
            @Query("game_id") String game_id,
            @Query("question_id") String queston_id,
            @Query("answer") String answer,
            @Query("time") String time,
            @Query("user_id") String user_id
    );



}
