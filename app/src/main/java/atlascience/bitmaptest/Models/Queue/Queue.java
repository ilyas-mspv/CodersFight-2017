package atlascience.bitmaptest.Models.Queue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilyas on 03-Apr-17.
 */

public class Queue {
    @SerializedName("order_number")
    private int order_number;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("rating")
    private int rating;
    @SerializedName("username")
    private String username;

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
