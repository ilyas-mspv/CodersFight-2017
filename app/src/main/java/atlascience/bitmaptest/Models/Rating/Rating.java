package atlascience.bitmaptest.Models.Rating;


import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("id")
    public int id;
    @SerializedName("order_number")
    public int order_num_id;
    @SerializedName("username")
    public String username;
    @SerializedName("rating")
    public int rating;

    public Rating(int id, int order_id, String username, int rating) {
        this.id = id;
        this.order_num_id = order_id;
        this.username = username;
        this.rating = rating;
    }

    public int get_order_num_id() {
        return order_num_id;
    }

    public void set_order_num_id(int id) {
        this.id = order_num_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }



}
