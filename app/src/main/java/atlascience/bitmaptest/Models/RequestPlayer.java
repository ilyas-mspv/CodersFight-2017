package atlascience.bitmaptest.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilyas on 06-Apr-17.
 */

public class RequestPlayer {

    @SerializedName("user_id")
    int user_id;
    @SerializedName("username")
    String username;


    public RequestPlayer(int user_id, String username) {
        this.user_id = user_id;
        this.username = username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
